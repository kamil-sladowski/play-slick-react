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
class ProductRepository @Inject() (dbConfigProvider: DatabaseConfigProvider, categoryRepository: CategoryRepository)(implicit ec: ExecutionContext) {
  // We want the JdbcProfile for this provider
  private val dbConfig = dbConfigProvider.get[JdbcProfile]

  // These imports are important, the first one brings db into scope, which will let you do the actual db operations.
  // The second one brings the Slick DSL into scope, which lets you define the table and other queries.
  import dbConfig._
  import profile.api._

  /**
   * Here we define the table. It will have a name of people
   */
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


    /**
     * This is the tables default "projection".
     *
     * It defines how the columns are converted to and from the Person object.
     *
     * In this case, we are simply passing the id, name and page parameters to the Person case classes
     * apply and unapply methods.
     */
    def * = (id, name, price, amount, description, category) <> ((Product.apply _).tupled, Product.unapply)
  }

  /**
   * The starting point for all queries on the people table.
   */

  import categoryRepository.CategoryTable

  private val productTable = TableQuery[ProductTable]
  private val categoryTable = TableQuery[CategoryTable]


  /**
   * Create a person with the given name and age.
   *
   * This is an asynchronous operation, it will return a future of the created person, which can be used to obtain the
   * id for that person.
   */
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

  /**
   * List all the people in the database.
   */
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
