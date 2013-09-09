package models

import play.api.libs.json._
import play.api.libs.functional.syntax._
import reactivemongo.bson._
import play.modules.reactivemongo.json.BSONFormats._
import play.modules.reactivemongo.extensions._

object JsonTransforms {

  /**
   * add slug f
   */
  val nameToSlug = __.json.update(
    (__ \ 'slug).json.copyFrom(
      (__ \ 'name).read[String] map { str => JsString(str.slug) }
    )
  )

  def setObjectId(id: String) = __.json.update(
    (__ \ '_id \ '$oid).json.put(JsString(id))
  )

  /**
   * Convert from id: ... to _id: {$oid: ...} and set id explicitly
   * @param id the id to set, usually from a path or query string parameter
   */
  /*def setObjectId(id: String) = __.json.update(
    (__ \ '_id \ '$oid).json.put(JsString(id))
  ) andThen (__ \ 'id).json.prune*/

  /**
   * Convert from id: ... to _id: {$oid: ...}
   */
  val idToObjectId = __.json.update(
    (__ \ '_id \ '$oid).json.copyFrom(
      (__ \ 'id).json.pick
    )
  ) andThen (__ \ 'id).json.prune

  /*val objectIdToStringFormat = Format[BSONObjectID](
    (__ \ "id").read[String] map { str => BSONObjectID(str) },
    Writes[BSONObjectID] { id => JsString(id.stringify) }
  )*/

  /*val addNormalizedId = __.json.update(
    (__ \ 'id).json.copyFrom(
      (__ \ '_id).read[BSONObjectID] map { id => JsString(id.stringify) }
    )
  )*/

  val addNormalizedId = (__ \ 'id).json.copyFrom((__ \ '_id \ '$oid).json.pick)
  val addSlug = (__ \ 'slug).json.copyFrom((__ \ 'name).read[String] map { str => JsString(str.slug) })
}
