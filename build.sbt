import Settings._
import sbt.Keys._
import sbt._

lazy val modulesDir = file("backend") / "modules"

lazy val domain = project in modulesDir / "domain"

val api = (project in modulesDir / "api")
  .dependsOn(domain)

lazy val repository = (project in modulesDir / "repository")
  .dependsOn(domain)

lazy val notification = (project in modulesDir / "notification")
  .dependsOn(domain)
  .enablePlugins(SbtTwirl)

lazy val infra = (project in modulesDir / "infra")
  .aggregate(domain, api, repository, notification)
  .dependsOn(domain, api, repository, notification)

lazy val app = (project in modulesDir / "app")
  .dependsOn(infra)

lazy val root = (project in file("."))
  .aggregate(app)
  .dependsOn(app)
  .enablePlugins(JavaAppPackaging)
  .enablePlugins(UniversalPlugin)
  .settings(
    maintainer := "admin@quasi-g.com",
    organization := "com.group.quasi",
    organizationName := "quasi-group-inc",
    scalaVersion := "2.13.8",
    name := "app",
    Compile / mainClass := Some("com.group.quasi.app.WebApp"),
    scalacOptions ++= defaultScalacOptions
  )
