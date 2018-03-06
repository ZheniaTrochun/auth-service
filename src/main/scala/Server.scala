import akka.actor.ActorSystem
import akka.event.{Logging, LoggingAdapter}
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer
import akka.util.Timeout
import api.{AdminRoutes, AuthRoutes, HealthRoute}
import config.AppConfig
import services.{AdminServiceImpl, NamePassAuthServiceImpl, UserServiceImpl}
import slick.basic.DatabaseConfig
import slick.jdbc.JdbcProfile

import scala.concurrent.duration._
import scala.concurrent.ExecutionContext
import scala.language.postfixOps

object Server extends App with AppConfig with HealthRoute {
  implicit val system: ActorSystem = ActorSystem()
  implicit val executor: ExecutionContext = system.dispatcher
  val log: LoggingAdapter = Logging(system, getClass)
  implicit val materializer: ActorMaterializer = ActorMaterializer()
  implicit val timeout: Timeout = 25 seconds

  val dbConfig: DatabaseConfig[JdbcProfile] = DatabaseConfig.forConfig("postgres")

  val namePassAuthService = new NamePassAuthServiceImpl(dbConfig)
  val adminService = new AdminServiceImpl(dbConfig)
  val userService = new UserServiceImpl(dbConfig)

  val authRoutes = new AuthRoutes(namePassAuthService, userService)
  val adminRoutes = new AdminRoutes(adminService)

  Http().bindAndHandle(
    handler = logRequestResult("log")(authRoutes.routes ~ adminRoutes.routes ~ healthRoute),
    interface = httpInterface,
    port = httpPort)
}
