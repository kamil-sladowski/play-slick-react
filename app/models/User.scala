package models

import play.api.libs.json._

case class User(id: Long, name: String, email: String, phone: Int, postal: String, country: String,
                city: String, street: String,home_number: Int,flat_number: Int)

object User {
  implicit val userFormat = Json.format[User]
}
