package models

import play.api.libs.json._

case class Password(id: Long, name: String, password:String)

object Passwords {
  implicit val passwordsFormat = Json.format[Password]
}
