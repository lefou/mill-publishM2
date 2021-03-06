import ammonite.ops.home
import ammonite.ops.ls
import javax.management.openmbean.OpenType
import mill._
import mill.define.Module
import mill.scalalib._
import mill.scalalib.publish._

/** Release to Maven Central. */
def _release(
  sonatypeCreds: String,
  release: Boolean = true
) = T.command {
  publishM2.test.test()()
  publishM2.publish(sonatypeCreds = sonatypeCreds, release = release)()
}

val millVersion = "0.6.0"

object publishM2 extends ScalaModule with PublishModule {

  def scalaVersion = "2.12.8"

  def publishVersion = "0.1.3"

  object Deps {
    val lambdaTest = ivy"de.tototec:de.tobiasroeser.lambdatest:0.7.0"
    val millMain = ivy"com.lihaoyi::mill-main:${millVersion}"
    val millScalalib = ivy"com.lihaoyi::mill-scalalib:${millVersion}"
    val osLib = ivy"com.lihaoyi::os-lib:0.6.3"
    val scalaTest = ivy"org.scalatest::scalatest:3.0.4"
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

  def compileIvyDeps = Agg(
    Deps.millMain,
    Deps.millScalalib,
    Deps.osLib
  )

  object test extends Tests {
    override def ivyDeps = T{
      publishM2.compileIvyDeps() ++ Agg(
        Deps.scalaTest,
        Deps.lambdaTest
      )
    }
    def testFrameworks = Seq("org.scalatest.tools.Framework")

  }

}
