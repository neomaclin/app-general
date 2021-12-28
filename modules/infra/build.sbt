import Dependencies._

name := "infra"

libraryDependencies ++= Seq(
  "org.springframework.security" % "spring-security-crypto" % "5.6.0",
  "commons-logging" % "commons-logging" % "1.2", // because of spring-security-crypto
)

crossScalaVersions ++= Seq(versions.scala2, versions.scala3)

scalacOptions ++= defaultScalacOptions

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
