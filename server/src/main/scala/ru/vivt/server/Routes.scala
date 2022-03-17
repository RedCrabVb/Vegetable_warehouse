package ru.vivt.server

import cats.effect.IO
import io.circe.generic.auto._
import org.http4s.HttpRoutes
import org.http4s.circe.CirceEntityCodec.circeEntityEncoder
import org.http4s.dsl.Http4sDsl
import ru.vivt.server.models.View
import slick.jdbc.JdbcBackend.Database
import cats.effect._
import org.http4s._
import org.http4s.dsl.io._
import java.io.File


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

  def static(file: String, request: Request[IO]) = {
    println(file)
    StaticFile.fromResource("/" + file, Some(request)).getOrElseF(NotFound())
  }


  def webRoutes: HttpRoutes[IO] = {
    HttpRoutes.of[IO] {
      case req@GET -> Root / "script" / path => {
        static(s"html/script/$path", req)
      }
      case req@GET -> Root / "login" => {
        static("html/static/login.html", req)
      }
    }
  }
}
