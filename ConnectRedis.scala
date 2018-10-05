package main.java

import org.apache.commons.pool2.impl.GenericObjectPoolConfig
import redis.clients.jedis.JedisPool

object ConnectRedis extends Serializable {
  println("Start")
  val redisHost = "x.x.x.x"
  val redisPort = 6379
  val redisTimeout = 40000
  val redisAuth = "pass"
  lazy val pool = new JedisPool(new GenericObjectPoolConfig(), redisHost, redisPort, redisTimeout, redisAuth)
  lazy val hook = new Thread {
    override def run = {
      println("开始执行Redis线程：" + this)
      pool.destroy()
    }
  }
  println("Over")
}
