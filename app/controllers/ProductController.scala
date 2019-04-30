package controllers

import javax.inject._
import models._
import play.api.data.Form
import play.api.data.Forms._
import play.api.data.validation.Constraints._
import play.api.i18n._
import play.api.libs.json.Json
import play.api.mvc._

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}

class ProductController @Inject()(productsRepo: ProductRepository,
                                  categoryRepo: CategoryRepository,
                                  cc: MessagesControllerComponents
                                )(implicit ec: ExecutionContext)
  extends MessagesAbstractController(cc) {


  val productForm: Form[CreateProductForm] = Form {
    mapping(
      "name" -> nonEmptyText,
      "price" -> number,
      "amount" -> number,
      "description" -> nonEmptyText,
      "category_id" -> number,
    )(CreateProductForm.apply)(CreateProductForm.unapply)
  }


  def index = Action.async { implicit request =>
    val categories = categoryRepo.list()
    categories.map(cat => Ok(views.html.index(productForm,cat)))

      /*
      .onComplete{
      case Success(categories) => Ok(views.html.index(productForm,categories))
      case Failure(t) => print("")
    }*/
  }



//  def add = Action.async { implicit request =>
//    Ok(views.html.addproduct())
//  }


  def add = Action.async { implicit request =>
    // Bind the form first, then fold the result, passing a function to handle errors, and a function to handle succes.
    var a:Seq[Category] = Seq[Category]()
    val categories = categoryRepo.list().onComplete{
      case Success(cat) => a= cat
      case Failure(_) => print("fail")
    }

    productForm.bindFromRequest.fold(
      // The error function. We return the index page with the error form, which will render the errors.
      // We also wrap the result in a successful future, since this action is synchronous, but we're required to return
      // a future because the person creation function returns a future.
      errorForm => {
        Future.successful(
            Ok(views.html.index(errorForm,a))
          )
      },
      // There were no errors in the from, so create the person.
      product => {
        productsRepo.create(product.name, product.price, product.amount, product.description, product.category_id).map { _ =>
          // If successful, we simply redirect to the index page.
          Redirect(routes.ProductController.index).flashing("success" -> "product.created")
        }
      }
    )
  }



  def getAll = Action.async { implicit request =>
    productsRepo.list().map { products =>
      Ok(Json.toJson(products))
    }
  }

  def getByCategory(id: Integer) = Action.async { implicit  request =>
    productsRepo.getByCategory(id).map { products =>
      Ok(Json.toJson(products))
    }
  }

  def getByCategories = Action.async { implicit  request =>
    val categories: List[Int] = List(1,2,3)

    productsRepo.getByCategories(categories).map { products =>
      Ok(Json.toJson(products))
    }
  }

  def handlePost = Action.async { implicit request =>
    val name = request.body.asJson.get("name").as[String]
    val desc = request.body.asJson.get("description").as[String]
    val pric = request.body.asJson.get("price").as[Int]
    val amoun = request.body.asJson.get("amount").as[Int]
    val categ = 1

    productsRepo.create(name, pric, amoun, desc, categ).map { product =>
      Ok(Json.toJson(product))
    }
  }

  def getById(id: Integer) = Action { Ok("") }

  def update(id: Integer) = Action { Ok("") }
}

case class CreateProductForm(name: String, price:Int, amount:Int, description: String, category_id: Int)





class CategoryController @Inject()(categoriesRepo: CategoryRepository,
                                   cc: MessagesControllerComponents
                                  )(implicit ec: ExecutionContext)
  extends MessagesAbstractController(cc) {


  val categorieForm: Form[CreateCategoryForm] = Form {
    mapping(
      "name" -> nonEmptyText,
      "description" -> nonEmptyText,
    )(CreateCategoryForm.apply)(CreateCategoryForm.unapply)
  }


  def getAll = Action { Ok("") }

  def getById(id: Integer) = Action { Ok("") }

  def add = Action { Ok("") }

  def update(id: Integer) = Action { Ok("") }


  def handlePost = Action.async { implicit request =>
    val name = request.body.asJson.get("name").as[String]
    val description = request.body.asJson.get("description").as[String]

    categoriesRepo.create(name, description ).map { categorie =>
      Ok(Json.toJson(categorie))
    }
  }

}

