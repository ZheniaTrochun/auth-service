package api

import akka.http.scaladsl.server.Directives._
import services.AdminService

class AdminRoutes(val adminService: AdminService) {
  val routes = pathPrefix("admin") {
    path("create-tables") {
      adminService.createTable()
      complete("Created")
    } ~
    path("drop-tables") {
      adminService.dropTable()
      complete("Dropped")
    }
  }
}
