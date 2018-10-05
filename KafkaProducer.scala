package main.java

import java.util.{Properties, Random}

import kafka.javaapi.producer.Producer
import kafka.producer.{KeyedMessage, ProducerConfig}
import org.codehaus.jettison.json.JSONObject

object KafkaProducer {
  private val users = Array("u1","u2","u3","u4","u5","u6","u7","u8","u9","u10")
  private val random = new Random()
  private var pointer = -1
  def getUserID(): String = {
    pointer = pointer + 1
    if (pointer >= users.length) {
      pointer = 0
      users(pointer)
    } else {
      users(pointer)
    }
  }
  def click(): Double = {
    random.nextInt(10)
  }
  def main(args: Array[String]): Unit = {
    val topic = "User_Events"
    val brokers = "KafkaHost:9092"
    val props = new Properties()
    props.put("metadata.broker.list", brokers)
    props.put("serializer.class", "kafka.serializer.StringEncoder")
    val kafkaConfig = new ProducerConfig(props)
    val producer = new Producer[String, String](kafkaConfig)
    while (true) {
      val event = new JSONObject()
      event
        .put("User_Id", getUserID)
        .put("Event_Time", System.currentTimeMillis.toString)
        .put("Os_Type", "IOS")
        .put("Click_Count", click)
      producer.send(new KeyedMessage[String, String](topic, event.toString))
      println("消息发送Message Sent: " + event)
      Thread.sleep(2000)  //单位毫秒
    }
  }
}
