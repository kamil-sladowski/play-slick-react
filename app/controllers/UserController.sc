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

  def getById(id: String) = Action { Ok("") }

  def register = Action { Ok("") }
  def login = Action { Ok("") }

  def update(id: String) = Action { Ok("") }


  def handlePost = Action.async { implicit request =>
    val name = request.body.asJson.get("name").as[String]

    usersRepo.create(name).map { user =>
      Ok(Json.toJson(user))
    }
  }

}

case class CreateUserForm(name: String)
