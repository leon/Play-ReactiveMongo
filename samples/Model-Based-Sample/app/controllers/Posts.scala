package controllers

import play.api.mvc._
import play.api.libs.json._
import reactivemongo.bson.BSONObjectID

import models._
import models.JsonTransforms._

import scala.concurrent.ExecutionContext.Implicits.global

object Posts extends MongoResourceController[Post] {

  /**
   * Customized action that only shows active posts
   * @return active posts
   */
  def listActive(): EssentialAction = Action { implicit request =>
    val q = collection.query("active" -> true)
    Async {
      q.map { result =>
        Ok(Json.toJson(result))
      }
    }
  }
}
