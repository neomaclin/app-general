import sbt._

object Dependencies {
  object versions {
    val scala2 = "2.13.7"
    val scala3 = "3.1.0"
    val akka = "2.6.18"
    val cats = "2.7.0"
    val distage = "1.0.8"
    val tapir = "0.19.3"
    val circe = "0.14.1"
    val doobie = "1.0.0-RC1"
  }

  val akka = Seq(
    "com.typesafe.akka" %% "akka-actor-typed" % versions.akka,
    "com.typesafe.akka" %% "akka-stream" % versions.akka,
  )

  val tapir = Seq(
    "com.softwaremill.sttp.tapir" %% "tapir-core" % versions.tapir,
    "com.softwaremill.sttp.tapir" %% "tapir-json-circe" % versions.tapir,
  )

  val cats = Seq(
    "org.typelevel" %% "cats-core" % versions.cats,
    "org.typelevel" %% "cats-kernel" % versions.cats,
  )

  val distage = Seq(
    "io.7mind.izumi" %% "distage-core" % versions.distage,
    "io.7mind.izumi" %% "distage-testkit-core" % versions.distage % Test,
    "io.7mind.izumi" %% "distage-testkit-scalatest" % versions.distage % Test,
  )

  val circe = Seq(
    "io.circe" %% "circe-core" % versions.circe,
    "io.circe" %% "circe-generic" % versions.circe,
    "io.circe" %% "circe-parser" % versions.circe,
  )

  val doobie = Seq(
    "org.tpolecat" %% "doobie-core" % "1.0.0-RC1",
    "org.tpolecat" %% "doobie-hikari" % "1.0.0-RC1",
    "org.tpolecat" %% "doobie-postgres" % "1.0.0-RC1",
    "org.tpolecat" %% "doobie-scalatest" % "1.0.0-RC1" % Test,
  )
}
