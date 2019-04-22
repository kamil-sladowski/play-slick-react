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

  def getById(id: String) = Action { Ok("") }

  def add = Action { Ok("") }

  def update(id: String) = Action { Ok("") }


  def handlePost = Action.async { implicit request =>
    val name = request.body.asJson.get("name").as[String]

    purchasesRepo.create(name).map { purchase =>
      Ok(Json.toJson(purchase))
    }
  }

}

case class CreatePurchaseForm(name: String)
