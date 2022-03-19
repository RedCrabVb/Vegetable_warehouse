import Dependency.version.scala3Version
import Dependency.{circe, fs2, http4s}
import sbt._

ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := scala3Version

lazy val root = Project(id = "Vegetable_warehouse", base = file("."))
  .settings(name := "Vegetable_warehouse")
  .aggregate(
    server,
    client_web
  )

lazy val commons = (project in file("commons"))
  .disablePlugins(AssemblyPlugin)

lazy val servermain = Some("ru.vivt.server.Main")

lazy val server = (project in file("server"))
  .settings(
    mainClass := servermain,
    libraryDependencies ++= Seq(
      "com.typesafe.slick" %% "slick" % "3.3.2",
      "com.typesafe.slick" %% "slick-codegen" % "3.3.2",
      "org.postgresql" % "postgresql" % "42.3.3",
      "com.typesafe.slick" %% "slick-hikaricp" % "3.3.2",
      "org.slf4j" % "slf4j-nop" % "1.6.4"
    ),
    libraryDependencies ++= http4s.http4s,
    libraryDependencies ++= circe.circe,
    libraryDependencies ++= fs2.fs2
  ).dependsOn(commons)

val webappMain = Some("ru.vivt.webapp.Main")

lazy val client_web =  (project in file("./webapp"))
  .enablePlugins(ScalaJSPlugin)
  .settings(
    mainClass := webappMain,
    libraryDependencies ++= Seq(
      "org.scala-js" %%% "scalajs-dom"          % "1.1.0",
      "com.lihaoyi" %%% "scalatags" % "0.8.6",
      "io.circe"     %%% "circe-core"           % "0.14.1",
      "io.circe"     %%% "circe-generic"        % "0.14.1",
      "io.circe"     %%% "circe-parser"         % "0.14.1",
      "io.circe"     %%% "circe-generic-extras" % "0.14.1",
      "io.circe"     %%% "circe-jawn"           % "0.15.0-M1",
      "io.circe"     %%% "circe-derivation"     % "0.13.0-M5"
    ),
    scalaJSUseMainModuleInitializer := true
  )
  .settings(artifactPath in(Compile, fastOptJS) := new File("./server/src/main/resources/html/script/main.js"))
  .dependsOn(commons)