package controllers

import play.api.mvc._
import play.api.libs.json._

import reactivemongo.bson._
import play.modules.reactivemongo.MongoCollection

import models._
import models.JsonTransforms._

import scala.concurrent.ExecutionContext.Implicits.global

class MongoResourceController[R](implicit format: Format[R], coll: MongoCollection[R]) extends ResourceRouter[String] {

  // Expose the implicit collection so that we can use it in our own methods
  def collection = coll

  def list: EssentialAction = Action { implicit request =>
    val q = coll.query()
    Async {
      q.map { result =>
        Ok(Json.toJson(result))
      }
    }
  }

  def show(id: String): EssentialAction = Action { implicit request =>
    val q = coll.findById(id)
    Async {
      q.map { optResult =>
        optResult.map { result =>
          Ok(Json.toJson(result))
        } getOrElse {
          NotFound(s"Not Found: ${coll.name}[$id]")
        }
      }
    }
  }

  def create: EssentialAction = update(BSONObjectID.generate.stringify)

  def update(id: String) = Action(parse.json) { request =>
    request.body.validate(setObjectId(id) andThen format).fold(
      invalid = e => BadRequest(JsError.toFlatJson(e)),
      valid = { result =>
        Async {
          coll.update(id, result).map { lastError =>
            Created(Json.toJson(result))
          }
        }
      }
    )
  }

  def remove(id: String): EssentialAction = Action { implicit request =>
    Async {
      coll.remove(id).map { lastError =>
        if (lastError.ok) Ok else InternalServerError(lastError.toString)
      }
    }
  }
}
