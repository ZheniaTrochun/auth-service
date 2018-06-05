package config

import com.typesafe.config.{Config, ConfigFactory}

import scala.language.postfixOps

trait AppConfig {
  val config = new Configs()
  private val httpConfig = config.getConfig("http")

  val httpInterface = httpConfig.getString("interface")
  val httpPort = httpConfig.getInt("port")
}

class Configs {

  private val config = ConfigFactory.load()
//  private val remoteConfigManager = new RemoteConfigManager(config)
//  private var configCache: Map[String, String] = remoteConfigManager.getRemoteConfig()

  def getString(key: String): String = {
//    configCache.getOrElse(key, config.getString(key))
    config.getString(key)
  }

  def getInt(key: String): Int = {
//    configCache.getOrElse(key, config.getString(key)) toInt
    config.getString(key) toInt
  }

  def update(): Unit = {
//    configCache = remoteConfigManager.getRemoteConfig()
  }

  def getConfig(key: String): Config = {
    config.getConfig(key)
  }

  def setRemoteConfig(entry: (String, String)): Unit = {
//    remoteConfigManager.setConfig(entry)
    update()
  }

  def createDummyConfig(): Unit = {
//    remoteConfigManager.createDummyConfig()
    update()
  }

}
