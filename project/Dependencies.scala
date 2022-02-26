import sbt.{ExclusionRule, _}

object Dependencies {
  object versions {
    val scala2 = "2.13.8"
    val scala3 = "3.1.0"
    val akka = "2.6.18"
    val akkaHttp = "10.2.7"
    val cats = "2.7.0"
    val distage = "1.0.10"
    val tapir = "0.19.3"
    val circe = "0.14.1"
    val doobie = "1.0.0-RC1"
    val configSupport = "0.17.1"
    val bytebuddy = "1.12.8"
    val scalaTest = "3.2.10"
    val scalaMock = "5.2.0"
    val mockito = "1.17.0"
    val scalaLogging = "3.9.4"
    val logback = "1.2.10"
    val refined = "0.9.28"
    val alpakka = "3.0.4"
    val jwt = "9.0.3"
    val log4j = "2.17.1"
  }

  val akka = Seq(
    "com.typesafe.akka" %% "akka-actor-typed" % versions.akka,
  )

  val akkaStream = Seq(
    "com.typesafe.akka" %% "akka-stream-typed" % versions.akka,
  ) ++ akka

  val akkaHttp = Seq(
    "com.typesafe.akka" %% "akka-http" % versions.akkaHttp,
    "com.typesafe.akka" %% "akka-http-xml" % versions.akkaHttp,
    "de.heikoseeberger" %% "akka-http-circe" % "1.39.2",
  )

  val alpakka = Seq(
    "com.lightbend.akka" %% "akka-stream-alpakka-amqp",
    "com.lightbend.akka" %% "akka-stream-alpakka-sqs",
    "com.lightbend.akka" %% "akka-stream-alpakka-sns",
    "com.lightbend.akka" %% "akka-stream-alpakka-slick",
    "com.lightbend.akka" %% "akka-stream-alpakka-dynamodb",
    "com.lightbend.akka" %% "akka-stream-alpakka-s3",
    "com.lightbend.akka" %% "akka-stream-alpakka-ftp",
    "com.lightbend.akka" %% "akka-stream-alpakka-file",
  ).map(_ % versions.alpakka)

  val tapir = Seq(
    "com.softwaremill.sttp.tapir" %% "tapir-core",
    "com.softwaremill.sttp.tapir" %% "tapir-json-circe",
    "com.softwaremill.sttp.tapir" %% "tapir-akka-http-server",
    "com.softwaremill.sttp.tapir" %% "tapir-sttp-client",
    "com.softwaremill.sttp.tapir" %% "tapir-openapi-docs",
    "com.softwaremill.sttp.tapir" %% "tapir-openapi-circe",
    "com.softwaremill.sttp.tapir" %% "tapir-openapi-circe-yaml",
    "com.softwaremill.sttp.tapir" %% "tapir-refined"
  ).map(_ % versions.tapir)

  val cats = Seq(
    "org.typelevel" %% "cats-core",
    "org.typelevel" %% "cats-kernel",
  ).map(_ % versions.cats)

  val distage = Seq(
    "io.7mind.izumi" %% "distage-core" % versions.distage,
  )

  val distageTest = Seq(
    "io.7mind.izumi" %% "distage-testkit-core" % versions.distage,
    "io.7mind.izumi" %% "distage-testkit-scalatest" % versions.distage,
  )

  val configSupport = Seq(
    "com.github.pureconfig" %% "pureconfig" % versions.configSupport,
  )

  val circe = Seq(
    "io.circe" %% "circe-core",
    "io.circe" %% "circe-generic",
    "io.circe" %% "circe-parser",
    "io.circe" %% "circe-refined",
  ).map(_ % versions.circe)

  val doobie = Seq(
    "org.tpolecat" %% "doobie-core" % versions.doobie,
    "org.tpolecat" %% "doobie-postgres" % versions.doobie,
    // "org.tpolecat" %% "doobie-scalatest" % "1.0.0-RC1" % Test,
  )

  val emailDependencies = Seq(
    "com.sun.mail" % "javax.mail" % "1.6.2" exclude ("javax.activation", "activation"),
    "com.sun.activation" % "javax.activation" % "1.2.0"
  )

  val phoneDependencies = Seq(
    "com.googlecode.libphonenumber" % "libphonenumber" % "8.12.42",
  )

  val scalaTest = Seq(
    "org.scalatest" %% "scalatest" % versions.scalaTest,
  )

  val scalaMock = Seq(
    "org.scalamock" %% "scalamock" % versions.scalaMock,
    "org.mockito" %% "mockito-scala" % versions.mockito excludeAll ExclusionRule(
      organization = "net.bytebuddy",
    ),
    "net.bytebuddy" % "byte-buddy" % versions.bytebuddy,
    "net.bytebuddy" % "byte-buddy-agent" % versions.bytebuddy,
  )


  val loggingDependencies = Seq(
    "com.typesafe.scala-logging" %% "scala-logging" % versions.scalaLogging,
  )

  val refined = Seq(
    "eu.timepit" %% "refined" % versions.refined,
    "eu.timepit" %% "refined-cats" % versions.refined, // optional
  )

  val jwt = Seq(
    "com.github.jwt-scala" %% "jwt-circe" % versions.jwt excludeAll ExclusionRule(
      organization = "org.bouncycastle",
      ),
    "org.bouncycastle" % "bcprov-jdk15on" % "1.70",
    "org.bouncycastle" % "bcpkix-jdk15on" % "1.70"
  )

  val slickPg = Seq(
    "com.github.tminglei" %% "slick-pg" % "0.20.2",
    "com.github.tminglei" %% "slick-pg_circe-json" % "0.20.2",
  )

  val quartzScheduler = Seq(
    "com.enragedginger" %% "akka-quartz-scheduler" % "1.9.2-akka-2.6.x"
  )

  val common = Seq(
    "com.softwaremill.common" %% "id-generator" % "1.3.1"
  )

  val flyway = Seq(
    "org.flywaydb" % "flyway-core" % "8.5.0"
  )

  val logger = Seq(
    "org.apache.logging.log4j" % "log4j-api" % versions.log4j,
    "org.apache.logging.log4j" % "log4j-core" % versions.log4j,
    "org.apache.logging.log4j" % "log4j-slf4j-impl" % versions.log4j,
  )
}
