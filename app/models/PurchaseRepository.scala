package models

import javax.inject.{ Inject, Singleton }
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile
import models.ProductRepository
import scala.concurrent.{ Future, ExecutionContext }


@Singleton
class PurchaseRepository @Inject() (dbConfigProvider: DatabaseConfigProvider,
                                    productRepository: ProductRepository)(implicit ec: ExecutionContext) {
  // We want the JdbcProfile for this provider
  private val dbConfig = dbConfigProvider.get[JdbcProfile]

  // These imports are important, the first one brings db into scope, which will let you do the actual db operations.
  // The second one brings the Slick DSL into scope, which lets you define the table and other queries.
  import dbConfig._
  import profile.api._


  import productRepository.ProductTable

  private class PurchaseTable(tag: Tag) extends Table[Purchase](tag, "purchase") {

    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
    def user_id = column[Int]("user_id")
    def product_id = column[Int]("product_id")
    def date = column[String]("date")
    def amount = column[Int]("amount")
//    def product_fk = foreignKey("cat_fk",product, productTable)(_.id)


    def * = (id, user_id, product_id, date, amount) <> ((Purchase.apply _).tupled, Purchase.unapply)
  }

//  import productRepository.ProductTable

  private val purchaseTable = TableQuery[PurchaseTable]
//  private val productTable = TableQuery[ProductTable]


  def create(user_id: Int, product_id: Int, date: String, amount: Int): Future[Purchase] = db.run {
    (purchaseTable.map(p => (p.user_id, p.product_id, p.date, p.amount))
      returning purchaseTable.map(_.id)
      into {case ((user_id, product_id, date, amount),id) => Purchase(id,user_id, product_id, date, amount)}
      ) += (user_id, product_id, date, amount)
  }

  def list(): Future[Seq[Purchase]] = db.run {
    purchaseTable.result
  }

//  def getByProduct(product_id: Int): Future[Seq[Purchase]] = db.run {
//    purchaseTable.filter(_.product === product_id).result
//  }
//
//  def getByProducts(product_ids: List[Int]): Future[Seq[Purchase]] = db.run {
//    purchaseTable.filter(_.product inSet product_ids).result
//  }


}
