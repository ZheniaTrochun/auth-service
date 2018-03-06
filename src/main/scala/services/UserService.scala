package services

import db.UserCredsRepository
import org.slf4j.LoggerFactory
import slick.basic.DatabaseConfig
import slick.jdbc.JdbcProfile

import scala.concurrent.Future

import scala.concurrent.ExecutionContext.Implicits.global

trait UserService {
  def delete(name: String): Future[Boolean]
}

class UserServiceImpl(val dbConfig: DatabaseConfig[JdbcProfile]) extends UserService {
  val db = dbConfig.db
  val driver = dbConfig.profile

  val logger = LoggerFactory.getLogger(this.getClass)

  private val userCredsRepository = new UserCredsRepository(driver)


  override def delete(name: String): Future[Boolean] = {
    logger.debug(s"Deleting user $name ...")

    db.run(userCredsRepository.deleteByName(name)) flatMap { num: Int =>
      logger.debug(s"Deleted $num users")
      Future.successful(num != 0)
    }
  }
}
