package utils

import akka.http.scaladsl.model.{headers, _}
import akka.http.scaladsl.model.headers.RawHeader
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.Directives._
import models.json.JsonProtocol
import spray.json.JsValue
import spray.json._


import scala.concurrent.Future
import scala.util.{Failure, Success}

trait RouteUtils extends JsonProtocol {
  def completeWithFuture(f: => Future[Option[JsValue]]): Route = {
    onComplete(f) {
      case Success(res) =>
        res match {
          case Some(value) =>
            complete(HttpResponse(StatusCodes.OK, entity = HttpEntity(ContentTypes.`application/json`, res.toString)))

          case None =>
            complete(HttpResponse(StatusCodes.BadRequest, entity = HttpEntity(ContentTypes.`application/json`, "")))
        }

      case Failure(ex) =>
        complete(HttpResponse(StatusCodes.InternalServerError, entity = HttpEntity(ContentTypes.`application/json`, ex.getMessage)))
    }
  }

  def completeWithFutureHeader(key: String)(f: => Future[Option[String]]): Route = {
    onComplete(f) {
      case Success(res) =>
        res match {
          case Some(value) =>
            val rawResp = HttpResponse(StatusCodes.OK, entity = HttpEntity(ContentTypes.`application/json`, res.toString))
            complete(rawResp.withHeaders(RawHeader(key, value)))

          case None =>
            complete(HttpResponse(StatusCodes.BadRequest, entity = HttpEntity(ContentTypes.`application/json`, "")))
        }

      case Failure(ex) =>
        complete(HttpResponse(StatusCodes.InternalServerError, entity = HttpEntity(ContentTypes.`application/json`, ex.getMessage)))
    }
  }



  implicit class FutureBooleanJson(val value: Future[Boolean]) {
    def toOptionalJson: Future[Option[JsValue]] = {
      value.map(res => if (res) Some("result: true".toJson) else None)
    }
  }

//  implicit class FutureOptionalIntJson(val value: Future[Option[Int]]) {
//    def toOptionalJson: Future[Option[JsValue]] = {
//      value.map(res => if (res) Some("result: true".toJson) else None)
//    }
//  }
}
