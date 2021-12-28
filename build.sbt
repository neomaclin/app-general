import Settings.{defaultCrossScalaVersions, fmt, fmtCheck, defaultScalaVersion}

defaultScalaVersion
defaultCrossScalaVersions

lazy val modulesDir = file("modules")

lazy val domain = project in modulesDir / "domain"

lazy val api = (project in modulesDir / "api")
  .dependsOn(domain)

lazy val repository = (project in modulesDir / "repository")
  .dependsOn(domain)

lazy val infra = (project in modulesDir / "infra")
  .dependsOn(domain)

lazy val app = (project in modulesDir / "app")
  .dependsOn(domain, api, repository, infra)

lazy val root = (project in file("."))
  .aggregate(app, api, domain, repository, infra)
  .settings(
    name := "exchange-platform",
  )

fmtCheck
fmt
