package models

import com.byteslounge.slickrepo.meta.Entity

case class UserCreds(id: Option[Int] = None,
                     name: Option[String],
                     passwordHash: Option[String])
  extends Entity[UserCreds, Int] {

  override def withId(id: Int) = this.copy(id = Some(id))
}

case class UserRegisterRequest(name: String, password: String, email: String) {
  def toUserDto: UserDto = UserDto(name, password)
  def toUserCreds(hashedPass: String): UserCreds = UserCreds(name = Some(this.name), passwordHash = Some(hashedPass))
}

case class UserSignInRequest(name: String, password: String)

case class UserDto(name: String, email: String)

