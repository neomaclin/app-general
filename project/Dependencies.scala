import sbt._

object Dependencies {
  object versions {
    val scala2 = "2.13.7"
    val scala3 = "3.1.0"
    val cats = "2.7.0"
    val distage = "1.0.8"
    val tapir = "0.19.3"
    val circe = "0.14.1"
  }

  val tapir = Seq(
    "com.softwaremill.sttp.tapir" %% "tapir-core" % versions.tapir,
    "com.softwaremill.sttp.tapir" %% "tapir-json-circe" % versions.tapir
  )

  val cats = Seq(
    "org.typelevel" %% "cats-core" % versions.cats,
    "org.typelevel" %% "cats-kernel" % versions.cats
  )

  val defaultScalacOptions = Seq(
    "-feature",
    "-deprecation",
    "-unchecked",
    "-language:postfixOps",
    "-language:higherKinds"
  )

  val distage = Seq(
    "io.7mind.izumi" %% "distage-core" % versions.distage,
    "io.7mind.izumi" %% "distage-testkit-core" % versions.distage % Test,
    "io.7mind.izumi" %% "distage-testkit-scalatest" % versions.distage % Test
  )

  val circe = Seq(
    "io.circe" %% "circe-core" % versions.circe,
    "io.circe" %% "circe-generic" % versions.circe,
    "io.circe" %% "circe-generic-extras" % versions.circe,
    "io.circe" %% "circe-parser" % versions.circe
  )
}
