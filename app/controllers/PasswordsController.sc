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

class PasswordController @Inject()(categoriesRepo: PasswordRepository,
                                   cc: MessagesControllerComponents
                                  )(implicit ec: ExecutionContext)
  extends MessagesAbstractController(cc) {


  val categorieForm: Form[CreatePasswordForm] = Form {
    mapping(
      "name" -> nonEmptyText,
      "password" -> nonEmptyText,
    )(CreatePasswordForm.apply)(CreatePasswordForm.unapply)
  }


  def getByName(id: String) = Action { Ok("") }

  def add = Action { Ok("") }

  def update(id: String) = Action { Ok("") }


  def handlePost = Action.async { implicit request =>
    val name = request.body.asJson.get("name").as[String]

    categoriesRepo.create(name).map { categorie =>
      Ok(Json.toJson(categorie))
    }
  }

}

case class CreatePasswordForm(name: String, password:String)
