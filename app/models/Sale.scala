
package models

import play.api.libs.json._

case class Sale(id: Int, product_id: Int, price: Int, start: String, end: String, amount: Int)

object Sale {
  implicit val saleFormat = Json.format[Sale]
}
