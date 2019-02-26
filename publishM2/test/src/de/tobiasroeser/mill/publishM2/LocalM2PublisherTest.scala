package de.tobiasroeser.mill.publishM2

import java.io.File

import de.tobiasroeser.lambdatest.FunctionWithException
import de.tobiasroeser.lambdatest.TempFile._
import mill.api.{Ctx, PathRef}
import mill.scalalib.publish.Artifact
import mill.util.DummyLogger
import org.scalatest.FreeSpec
import os.Path

class LocalM2PublisherTest extends FreeSpec {

  implicit def f2f[T](f: File => T) = new FunctionWithException[File, T] {
    override def apply(param: File): T = f(param)
  }

  implicit val logger = DummyLogger
  implicit val ctx = Ctx.Log.logToCtx(logger)

  "Publish copies artifact files" in {
    withTempDir { repo =>
      withTempFile("JAR", jar => {
        withTempFile("DOC", doc => {
          withTempFile("SRC", src => {
            withTempFile("POM", pom => {
              val publisher = new LocalM2Publisher(Path(repo))
              val artifact = Artifact("group.org", "id", "version")
              val res = publisher.publish(Path(jar), Path(src), Path(doc), Path(pom), artifact)
              val expected = Set(
                Path(repo) / 'group / 'org / 'id / 'version / "id-version.jar",
                Path(repo) / 'group / 'org / 'id / 'version / "id-version.pom",
                Path(repo) / 'group / 'org / 'id / 'version / "id-version-sources.jar",
                Path(repo) / 'group / 'org / 'id / 'version / "id-version-javadoc.jar"
              )
              assert(res.size === 4)
              assert(res.toSet === expected)
              assert(os.walk(Path(repo)).filter(os.isFile).toSet === expected)

            })
          })
        })
      })
    }
  }

  "Publish overwrites existing artifact files" in {
    withTempDir { repo =>
      os.write(Path(repo) / 'group / 'org / 'id / 'version / "id-version.jar", "OLDJAR", createFolders = true)
      assert(os.read(Path(repo) / 'group / 'org / 'id / 'version / "id-version.jar") === "OLDJAR")
      withTempFile("JAR", jar => {
        withTempFile("DOC", doc => {
          withTempFile("SRC", src => {
            withTempFile("POM", pom => {
              val publisher = new LocalM2Publisher(Path(repo))
              val artifact = Artifact("group.org", "id", "version")
              val res = publisher.publish(Path(jar), Path(src), Path(doc), Path(pom), artifact)
              val expected = Set(
                Path(repo) / 'group / 'org / 'id / 'version / "id-version.jar",
                Path(repo) / 'group / 'org / 'id / 'version / "id-version.pom",
                Path(repo) / 'group / 'org / 'id / 'version / "id-version-sources.jar",
                Path(repo) / 'group / 'org / 'id / 'version / "id-version-javadoc.jar"
              )
              assert(res.size === 4)
              assert(res.toSet === expected)
              assert(os.walk(Path(repo)).filter(os.isFile).toSet === expected)
              assert(os.read(Path(repo) / 'group / 'org / 'id / 'version / "id-version.jar") === "JAR")
            })
          })
        })
      })
    }
  }

}