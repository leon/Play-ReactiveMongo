package models

import play.api.libs.iteratee._
import play.api.libs.json._
import play.api.libs.functional.syntax._
import play.modules.reactivemongo.MongoCollection
import reactivemongo.bson.BSONObjectID
import play.modules.reactivemongo.extensions._

import scala.concurrent._

case class User(
  email: String,
  firstName: String,
  lastName: String,
  active: Boolean = true,
  _id: Option[BSONObjectID] = None
){
  val name = s"$firstName $lastName"
}

object User {
  import play.modules.reactivemongo.json.BSONFormats.BSONObjectIDFormat

  implicit val format = Json.format[User]

  implicit object collection extends MongoCollection[User]
}
