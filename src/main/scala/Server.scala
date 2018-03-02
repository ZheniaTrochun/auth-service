import akka.actor.ActorSystem
import akka.event.{Logging, LoggingAdapter}
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer
import akka.util.Timeout
import api.{AdminRoutes, AuthRoutes}
import com.typesafe.config.ConfigFactory
import services.{AdminServiceImpl, NamePassAuthServiceImpl}
import slick.basic.DatabaseConfig
import slick.jdbc.JdbcProfile

import scala.concurrent.duration._
import scala.concurrent.ExecutionContext

object Server extends App {
  implicit val system: ActorSystem = ActorSystem()
  implicit val executor: ExecutionContext = system.dispatcher
  val log: LoggingAdapter = Logging(system, getClass)
  implicit val materializer: ActorMaterializer = ActorMaterializer()
  implicit val timeout: Timeout = 25 seconds

  val dbConfig: DatabaseConfig[JdbcProfile] = DatabaseConfig.forConfig("postgres")
  val config = ConfigFactory.load()
  val httpConfig = config.getConfig("http")
  val httpInterface = httpConfig.getString("interface")
  val httpPort = httpConfig.getInt("port")

  val namePassAuthService = new NamePassAuthServiceImpl(dbConfig)
  val adminService = new AdminServiceImpl(dbConfig)

  val authRoutes = new AuthRoutes(namePassAuthService)
  val adminRoutes = new AdminRoutes(adminService)

  Http().bindAndHandle(
    handler = logRequestResult("log")(authRoutes.routes ~ adminRoutes.routes),
    interface = httpInterface,
    port = httpPort)
}
