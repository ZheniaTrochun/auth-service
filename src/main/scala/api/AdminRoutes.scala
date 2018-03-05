package api

import akka.http.scaladsl.server.Directives._
import config.AppConfig
import services.AdminService

class AdminRoutes(val adminService: AdminService) extends AppConfig {
  val routes =
    pathPrefix("admin") {
      pathPrefix("db") {
        path("create-tables") {
          adminService.createTable()
          complete("Created")
        } ~
        path("drop-tables") {
          adminService.dropTable()
          complete("Dropped")
        }
      } ~
      pathPrefix("config"){
        (path("create-dummy") & get) {
          config.createDummyConfig()
          complete("Ok")
        } ~
          (path("update") & get) {
            config.update()
            complete("Ok")
          }
      }
    }
}
