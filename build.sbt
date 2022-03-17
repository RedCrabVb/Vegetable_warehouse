import Dependency.version.scala3Version
import Dependency.{circe, fs2, http4s}
import sbt._

ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := scala3Version

lazy val root = (project in file("."))
  .settings(
    libraryDependencies ++= Seq(
      "com.typesafe.slick" %% "slick" % "3.3.2",
      "com.typesafe.slick" %% "slick-codegen" % "3.3.2",
      "org.postgresql" % "postgresql" % "42.3.3",
      "com.typesafe.slick" %% "slick-hikaricp" % "3.3.2",
      "org.slf4j" % "slf4j-nop" % "1.6.4"
    ),
    libraryDependencies ++= http4s.http4s,
    libraryDependencies ++= circe.circe,
    libraryDependencies ++= fs2.fs2,
    name := "Vegetable_warehouse"
  )
