package models.json

import models.{UserCreds, UserDto, UserRegisterRequest, UserSignInRequest}
import spray.json.DefaultJsonProtocol._

trait JsonProtocol {
  implicit val userCredsFormat = jsonFormat4(UserCreds.apply)
  implicit val regReqFormat = jsonFormat3(UserRegisterRequest.apply)
  implicit val loginReqFormat = jsonFormat2(UserSignInRequest.apply)
  implicit val userDtoFormat = jsonFormat2(UserDto.apply)
}
