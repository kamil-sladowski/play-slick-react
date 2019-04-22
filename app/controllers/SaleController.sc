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

  def getById(id: String) = Action { Ok("") }

  def add = Action { Ok("") }

  def update(id: String) = Action { Ok("") }


  def handlePost = Action.async { implicit request =>
    val name = request.body.asJson.get("name").as[String]

    salesRepo.create(name).map { sale =>
      Ok(Json.toJson(sale))
    }
  }

}

case class CreateSaleForm(name: String)
