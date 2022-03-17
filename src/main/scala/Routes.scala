import cats.effect.{Concurrent, IO, Sync}
import cats.syntax.all._
import cats.{Applicative, Monad, MonadThrow}
import io.circe.Encoder
import org.http4s.circe._
import org.http4s.circe.CirceEntityCodec.{circeEntityDecoder, circeEntityEncoder}
import org.http4s.dsl.Http4sDsl
import org.http4s.{EntityEncoder, HttpRoutes}
import io.circe.generic.auto._
import io.circe.syntax._
import io.circe.parser._
import slick.dbio.DBIOAction
import slick.jdbc.JdbcBackend.Database
import slick.jdbc.PostgresProfile.api._

import java.util.Date
import cats.effect.unsafe.implicits.global
import models.{Tables, View}
import slick.jdbc.JdbcBackend.Database


trait Routes {
  val db = Database.forConfig("mydb")
  val dsl: Http4sDsl[IO] = Http4sDsl[IO]

  import dsl._

  def viewRoutes: HttpRoutes[IO] = {
    HttpRoutes.of[IO] {
      case req@GET -> Root / "hello" =>
        for {
          resp <- Ok("Hello")
        } yield resp
      case req@GET -> Root / "api" / "view" / "sales" => {
        for {
          salesInfo <- IO.fromFuture(IO(db.run(View.salesInfo())))
          resp <- Ok(IO(salesInfo.map(x => View.toSalesInfo(x))))
        } yield (resp)
      }
      case req@GET -> Root / "api" / "view" / "employee" => {
        for {
          salesInfo <- IO.fromFuture(IO(db.run(View.employeeInfo())))
          resp <- Ok(IO(salesInfo.map(x => View.toEmployee(x))))
        } yield (resp)
      }
      case req@GET -> Root / "api" / "view" / "client" => {
        for {
          salesInfo <- IO.fromFuture(IO(db.run(View.clientInfo())))
          resp <- Ok(IO(salesInfo.map(x => View.toClientInfo(x))))
        } yield (resp)
      }

    }


  }
}



