import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {

  val appName         = "Model-Based-Sample"
  val appVersion      = "1.0-SNAPSHOT"

  val appDependencies = Seq(
    "org.reactivemongo" %% "play2-reactivemongo" % "0.10-SNAPSHOT",
    "org.specs2" %% "specs2" % "2.2" % "test",
    "de.flapdoodle.embed" % "de.flapdoodle.embed.mongo" % "1.36" % "test"
  )

  val main = play.Project(appName, appVersion, appDependencies).settings(
    scalacOptions ++= Seq("-unchecked", "-deprecation","-feature"),
    scalacOptions in Test ++= Seq("-Yrangepos"),
    resolvers += Resolver.file("Local repo", file("/Users/leon/.ivy2/local"))(Resolver.ivyStylePatterns)
  )

}
