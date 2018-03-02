package services

import akka.http.scaladsl.Http
import akka.http.scaladsl.client.RequestBuilding
import akka.http.scaladsl.model.{HttpRequest, HttpResponse}
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

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

trait NamePassAuthService {
  def register(name: String, password: String, email: String): Future[Boolean]

  def login(name: String, password: String): Future[Option[String]]
}

class NamePassAuthServiceImpl
  (val dbConfig: DatabaseConfig[JdbcProfile])
  (implicit val mat: Materializer)
    extends NamePassAuthService with JwtUtils {

  val config = ConfigFactory.load()

  private val db = dbConfig.db
  private val driver = dbConfig.profile

  private val userCredsRepository = new UserCredsRepository(driver)

  lazy val ipApiConnectionFlow: Flow[HttpRequest, HttpResponse, Any] =
    Http().outgoingConnection(config.getString("services.data-service.host"))

  def ipApiRequest(request: HttpRequest): Future[HttpResponse] = Source.single(request).via(ipApiConnectionFlow).runWith(Sink.head)

  override def register(name: String, password: String, email: String): Future[Boolean] = {
    val creds = UserCreds(name = Some(name), passwordHash = Some(password.bcrypt))

    ipApiRequest(RequestBuilding.Post("/users/", UserDto(name, email))) flatMap { response =>
      response.status match {
        case OK =>
          db.run(userCredsRepository.save(creds)) map { _: UserCreds =>
            true
          } recover {
            case _ =>
              false
          }

        case _ =>
          Future.successful(false)
      }
    }
  }

  override def login(name: String, password: String): Future[Option[String]] = {
    db.run(userCredsRepository.findByName(name)) map {
      case Some(userCreds) =>
        if (password.isBcrypted(userCreds.passwordHash.get)) {
          Some(createToken(name))
        } else {
          None
        }

      case _ =>
        None
    }
  }
}