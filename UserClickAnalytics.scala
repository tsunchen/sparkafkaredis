package main.java

import net.sf.json.JSONObject
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.spark.SparkConf
import org.apache.spark.streaming.kafka010.ConsumerStrategies.Subscribe
import org.apache.spark.streaming.kafka010.KafkaUtils
import org.apache.spark.streaming.kafka010.LocationStrategies.PreferConsistent
import org.apache.spark.streaming.{Seconds, StreamingContext}

object UserClickAnalytics {
  def main(args: Array[String]): Unit = {
    var masterUrl = "local[2]"
    var conf = new SparkConf().setMaster(masterUrl).setAppName("SparKaR_TSUNx")
    var ssc=new StreamingContext(conf,Seconds(4))

    var topic=Array("User_Events")
    var group="con-consumer-group"
    var kafkaParam = Map(
      "bootstrap.servers" -> "KafkaHost:9092",
      "key.deserializer" -> classOf[StringDeserializer],
      "value.deserializer" -> classOf[StringDeserializer],
      "group.id" -> group,
      "auto.offset.reset" -> "latest",
      "enable.auto.commit" -> (false: java.lang.Boolean)
    )
    val dbIndex = 1
    val clickHashKey = "App::User::Click"
    val stream=KafkaUtils.createDirectStream[String,String](ssc, PreferConsistent,Subscribe[String,String](topic,kafkaParam))
    val events = stream.flatMap(line => {
      val data = JSONObject.fromObject(line.value())
      println("输出获取信息数据Get Data：")
      println(data)
      Some(data)
    })
    val userClicks = events.map(x => (x.getString("User_Id"), x.getInt("Click_Count"))).reduceByKey(_ + _)
    userClicks.foreachRDD(rdd => {
      rdd.foreachPartition(partitionOfRecords => {
        partitionOfRecords.foreach(pair => {
          val uid = pair._1
          val clickCount = pair._2
          val jedis = ConnectRedis.pool.getResource
          jedis.select(dbIndex)
          jedis.hincrBy(clickHashKey, uid, clickCount)
          ConnectRedis.pool.returnResourceObject(jedis)
        })
      })
    })
    ssc.start()
    ssc.awaitTermination()
    println("结束Over")
  }
}
