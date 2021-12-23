import sbt._

object Dependencies {
  object versions {
    val scala2Version = "2.13.6"
    val scala3Version = "3.1.0"
    val catsVersion = "2.7.0"
    val distageVersion = "1.0.8"
    //val tapirVersion = "0.19.1"
    val circeVersion = "0.14.1"
  }

  val cats = Seq(
    "org.typelevel" %% "cats-core" % versions.catsVersion,
    "org.typelevel" %% "cats-kernel" % versions.catsVersion
  )

  val defaultScalacOptions = Seq(
    "-feature",
    "-deprecation",
    "-xfatal-warning",
    "-unchecked",
    "-language:postfixOps",
    "-language:higherKinds",
  )

  val distage = Seq(
    "io.7mind.izumi" %% "distage-core" % versions.distageVersion,
    "io.7mind.izumi" %% "distage-testkit-core" % versions.distageVersion % Test,
    "io.7mind.izumi" %% "distage-testkit-scalatest" % versions.distageVersion % Test,
  )

  val circe = Seq(
    "io.circe" %% "circe-core",
    "io.circe" %% "circe-generic",
    "io.circe" %% "circe-generic-extras",
    "io.circe" %% "circe-parser"
  ).map(_ % versions.circeVersion)
}
