package models

import org.specs2.mutable._
import play.api.libs.iteratee._
import play.api.libs.json._
import play.api.libs.functional.syntax._
import play.modules.reactivemongo.MongoCollection
import reactivemongo.bson.BSONObjectID
import scala.concurrent._
import com.github.athieriot.EmbedConnection

class PostsSpec extends Specification with EmbedConnection {

  implicit val ctx = new MongoContext

  import scala.concurrent.ExecutionContext.Implicits.global

  sequential

  "PostsSpec" should {

    "Create posts" in {
      import Post._

      val userId = BSONObjectID.generate

      val posts = Seq(
        Post(userId, "This is the first post", "Hello from post 1"),
        Post(userId, "This is a post with something else", "Hello from post 2"),
        Post(userId, "This post is inactive", "I'm inactive", active = false)
      )

      val inserted = Await.result(Post.collection.bulkInsert(Enumerator.enumerate(posts)), ctx.timeout)
      inserted must beEqualTo(3)
    }

    "Query for active posts" in {
      val activePosts = Await.result(Post.collection.query("active" -> true), ctx.timeout)
      activePosts.size must beEqualTo(2)
    }
  }

}
