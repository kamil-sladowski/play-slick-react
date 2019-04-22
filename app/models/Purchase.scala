
package models

import play.api.libs.json._

case class Purchase(id: Long, user_id: Int, product_id:Int, date:String, amount: Int)

object Purchase {
  implicit val purchaseFormat = Json.format[Purchase]
}
