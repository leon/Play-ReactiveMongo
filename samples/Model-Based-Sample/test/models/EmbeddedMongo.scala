package models

import de.flapdoodle.embed.mongo._
import de.flapdoodle.embed.mongo.config.{Net, MongodConfigBuilder}
import de.flapdoodle.embed.mongo.distribution.Version
import de.flapdoodle.embed.process.runtime.Network
import org.specs2.specification.BeforeAfterExample

trait EmbeddedMongo extends BeforeAfterExample {

  lazy val runtime: MongodStarter = MongodStarter.getDefaultInstance
  lazy val mongodExe: MongodExecutable = runtime.prepare(new MongodConfigBuilder().version(Version.Main.PRODUCTION).net(new Net(27028, false)).build())
  lazy val mongod: MongodProcess = mongodExe.start()

  def before {
    mongod
  }

  def after {
    mongod.stop()
  }
}
