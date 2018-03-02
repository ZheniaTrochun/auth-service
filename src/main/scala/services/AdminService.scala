package services

import db.UserCredsRepository
import slick.basic.DatabaseConfig
import slick.jdbc.JdbcProfile

trait AdminService {
  def createTable(): Unit

  def dropTable(): Unit
}

class AdminServiceImpl(val dbConfig: DatabaseConfig[JdbcProfile]) extends AdminService {

  val db = dbConfig.db
  val driver = dbConfig.profile

  val userCredsRepository = new UserCredsRepository(driver)

  override def createTable(): Unit = {
    db.run(userCredsRepository.createSchema())
  }

  override def dropTable(): Unit = {
    db.run(userCredsRepository.dropSchema())
  }
}