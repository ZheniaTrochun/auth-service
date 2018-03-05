package config

import java.net.URI

import com.redis.RedisClient
import com.typesafe.config.Config

class RemoteConfigManager(val config: Config) {

  private val redisClient = new RedisClient(new URI(config.getString("redis.url")))

  def getRemoteConfig(): Map[String, String] = {
    redisClient.hgetall1[String, String]("auth-service-config")
      .getOrElse(Map.empty[String, String])
  }

  def createDummyConfig(): Unit = {
    val conf = Map(
      "api.security.secret" -> "top-secret",
      "api.security.algo" -> "HS256",
      "services.data-service.host" -> "my-data-service.herokuapp.com")

    redisClient.hmset("auth-service-config", conf)
  }

  def setConfig(entry: (String, String)): Unit = {
    redisClient.hset("auth-service-config", entry._1, entry._2)
  }

}
