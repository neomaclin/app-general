import Dependencies.versions

ThisBuild / version := "0.1.0-SNAPSHOT"
ThisBuild / organization := "com.github.scalacn"
ThisBuild / organizationName := "exchange"
ThisBuild / scalaVersion := versions.scala2

lazy val modulesDir = file("modules")

lazy val domain = (project in modulesDir / "domain")

lazy val api = (project in modulesDir / "api")
  .dependsOn(domain)

lazy val client = (project in modulesDir / "client")
  .dependsOn(domain)

lazy val persistence = (project in modulesDir / "persistence")
  .dependsOn(domain)

lazy val metric = (project in modulesDir / "metric")
  .dependsOn(domain)

lazy val infra = (project in modulesDir / "infra")
  .dependsOn(domain)

lazy val root = (project in file("."))
  .aggregate(domain, api, client, persistence, metric, infra)
  .settings(
    name := "exchange"
  )

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
