package models

import javax.inject.{ Inject, Singleton }
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile
import scala.concurrent.{ Future, ExecutionContext }

@Singleton
class UserRepository @Inject() (dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) {
  private val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._

  private class UserTable(tag: Tag) extends Table[User](tag, "user") {


    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
    def name = column[String]("name")
    def email = column[String]("email")
    def phone = column[Int]("phone")
    def postal = column[String]("postal")
    def country = column[String]("country")
    def city = column[String]("city")
    def street = column[String]("street")
    def home_number = column[Int]("home_number")
    def flat_number = column[Int]("flat_number")

    def * = (id, name, email, phone, postal, country,
      city, street, home_number, flat_number) <> ((User.apply _).tupled, User.unapply)
  }

  private val userTable = TableQuery[UserTable]

  def create(name:String, email:String, phone:Int, postal:String, country:String, city:String, street:String, home_number:Int, flat_number:Int): Future[User] = db.run {
    (userTable.map(p => (p.name, p.email, p.phone, p.postal, p.country, p.city, p.street, p.home_number, p.flat_number))
      returning userTable.map(_.id)
      into {case ((name, email, phone, postal, country,
    city, street, home_number, flat_number),id) => User(id, name, email, phone, postal, country,
      city, street, home_number, flat_number)}
      ) += (name, email, phone, postal, country, city, street, home_number, flat_number)
  }

  def list(): Future[Seq[User]] = db.run {
    userTable.result
  }


}