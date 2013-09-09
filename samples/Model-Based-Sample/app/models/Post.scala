package models

import play.api.libs.iteratee._
import play.api.libs.json._
import play.api.libs.functional.syntax._
import play.modules.reactivemongo.MongoCollection
import reactivemongo.bson.BSONObjectID
import play.modules.reactivemongo.extensions._

import scala.concurrent._
import JsonTransforms._

case class Post(
  user: BSONObjectID,
  title: String,
  content: String,
  active: Boolean = true,
  _id: Option[BSONObjectID] = None
){
  val slug = title.slug
}

object Post {
  import play.modules.reactivemongo.json.BSONFormats.BSONObjectIDFormat

  val reads = Json.reads[Post]
  val writes = Json.writes[Post]
  implicit val format = Format(reads, writes)

  implicit object collection extends MongoCollection[Post] {
    // The collection will get it's name from the lower case name of the case class plus an "s" for plural, you can override it if you want.
    //override def name = "posts"
  }
}
