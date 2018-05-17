package security

import java.lang.Exception

import authentikat.jwt.{JsonWebToken, JwtClaimsSet, JwtHeader}
import config.AppConfig
import io.jsonwebtoken.{Jwts, SignatureAlgorithm}

trait JwtUtils extends AppConfig {

  private val algo = config.getString("api.security.algo")
  private val secret = config.getString("api.security.secret")

  def createToken(name: String): String = {
//    JsonWebToken.apply(JwtHeader(algo), JwtClaimsSet(Map("user" -> name)), secret)
    Jwts.builder().setSubject(name).signWith(SignatureAlgorithm.HS256, secret).compact()
  }

  def isTokenValid(jwt: String): Boolean = {
    try {
      Jwts.parser.setSigningKey(secret).parseClaimsJws(jwt)
      true
    } catch {
      case _ => false
    }
//    JsonWebToken.validate(jwt, secret)
  }

  def decodeUser(jwt: String): Option[String] = {
//    jwt match {
//      case JsonWebToken(_, claimsSet, _) =>
//        Some(claimsSet.asSimpleMap.get("user"))
//
//      case _ =>
//        None
//    }
    Option(Jwts.parser().setSigningKey(secret).parseClaimsJws(jwt).getBody.getSubject)
  }
}
