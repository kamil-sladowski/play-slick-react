
package models

import play.api.libs.json._

case class Admin(id: Long, name: String)

object Admin {
  implicit val adminFormat = Json.format[Admin]
}
