package security

import authentikat.jwt.{JsonWebToken, JwtClaimsSet, JwtHeader}
import config.AppConfig

trait JwtUtils extends AppConfig {

  private val algo = config.getString("api.security.algo")
  private val secret = config.getString("api.security.secret")

  def createToken(name: String): String = {
    JsonWebToken.apply(JwtHeader(algo), JwtClaimsSet(Map("user" -> name)), secret)
  }

  def isTokenValid(jwt: String): Boolean = {
    JsonWebToken.validate(jwt, secret)
  }

  def decodeUser(jwt: String): Option[String] = {
    jwt match {
      case JsonWebToken(_, claimsSet, _) =>
        Some(claimsSet.asSimpleMap.get("user"))

      case _ =>
        None
    }
  }
}
