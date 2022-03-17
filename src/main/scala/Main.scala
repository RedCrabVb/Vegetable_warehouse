import Main.viewRoutes
import cats.effect.{ExitCode, IO, IOApp}
import models._
import org.http4s.blaze.server.BlazeServerBuilder
import slick.dbio.DBIOAction
import slick.jdbc.JdbcBackend.Database
import slick.jdbc.PostgresProfile.api._

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration

object Main extends IOApp with Routes {
  val serverPort = 8080
  println("Server start, for test: http://localhost:8080/hello")
  val app = (
    viewRoutes
    ).orNotFound

  val server = BlazeServerBuilder[IO]
    .bindHttp(8080)
    .withHttpApp(app)

  val serverResource = server.resource

  def run(args: List[String]): IO[ExitCode] =
    server
      .serve
      .compile.drain
      .as(ExitCode.Success)

}
