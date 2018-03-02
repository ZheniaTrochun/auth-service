package models

import com.byteslounge.slickrepo.meta.Entity

case class UserCreds(val id: Option[Int] = None,
                     val name: Option[String],
                     val passwordHash: Option[String],
                     val token: Option[String] = None)
  extends Entity[UserCreds, Int] {

  override def withId(id: Int) = this.copy(id = Some(id))
}

case class UserRegisterRequest(val name: String, val password: String, val email: String) {
  def toUserDto: UserDto = UserDto(name, password)
  def toUserCreds(hashedPass: String): UserCreds = UserCreds(name = Some(this.name), passwordHash = Some(hashedPass))
}

case class UserSignInRequest(val name: String, val password: String)

case class UserDto(val name: String, val email: String)

