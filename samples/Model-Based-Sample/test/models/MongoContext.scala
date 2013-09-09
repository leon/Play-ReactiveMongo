package models

import org.specs2.mutable.Around
import org.specs2.execute.{AsResult, Result}
import org.specs2.specification.Scope
import play.api.test._
import play.api.test.FakeApplication
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext

trait MongoContext extends Around with Scope {

  implicit lazy val app: FakeApplication = new FakeApplication(
    additionalConfiguration = Map(
      ("mongodb.uri" -> "mongodb://localhost:27017/reactivetest")
    )
  )
  implicit def timeout = 5.seconds
  implicit def ec = ExecutionContext.Implicits.global

  override def around[T: AsResult](t: => T): Result = {
    Helpers.running(app)(AsResult.effectively(t))
  }

}
