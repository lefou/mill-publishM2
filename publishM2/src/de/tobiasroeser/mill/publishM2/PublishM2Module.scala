package de.tobiasroeser.mill.publishM2

import mill.T
import mill.api.PathRef
import mill.scalalib.{JavaModule, PublishModule}
import os.Path

trait PublishM2Module extends JavaModule with PublishModule {

  /**
   * Publish to the local Maven repository.
   * @param path The path to the local repository (default: `os.home / ".m2" / "repository"`).
   * @return [[PathRef]]s to published files.
   */
  def publishM2Local(path: Path = os.home / ".m2" / "repository") = T.command {
    new LocalM2Publisher(path)
      .publish(
        jar = jar().path,
        sourcesJar = sourceJar().path,
        docJar = docJar().path,
        pom = pom().path,
        artifact = artifactMetadata()
      ).map(PathRef(_))
  }

}

