package ru.vivt.server

import cats.effect.{ExitCode, IO, IOApp}
import cats.implicits._
import org.http4s.blaze.server.BlazeServerBuilder
import org.http4s.implicits._

object Main extends IOApp with Routes {
  val serverPort = 8080
  println(s"Server start, for test: http://localhost:$serverPort/hello")
  val app = (
    viewRoutes <+>
      webRoutes <+>
      accountRoutes <+>
      apiRoots
    ).orNotFound

  val server = BlazeServerBuilder[IO]
    .bindHttp(serverPort)
    .withHttpApp(app)

  val serverResource = server.resource

  def run(args: List[String]): IO[ExitCode] =
    server
      .serve
      .compile.drain
      .as(ExitCode.Success)

}
