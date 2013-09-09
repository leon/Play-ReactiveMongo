package models

import org.specs2.mutable._
import play.api.libs.iteratee._
import reactivemongo.bson.BSONObjectID
import scala.concurrent._
import java.util.concurrent.TimeUnit
import play.api.test.WithApplication

class PostsSpec extends Specification {

  sequential

  "PostsSpec" should {

    "Create posts" in new MongoContext {
      import Post._

      val userId = BSONObjectID.generate

      val posts = Seq(
        Post(userId, "This is the first post", "Hello from post 1"),
        Post(userId, "This is a post with something else", "Hello from post 2"),
        Post(userId, "This post is inactive", "I'm inactive", active = false)
      )

      val inserted = Await.result(Post.collection.bulkInsert(Enumerator.enumerate(posts)), timeout)
      inserted must beEqualTo(3)
    }

    "Query for active posts" in new MongoContext {

      val activePosts = Await.result(Post.collection.query("active" -> true), timeout)
      activePosts.size must beEqualTo(2)
    }
  }

}