case class CreateCategoryForm(name: String, description: String)


  class PurchaseController @Inject()(purchasesRepo: PurchaseRepository,
                                     cc: MessagesControllerComponents
                                    )(implicit ec: ExecutionContext)
    extends MessagesAbstractController(cc) {


    val purchaseForm: Form[CreatePurchaseForm] = Form {
      mapping(
        "name" -> nonEmptyText,
        "user_id" -> number,
        "product_id" -> number,
        "date" -> nonEmptyText,
        "amount" -> number,
      )(CreatePurchaseForm.apply)(CreatePurchaseForm.unapply)
    }


    def getAll = Action { Ok("") }

    def getById(id: Integer) = Action { Ok("") }

    def add = Action { Ok("") }

    def update(id: Integer) = Action { Ok("") }


    def handlePost = Action.async { implicit request =>
      val user_id = request.body.asJson.get("user_id").as[Int]
      val product_id = request.body.asJson.get("product_id").as[Int]
      val date = request.body.asJson.get("date").as[String]
      val amount = request.body.asJson.get("amount").as[Int]


      purchasesRepo.create(user_id, product_id, date, amount).map { purchase =>
        Ok(Json.toJson(purchase))
      }
    }

  }

  case class CreatePurchaseForm(name: String, user_id: Int, product_id: Int, date: String, amount: Int)


class SaleController @Inject()(salesRepo: SaleRepository,
                               cc: MessagesControllerComponents
                              )(implicit ec: ExecutionContext)
  extends MessagesAbstractController(cc) {


  val saleForm: Form[CreateSaleForm] = Form {
    mapping(
      "name" -> nonEmptyText,
      "product_id" -> number,
      "price" -> number,
      "start" -> nonEmptyText,
      "end" -> nonEmptyText,
      "amount" -> number,
    )(CreateSaleForm.apply)(CreateSaleForm.unapply)
  }


  def getAll = Action { Ok("") }

  def getById(id: Integer) = Action { Ok("") }

  def add = Action { Ok("") }

  def update(id: Integer) = Action { Ok("") }


  def handlePost = Action.async { implicit request =>
    val product_id = request.body.asJson.get("product_id").as[Int]
    val price = request.body.asJson.get("price").as[Int]
    val start = request.body.asJson.get("start").as[String]
    val end = request.body.asJson.get("end").as[String]
    val amount = request.body.asJson.get("amount").as[Int]

    salesRepo.create(product_id, price, start, end, amount).map { sale =>
      Ok(Json.toJson(sale))
    }
  }

}

case class CreateSaleForm(name: String, product_id: Int, price: Int, start: String, end: String, amount: Int)





class AdminController @Inject()(adminsRepo: AdminRepository,
                                cc: MessagesControllerComponents
                               )(implicit ec: ExecutionContext)
  extends MessagesAbstractController(cc) {


  val adminForm: Form[CreateAdminForm] = Form {
    mapping(
      "name" -> nonEmptyText,
    )(CreateAdminForm.apply)(CreateAdminForm.unapply)
  }


  def getAll = Action { Ok("") }

  def getById(id: Integer) = Action { Ok("") }

  def add = Action { Ok("") }

  def login = Action { Ok("") }

  def register = Action { Ok("") }

  def update(id: Integer) = Action { Ok("") }


  def handlePost = Action.async { implicit request =>
    val name = request.body.asJson.get("name").as[String]

    adminsRepo.create(name).map { admin =>
      Ok(Json.toJson(admin))
    }
  }

}

case class CreateAdminForm(name: String)





class UserController @Inject()(usersRepo: UserRepository,
                               cc: MessagesControllerComponents
                              )(implicit ec: ExecutionContext)
  extends MessagesAbstractController(cc) {


  val userForm: Form[CreateUserForm] = Form {
    mapping(
      "name" -> nonEmptyText,
      "email" -> nonEmptyText,
      "phone" -> number,
      "postal" -> nonEmptyText,
      "country" -> nonEmptyText,
      "city" -> nonEmptyText,
      "street" -> nonEmptyText,
      "home_number" -> number,
      "flat_number" -> number,
    )(CreateUserForm.apply)(CreateUserForm.unapply)
  }


  def getAll = Action { Ok("") }

  def getById(id: Integer) = Action { Ok("") }

  def register = Action { Ok("") }
  def login = Action { Ok("") }

  def update(id: Integer) = Action { Ok("") }


  def handlePost = Action.async { implicit request =>
    val name = request.body.asJson.get("name").as[String]
    val email = request.body.asJson.get("email").as[String]
    val phone = request.body.asJson.get("phone").as[Int]
    val postal = request.body.asJson.get("postal").as[String]
    val country = request.body.asJson.get("country").as[String]
    val city = request.body.asJson.get("city").as[String]
    val street = request.body.asJson.get("street").as[String]
    val home_number = request.body.asJson.get("home_number").as[Int]
    val flat_number = request.body.asJson.get("flat_number").as[Int]


    usersRepo.create(name, email, phone, postal, country, city, street, home_number,flat_number).map { user =>
      Ok(Json.toJson(user))
    }
  }

}

case class CreateUserForm(name: String, email: String, phone: Int, postal: String,
                          country: String, city: String, street: String, home_number: Int, flat_number: Int)
