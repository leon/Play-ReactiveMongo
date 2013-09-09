package controllers

import play.api.mvc._
import play.core.Router
import scala.runtime.AbstractPartialFunction

trait ResourceController[T] extends Controller {
  def list: EssentialAction
  def create: EssentialAction
  def show(id: T): EssentialAction
  def update(id: T): EssentialAction
  def remove(id: T): EssentialAction
}

trait Routes {
  def routes: PartialFunction[RequestHeader, Handler]
  def documentation: Seq[(String, String, String)]
  def setPrefix(prefix: String)
  def prefix: String
}

abstract class ResourceRouter[T](implicit idBindable: PathBindable[T]) extends Router.Routes with ResourceController[T] {
  private var path: String = ""
  def setPrefix(prefix: String) {
    path = prefix
  }
  def prefix = path
  def documentation = Nil
  def routes = new AbstractPartialFunction[RequestHeader, Handler] {

    private val MaybeSlash = "/?".r
    private val Id = "/([^/]+)/?".r

    def withId(id: String, action: T => EssentialAction) = idBindable.bind("id", id).fold(badRequest, action)

    override def applyOrElse[A <: RequestHeader, B >: Handler](rh: A, default: A => B) = {
      if (rh.path.startsWith(path)) {
        (rh.method, rh.path.drop(path.length)) match {
          case ("GET", MaybeSlash()) => list
          case ("POST", MaybeSlash()) => create
          case ("GET", Id(id)) => withId(id, show)
          case ("PUT", Id(id)) => withId(id, update)
          case ("PATCH", Id(id)) => withId(id, update)
          case ("DELETE", Id(id)) => withId(id, remove)
          case _ => default(rh)
        }
      } else {
        default(rh)
      }
    }

    def isDefinedAt(rh: RequestHeader): Boolean = {
      if (rh.path.startsWith(path)) {
        (rh.method, rh.path.drop(path.length)) match {
          case ("GET", MaybeSlash()) => true
          case ("POST", MaybeSlash()) => true
          case ("GET", Id(id)) => true
          case ("PUT", Id(id)) => true
          case ("PATCH", Id(id)) => true
          case ("DELETE", Id(id)) => true
          case _ => false
        }
      } else {
        false
      }
    }
  }
}
