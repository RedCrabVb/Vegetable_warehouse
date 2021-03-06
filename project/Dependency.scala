import Dependency.version.{circeVersion, fs2CoreVersion, http4sVersion}
import sbt._

object Dependency {
  object version {
    val scala3Version = "2.13.0"
    val http4sVersion = "0.23.10"
    val fs2CoreVersion = "3.2.4"
    val circeVersion = "0.14.1"
  }

  object http4s {
    val http4s = Seq("org.http4s" %% "http4s-core" % http4sVersion,
      "org.http4s" %% "http4s-dsl" % http4sVersion,
      "org.http4s" %% "http4s-circe" % http4sVersion,
      "org.http4s" %% "http4s-blaze-server" % http4sVersion,
      "org.http4s" %% "http4s-blaze-client" % http4sVersion)
  }

  object circe {
    val circe = Seq(
      "io.circe" %% "circe-core" % circeVersion,
      "io.circe" %% "circe-generic" % circeVersion,
      "io.circe" %% "circe-parser" % circeVersion,
    )
  }

  object fs2 {
    val fs2 = Seq("co.fs2" %% "fs2-core" % fs2CoreVersion,
      "co.fs2" %% "fs2-io" % fs2CoreVersion,
      "co.fs2" %% "fs2-reactive-streams" % fs2CoreVersion,
      "co.fs2" %% "fs2-scodec" % fs2CoreVersion)
  }
}