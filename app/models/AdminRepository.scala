package models

import javax.inject.{ Inject, Singleton }
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile
import scala.concurrent.{ Future, ExecutionContext }


@Singleton
class AdminRepository @Inject() (dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) {
  private val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._

  private class AdminTable(tag: Tag) extends Table[Admin](tag, "admin") {


    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
    def name = column[String]("name")



    def * = (id, name) <> ((Admin.apply _).tupled, Admin.unapply)
  }

  private val adminTable = TableQuery[AdminTable]

  def create(name:String): Future[Admin] = db.run {
    (adminTable.map(p => (p.name))
      returning adminTable.map(_.id)
      into {case ((name),id) => Admin(id,name)}
      ) += (name)
  }

  def list(): Future[Seq[Admin]] = db.run {
    adminTable.result
  }

  def getByID(id: Long): Future[Seq[Admin]] = db.run {
    adminTable.filter(_.id === id).result
  }


}
