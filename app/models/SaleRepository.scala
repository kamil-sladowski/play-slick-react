package models

import javax.inject.{ Inject, Singleton }
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile
import scala.concurrent.{ Future, ExecutionContext }


@Singleton
class SaleRepository @Inject() (dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) {
 private val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._

  private class SaleTable(tag: Tag) extends Table[Sale](tag, "sale") {


    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
    def product_id = column[Int]("product_id")
    def price = column[Int]("price")
    def start = column[String]("start")
    def end = column[String]("end")
    def amount = column[Int]("amount")



    def * = (id, product_id, price, start, end, amount) <> ((Sale.apply _).tupled, Sale.unapply)
  }

  private val saleTable = TableQuery[SaleTable]

  def create(product_id: Int, price: Int, start:String, end: String, amount: Int): Future[Sale] = db.run {
    (saleTable.map(p => (p.product_id, p.price, p.start, p.end, p.amount))
      returning saleTable.map(_.id)
      into {case ((product_id, price, start, end, amount),id) => Sale(id,product_id, price, start, end, amount)}
      ) += (product_id, price, start, end, amount)
  }

  def list(): Future[Seq[Sale]] = db.run {
    saleTable.result
  }

  def getByID(id: Long): Future[Seq[Sale]] = db.run {
    saleTable.filter(_.id === id).result
  }

}
