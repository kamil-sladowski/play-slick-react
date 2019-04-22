package models

import play.api.libs.json._

case class Product(id: Long, name: String, price:Int, amount:Int, description: String, category: Int)

object Product {
  implicit val productFormat = Json.format[Product]
}
