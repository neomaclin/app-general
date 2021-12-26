import Dependencies.versions

ThisBuild / version := "0.1.0-SNAPSHOT"
ThisBuild / organization := "com.github.scalacn"
ThisBuild / scalaVersion := versions.scala2
ThisBuild / crossScalaVersions := Seq(versions.scala2, versions.scala3)

lazy val domain = (project in file("modules/domain"))

lazy val api = (project in file("modules/api"))
  .dependsOn(domain)

lazy val client = (project in file("modules/client"))
  .dependsOn(domain)

lazy val persistence = (project in file("modules/persistence"))
  .dependsOn(domain)

lazy val metric = (project in file("modules/metric"))
  .dependsOn(domain)

lazy val infra = (project in file("modules/infra"))
  .dependsOn(domain)

lazy val root = (project in file("."))
  .aggregate(domain, api, client, persistence, metric, infra)

scalafmtCheckAll := {
  (Compile / scalafmtSbtCheck).value
  (Compile / scalafmtCheck).value
  (Test / scalafmtCheck).value
}

scalafmtAll := {
  (Compile / scalafmtSbt).value
  (Compile / scalafmt).value
  (Test / scalafmt).value
}
