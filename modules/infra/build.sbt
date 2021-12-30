import Dependencies._
import Settings._

name := "exchange-infra"

libraryDependencies ++= Seq(
  "org.springframework.security" % "spring-security-crypto" % "5.6.0",
  "commons-logging" % "commons-logging" % "1.2", // because of spring-security-crypto
  "com.github.jwt-scala" %% "jwt-circe" % "9.0.2",
)

defaultScalaVersion
defaultCrossScalaVersions
projectVersionSetting

scalacOptions ++= defaultScalacOptions

fmtCheck
fmt
