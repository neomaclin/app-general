import Dependencies.versions

lazy val modulesDir = file("modules")

lazy val domain = (project in modulesDir / "domain")

lazy val api = (project in modulesDir / "api")
  .dependsOn(domain)

lazy val client = (project in modulesDir / "client")
  .dependsOn(domain)

lazy val repository = (project in modulesDir / "repository")
  .dependsOn(domain)

lazy val metric = (project in modulesDir / "metric")
  .dependsOn(domain)

lazy val infra = (project in modulesDir / "infra")
  .dependsOn(domain)

lazy val root = (project in file("."))
  .aggregate(domain, api, client, repository, metric, infra)
  .settings(
    version := "0.1.0-SNAPSHOT",
    organization := "com.github.scalacn",
    organizationName := "exchange",
    name := "exchange",
    scalaVersion := versions.scala2,
    crossScalaVersions ++= Seq(versions.scala2, versions.scala3),
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
