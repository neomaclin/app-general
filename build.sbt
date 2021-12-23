
lazy val domain = (project in file("modules/domain"))

lazy val api = (project in file("modules/api")).dependsOn(domain)

lazy val client = (project in file("modules/client")).dependsOn(domain)

lazy val persistence = (project in file("modules/persistence")).dependsOn(domain)

lazy val metric = (project in file("modules/metric")).dependsOn(domain)

lazy val infra = (project in file("modules/infra")).dependsOn(domain)

lazy val root = (project in file("."))
  .settings(
    name := "GeneralApp",
    idePackagePrefix := Some("com.group.quasi") ,
    version := "0.1.0-SNAPSHOT",
    scalaVersion := "2.13.7"
  )

scalafmtCheckAll := {
  (Compile/scalafmtSbtCheck).value
  (Compile/scalafmtCheck).value
  (Test/scalafmtCheck).value
}

scalafmtAll := {
  (Compile/scalafmtSbt).value
  (Compile/scalafmt).value
  (Test/scalafmt).value
}