import Dependencies._

name := "exchange-infra"

libraryDependencies ++= Seq(
  "org.springframework.security" % "spring-security-crypto" % "5.6.0",
  "commons-logging" % "commons-logging" % "1.2", // because of spring-security-crypto
)

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
