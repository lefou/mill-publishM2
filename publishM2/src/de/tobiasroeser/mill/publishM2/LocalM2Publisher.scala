package de.tobiasroeser.mill.publishM2

import mill.api.Ctx
import mill.scalalib.publish.Artifact
import os.Path

class LocalM2Publisher(m2Repo: Path) {

  def publish(
    jar: Path,
    sourcesJar: Path,
    docJar: Path,
    pom: Path,
    artifact: Artifact
  )(implicit ctx: Ctx.Log): Seq[Path] = {
    val releaseDir = m2Repo / artifact.group.split("[.]") / artifact.id / artifact.version
    ctx.log.info(s"Publish ${artifact.id}-${artifact.version} to ${releaseDir}")
    os.makeDir.all(releaseDir)
    Seq(
      jar -> releaseDir / s"${artifact.id}-${artifact.version}.jar",
      sourcesJar -> releaseDir / s"${artifact.id}-${artifact.version}-sources.jar",
      docJar -> releaseDir / s"${artifact.id}-${artifact.version}-javadoc.jar",
      pom -> releaseDir / s"${artifact.id}-${artifact.version}.pom"
    ).map {
        case (from, to) =>
          os.copy.over(from, to)
          to
      }
  }

}
