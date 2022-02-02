import Settings._
import sbt._
import Keys._

lazy val modulesDir = file("backend") / "modules"

lazy val domain = project in modulesDir / "domain"

val auth = (project in modulesDir / "api" / "auth" )
  .dependsOn(domain)

lazy val repository = (project in modulesDir / "repository")
  .dependsOn(domain)

lazy val notification = (project in modulesDir / "notification")
  .dependsOn(domain)
  .enablePlugins(SbtTwirl)

lazy val infra = (project in modulesDir / "infra")
  .dependsOn(domain, auth, repository, notification)

lazy val app = (project in modulesDir / "app")
  .dependsOn(infra)

lazy val root = (project in file("."))
  .aggregate(domain, repository, infra, auth, app, notification)
  .settings(
    organization := "com.group.quasi",
    organizationName := "quasi-group-inc",
    name := "app",
    scalacOptions ++= defaultScalacOptions
  ).enablePlugins(DockerPlugin)
