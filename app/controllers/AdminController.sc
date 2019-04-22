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

  def getById(id: String) = Action { Ok("") }

  def add = Action { Ok("") }

  def login = Action { Ok("") }

  def register = Action { Ok("") }

  def update(id: String) = Action { Ok("") }


  def handlePost = Action.async { implicit request =>
    val name = request.body.asJson.get("name").as[String]

    adminsRepo.create(name).map { admin =>
      Ok(Json.toJson(admin))
    }
  }

}

case class CreateAdminForm(name: String)
