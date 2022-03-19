package ru.vivt.server

import cats.effect.IO
import io.circe.generic.auto._
import org.http4s.{HttpRoutes, MediaType, QueryParamDecoder, _}
import org.http4s.circe.CirceEntityCodec.circeEntityEncoder
import org.http4s.dsl.Http4sDsl
import ru.vivt.server.models.{Procedure, Tables, View}
import slick.jdbc.JdbcBackend.Database
import cats.effect._
import cats.effect._
import org.http4s.dsl.io._
import slick.jdbc.PostgresProfile.api._
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
    StaticFile.fromFile(new File("./server/src/main/resources/" + file), Some(request)).getOrElseF(NotFound())
  }


  def webRoutes: HttpRoutes[IO] = {
    HttpRoutes.of[IO] {
      case req@GET -> Root / "script" / path =>
        static(s"html/script/$path", req)
      case req@GET -> Root / "style" / path =>
        static(s"html/static/$path", req)
      case req@GET -> Root / "app" / path =>
        static(s"html/static/$path", req)
      case req@GET -> Root / "bootstrap" / path2 / path =>
        static(s"html/bootstrap-5.1.3-dist/$path2/$path", req)
    }
  }

  def accountRoutes: HttpRoutes[IO] = {
    HttpRoutes.of[IO] {
      case req@POST -> Root / "registration" / "employee" =>
        for {
          user <- req.as[String]
          keyValue <-IO (user.split("&").map(x => {val arr = x.split("="); (arr(0), arr(1)) }).toMap)
          _ <- IO(db.run(Procedure.registrationEmployee(
            keyValue("fullName"),
            keyValue("passport"),
            keyValue("position"),
            keyValue("login"),
            keyValue("password")
          )))
          resp <- Ok()
        } yield resp
      case req@POST -> Root / "registration" / "client" =>
        for {
          user <- req.as[String]
          keyValue <-IO (user.split("&").map(x => {val arr = x.split("="); (arr(0), arr(1)) }).toMap)
          _ <- IO(db.run(Procedure.registrationClient(keyValue("username"), keyValue("password"))))
          resp <- Ok()
        } yield resp
      case req@POST -> Root / "login" => {
        for {
          user <- req.as[String]
          keyValue <-IO (user.split("&").map(x => {val arr = x.split("="); (arr(0), arr(1)) }).toMap)
          authorizationTry <- IO.fromFuture(
            IO{
              db.run(Tables.User.filter(user =>
                user.login === keyValue("username") &&
                  user.password === keyValue("password")
              ).result)
            }
          )
          resp <- if (!authorizationTry.isEmpty) {
            Ok(authorizationTry)
          } else {
            Forbidden("Not right data")
          }
        } yield resp
      }
    }
  }

  def apiRoots: HttpRoutes[IO] = {
    HttpRoutes.of[IO] {
      case req@POST -> Root / "api" / "goods" => {
        for {
          goods <- IO.fromFuture(IO(db.run(Tables.Goods.result)))
          resp <- Ok(goods)
        } yield resp
      }
    }
  }
}
