package api

import models.json.JsonProtocol
import utils.RouteUtils
import akka.http.scaladsl.server.Directives._
import models.{UserRegisterRequest, UserSignInRequest}
import services.NamePassAuthService
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._

import scala.language.postfixOps

class AuthRoutes(val namePathAuthService: NamePassAuthService) extends RouteUtils with JsonProtocol{
  val routes =
    pathPrefix("auth") {
      (path("sign-up") & post) {
        entity(as[UserRegisterRequest]) { req: UserRegisterRequest =>
          completeWithFuture {
            namePathAuthService.register(req.name, req.password, req.email) toOptionalJson
          }
        }
      } ~
      (path("sign-in") & post) {
        entity(as[UserSignInRequest]) { req: UserSignInRequest =>
          completeWithFutureHeader("Authentication") {
            namePathAuthService.login(req.name, req.password)
          }
        }
      }
    }
}
