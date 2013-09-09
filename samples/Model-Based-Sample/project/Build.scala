import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {

  val appName         = "Model-Based-Sample"
  val appVersion      = "1.0-SNAPSHOT"

  val appDependencies = Seq(
    "org.reactivemongo" %% "play2-reactivemongo" % "0.10-SNAPSHOT",
    "com.github.athieriot" %% "specs2-embedmongo" % "0.5.1" % "test"
  )

  val main = play.Project(appName, appVersion, appDependencies).settings(
    scalacOptions ++= Seq("-unchecked", "-deprecation","-feature"),
    resolvers += Resolver.file("Local repo", file("/Users/leon/.ivy2/local"))(Resolver.ivyStylePatterns)
  )

}
