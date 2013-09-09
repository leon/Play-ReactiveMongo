package models

import org.specs2.specification.Around
import org.specs2.execute.{AsResult, Result}
import play.api.test._
import play.api.test.Helpers._
import scala.concurrent.duration._
import play.api.Configuration
import com.typesafe.config.Config

class MongoContext(val app: FakeApplication = new FakeApplication(
  configuration = new Config() .new java.io.File("conf/test.conf"),
  additionalPlugins = Seq("play.modules.reactivemongo.ReactiveMongoPlugin")
)) extends Around {

  override def around[T: AsResult](t: => T): Result = Helpers.running(app)(AsResult(t))

  val timeout = 5.seconds
}
