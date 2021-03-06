package db

import com.byteslounge.slickrepo.meta.Keyed
import com.byteslounge.slickrepo.repository.Repository
import models.UserCreds
import slick.ast.BaseTypedType
import slick.jdbc.JdbcProfile
import slick.sql.{FixedSqlAction, SqlAction}

class UserCredsRepository(override val driver: JdbcProfile) extends Repository[UserCreds, Int](driver) {

  import driver.api._

  override type TableType = Creds

  override def pkType = implicitly[BaseTypedType[Int]]

  override def tableQuery = TableQuery[Creds]


  def createSchema(): FixedSqlAction[Unit, NoStream, Effect.Schema] = tableQuery.schema.create

  def dropSchema(): FixedSqlAction[Unit, NoStream, Effect.Schema] = tableQuery.schema.drop


  def findByName(name: String): SqlAction[Option[UserCreds], NoStream, Effect.Read] = {
    tableQuery.filter(_.name === name).result.headOption
  }

  def deleteByName(name: String): FixedSqlAction[Int, NoStream, Effect.Write] = {
    tableQuery.filter(_.name === name).delete
  }


  class Creds(tag: Tag) extends Table[UserCreds](tag, "UserCreds") with Keyed[Int] {
    override def id = column[Int]("ID", O.AutoInc, O.PrimaryKey)
    def name = column[String]("name", O.SqlType("VARCHAR(20)"))
    def hashedPass = column[String]("passwordHash")

    override def * = (id.?, name.?, hashedPass.?)<>((UserCreds.apply _).tupled, UserCreds.unapply)
  }
}
