package models

import javax.inject.{ Inject, Singleton }
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile
import scala.concurrent.{ Future, ExecutionContext }


@Singleton
class PasswordRepository @Inject() (dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) {
  private val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._

  private class PasswordTable(tag: Tag) extends Table[Password](tag, "password") {


    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
    def name = column[String]("name")
    def password = column[String]("password")



    def * = (id, name, password) <> ((Password.apply _).tupled, Password.unapply)
  }

  private val passwordTable = TableQuery[PasswordTable]

  def create(name:String, password:String): Future[Password] = db.run {
    (passwordTable.map(p => (p.name, password))
      returning passwordTable.map(_.id)
      into {case ((name, password),id) => Password(id,name, password)}
      ) += (name, password)
  }



  def getByName(name: String): Future[Seq[Password]] = db.run {
    passwordTable.filter(_.name === name).result
  }


}
