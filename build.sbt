import Settings._
import sbt._
import Keys._
//
//import sbtbuildinfo.BuildInfoKey.action
//import sbtbuildinfo.BuildInfoKeys.{buildInfoKeys, buildInfoOptions, buildInfoPackage}
//import sbtbuildinfo.{BuildInfoKey, BuildInfoOption}
//
//import sbt._
//import Keys._
//
//import scala.util.Try
//import scala.sys.process.Process
//import complete.DefaultParsers._

//
//defaultScalaVersion
//defaultCrossScalaVersions
//projectVersionSetting

lazy val modulesDir = file("modules")

lazy val domain = project in modulesDir / "domain"

val auth = (project in modulesDir / "api" / "auth" )
  .dependsOn(domain)

lazy val repository = (project in modulesDir / "repository")
  .dependsOn(domain)

lazy val infra = (project in modulesDir / "infra")
  .dependsOn(domain, auth, repository)
  .enablePlugins(SbtTwirl)

lazy val app = (project in modulesDir / "app")
  .dependsOn(domain, repository, infra, auth)

lazy val root = (project in file("."))
  .aggregate(domain, repository, infra, auth, app)
  .settings(
    organization := "com.group.quasi",
    organizationName := "quasi-group-inc",
    name := "app",
    scalacOptions ++= defaultScalacOptions
  )
//
//fmtCheck
//fmt
//
//
//lazy val fatJarSettings = Seq(
//  assembly / assemblyJarName := "executable.jar",
// // assembly := assembly.dependsOn(copyWebapp).value,
//  assembly / assemblyMergeStrategy := {
//    case PathList(ps @ _*) if ps.last endsWith "io.netty.versions.properties"       => MergeStrategy.first
//    case PathList(ps @ _*) if ps.last endsWith "pom.properties"                     => MergeStrategy.first
//    case PathList(ps @ _*) if ps.last endsWith "scala-collection-compat.properties" => MergeStrategy.first
//    case x =>
//      val oldStrategy = (assembly / assemblyMergeStrategy).value
//      oldStrategy(x)
//  }
//)
//
//lazy val dockerSettings = Seq(
//  dockerExposedPorts := Seq(8080),
//  dockerBaseImage := "amazoncorretto:17",
//  Docker / packageName := "app-general",
// // dockerUsername := Some("quasi-group-inc"),
//  dockerUpdateLatest := true,
// // Docker / publishLocal := (Docker / publishLocal).dependsOn(copyWebapp).value,
//  Docker / version := git.gitDescribedVersion.value.getOrElse(git.formattedShaVersion.value.getOrElse("latest")),
//
//)