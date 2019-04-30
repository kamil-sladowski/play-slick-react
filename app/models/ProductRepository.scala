package models

import javax.inject.{ Inject, Singleton }
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile
import models.CategoryRepository
import scala.concurrent.{ Future, ExecutionContext }

/**
 * A repository for people.
 *
 * @param dbConfigProvider The Play db config provider. Play will inject this for you.
 */
@Singleton
class ProductRepository @Inject() (dbConfigProvider: DatabaseConfigProvider,
                                   categoryRepository: CategoryRepository)(implicit ec: ExecutionContext) {

  private val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._


  import categoryRepository.CategoryTable

  private class ProductTable(tag: Tag) extends Table[Product](tag, "product") {

    /** The ID column, which is the primary key, and auto incremented */
    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
    def name = column[String]("name")
    def description = column[String]("description")
    def price = column[Int]("price")
//    def photo = column[File]("photo")
    def amount = column[Int]("amount")
    def category = column[Int]("category_id")
    def category_fk = foreignKey("cat_fk",category, categoryTable)(_.id)



    def * = (id, name, price, amount, description, category) <> ((Product.apply _).tupled, Product.unapply)
  }


  import categoryRepository.CategoryTable

  private val productTable = TableQuery[ProductTable]
  private val categoryTable = TableQuery[CategoryTable]


  def create(name: String, price: Integer, amount:Integer, description: String, category: Int): Future[Product] = db.run {
    // We create a projection of just the name and age columns, since we're not inserting a value for the id column
    (productTable.map(p => (p.name, p.price, p.amount, p.description, p.category))
      // Now define it to return the id, because we want to know what id was generated for the person
      returning productTable.map(_.id)
      // And we define a transformation for the returned value, which combines our original parameters with the
      // returned id
      into {case ((name, price, amount, description, category),id) => Product(id,name, price, amount, description, category)}
    // And finally, insert the person into the database
    ) += (name, price, amount, description, category)
  }


  def list(): Future[Seq[Product]] = db.run {
    productTable.result
  }

  def getByCategory(category_id: Int): Future[Seq[Product]] = db.run {
    productTable.filter(_.category === category_id).result
  }

  def getByCategories(category_ids: List[Int]): Future[Seq[Product]] = db.run {
    productTable.filter(_.category inSet category_ids).result
  }


}
