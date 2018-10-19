import ammonite.ops.home
import ammonite.ops.ls
import javax.management.openmbean.OpenType
import mill._
import mill.define.Module
import mill.scalalib._
import mill.scalalib.publish._

object publishM2 extends ScalaModule with PublishModule {

  def scalaVersion = "2.12.7"

  def publishVersion = "0.0.1"

  object Deps {
    val millMain = ivy"com.lihaoyi::mill-main:0.2.8"
    val millScalalib = ivy"com.lihaoyi::mill-scalalib:0.2.8"
    val scalaTest = ivy"org.scalatest::scalatest:3.0.1"
  }

  def javacOptions = Seq("-source", "1.8", "-target", "1.8")

  def pomSettings = T {
    PomSettings(
      description = "Mill module to publish artifacts in local Maven repositories",
      organization = "de.tototec",
      url = "https://github.com/lefou/mill-publishM2",
      licenses = Seq(License.`Apache-2.0`),
      versionControl = VersionControl.github("lefou", "mill-publishM2"),
      developers = Seq(Developer("lefou", "Tobias Roeser", "https.//github.com/lefou"))
    )
  }

  override def artifactName = "de.tobiasroeser.mill.publishM2"

  def ivyDeps = Agg(
    Deps.millMain,
    Deps.millScalalib
  )

  object test extends Tests {

    override def ivyDeps = Agg(
      Deps.scalaTest
    )
    def testFrameworks = Seq("org.scalatest.tools.Framework")

  }

}
