import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import api.{AdminRoutes, AuthRoutes}
import com.typesafe.config.ConfigFactory
import services.{AdminServiceImpl, NamePassAuthServiceImpl}
import slick.basic.DatabaseConfig
import slick.jdbc.JdbcProfile

object Server {
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
