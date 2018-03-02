package services

import akka.actor.ActorSystem
import akka.http.javadsl.model.StatusCodes
import akka.http.scaladsl.Http
import akka.http.scaladsl.client.RequestBuilding
import akka.http.scaladsl.model.{HttpRequest, HttpResponse, StatusCode}
import akka.http.scaladsl.model.StatusCodes._
import akka.stream.Materializer
import akka.stream.scaladsl.{Flow, Sink, Source}
import db.UserCredsRepository
import models.{UserCreds, UserDto}
import slick.basic.DatabaseConfig
import slick.jdbc.JdbcProfile
import com.github.t3hnar.bcrypt._
import com.typesafe.config.ConfigFactory
import security.JwtUtils
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import models.json.JsonProtocol
import org.slf4j.LoggerFactory

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

trait NamePassAuthService {
  def register(name: String, password: String, email: String): Future[Boolean]

  def login(name: String, password: String): Future[Option[String]]
}

class NamePassAuthServiceImpl
  (val dbConfig: DatabaseConfig[JdbcProfile])
  (implicit val mat: Materializer, implicit val system: ActorSystem)
    extends NamePassAuthService with JwtUtils with JsonProtocol {

  val config = ConfigFactory.load()

  val db = dbConfig.db
  val driver = dbConfig.profile

  val logger = LoggerFactory.getLogger(this.getClass)

  private val userCredsRepository = new UserCredsRepository(driver)

  lazy val ipApiConnectionFlow: Flow[HttpRequest, HttpResponse, Any] =
    Http().outgoingConnection(config.getString("services.data-service.host"))

  def ipApiRequest(request: HttpRequest): Future[HttpResponse] = Source.single(request).via(ipApiConnectionFlow).runWith(Sink.head)

  override def register(name: String, password: String, email: String): Future[Boolean] = {
    val creds = UserCreds(name = Some(name), passwordHash = Some(password.bcrypt.replace('$', ':')))

    logger.debug(s"Start of register request processing... $creds")

    ipApiRequest(RequestBuilding.Post("/users/", UserDto(name, email))) flatMap { response =>
      response.status match {
        case OK =>
          logger.debug("Data-service responded with status OK(200)")

          db.run(userCredsRepository.save(creds)) map { _: UserCreds =>
            logger.debug("User credentials saving successful")
            true
          } recover {
            case ex: Exception =>
              logger.error("User credentials saving failed!", ex)
              false
          }

        case code: StatusCode =>
          logger.error(s"Data-service request failed with code $code MISSION ABORT!")
          Future.successful(false)
      }
    }
  }

  override def login(name: String, password: String): Future[Option[String]] = {
    logger.debug("Start of login request processing...")

    db.run(userCredsRepository.findByName(name)) map {
      case Some(userCreds) =>
        logger.debug("User fond successfully")

        if (password.isBcrypted(userCreds.passwordHash.get.replace(':', '$'))) {

          logger.debug("Password matched!")
          Some(createToken(name))
        } else {

          logger.warn("Invalid password!")
          None
        }

      case _ =>

        logger.error("Invalid response!")
        None
    }
  }
}