package ru.vivt.server

import cats.data.NonEmptyList
import cats.effect.IO
import io.circe.generic.auto._
import org.http4s.CacheDirective.`no-cache`
import org.http4s.circe.CirceEntityCodec.circeEntityEncoder
import org.http4s.dsl.Http4sDsl
import org.http4s.headers.`Cache-Control`
import org.http4s.{HttpRoutes, _}
import ru.vivt
import ru.vivt.server
import ru.vivt.server.models.DataBaseUtil._
import ru.vivt.server.models.{Procedure, Tables, View}
import slick.jdbc.PostgresProfile.api._

import java.io.File
import java.text.SimpleDateFormat
import java.util.{Calendar, TimeZone}

trait Routes {
  val dsl: Http4sDsl[IO] = Http4sDsl[IO]

  import dsl._

  def viewRoutes: HttpRoutes[IO] = {
    HttpRoutes.of[IO] {
      case GET -> Root / "hello" =>
        for {
          resp <- Ok("Hello")
        } yield resp
      case POST -> Root / "api" / "view" / "sales" =>
        for {
          salesInfo <- IO.fromFuture(IO(db.run(View.salesInfo())))
          resp <- Ok(IO(salesInfo.map(x => View.toSalesInfo(x))))
        } yield resp
      case POST -> Root / "api" / "view" / "employee" =>
        for {
          salesInfo <- IO.fromFuture(IO(db.run(View.employeeInfo())))
          resp <- Ok(IO(salesInfo.map(x => View.toEmployee(x))))
        } yield resp
      case POST -> Root / "api" / "view" / "client" =>
        for {
          salesInfo <- IO.fromFuture(IO(db.run(View.clientInfo())))
          resp <- Ok(IO(salesInfo.map(x => View.toClientInfo(x))))
        } yield resp
    }
  }

  def static(file: String, request: Request[IO]) = {
    StaticFile.fromFile(new File("./server/src/main/resources/" + file), Some(request))
      .map(_.addHeader(`Cache-Control`(NonEmptyList(`no-cache`(), Nil))))
      .getOrElseF(NotFound())
  }

  def loadStatic(req: Request[IO], path: String, level: Array[Long]) = {
    for {
      cookies <- IO(req.cookies.filter(_.name == "authcookie").map(_.content).mkString("&"))
      authorizationTry <- checkPrivileges(ru.vivt.commons.Entities.toKeyValue(cookies), level)
      resp <- authorizationTry match {
        case Right(_) =>
          static(s"html/static/$path.html", req)
        case Left(_) =>
          static(s"html/static/login.html", req)
      }
    } yield resp
  }


  def webRoutes: HttpRoutes[IO] = {
    HttpRoutes.of[IO] {
      case req@GET -> Root / "script" / path =>
        static(s"html/script/$path", req)
      case req@GET -> Root / "style" / path =>
        static(s"html/static/$path", req)
      case req@GET -> Root / "app" / path if (List("goods", "main").contains(path)) =>
        loadStatic(req, path, Array(0, 1, 2))
      case req@GET -> Root / "app" / path if (List("sellGoods", "historySales").contains(path)) =>
        loadStatic(req, path, Array(1, 2))
      case req@GET -> Root / "app" / path if (List("clientInfo", "employeeInfo").contains(path)) =>
        loadStatic(req, path, Array(2))
      case req@GET -> Root / "login" =>
        static(s"html/static/login.html", req)
      case req@GET -> Root / "logout" =>
        static(s"html/static/login.html", req)
          .map(_.addHeader(`Cache-Control`(NonEmptyList(`no-cache`(), Nil))))
          .map(_.removeCookie("authcookie"))
      case req@GET -> Root / "bootstrap" / path2 / path =>
        static(s"html/bootstrap-5.1.3-dist/$path2/$path", req)
    }
  }

  def accountRoutes: HttpRoutes[IO] = {
    HttpRoutes.of[IO] {
      case req@POST -> Root / "registration" / "employee" =>
        for {
          user <- req.as[String]
          keyValue <-IO (ru.vivt.commons.Entities.toKeyValue(user))
          _ <- IO.println("employee: " + keyValue.mkString(","))
          result <- IO.fromFuture(IO(db.run(Procedure.registrationEmployee(
            keyValue("fullName"),
            keyValue("passport"),
            keyValue("position"),
            keyValue("login"),
            keyValue("password")
          ))))
          _ <- IO.println(result)
          resp <- Ok(result)
        } yield resp
      case req@POST -> Root / "registration" / "client" =>
        for {
          user <- req.as[String]
          keyValue <-IO (ru.vivt.commons.Entities.toKeyValue(user))
          result <- IO.fromFuture(IO(db.run(Procedure.registrationClient(keyValue("username"), keyValue("password")))))
          resp <- Ok(result)
        } yield resp
      case req@POST -> Root / "login" =>
        for {
          user <- req.as[String]
          keyValue <-IO (ru.vivt.commons.Entities.toKeyValue(user))
          authorizationTry <- checkPrivileges(keyValue, Array(-1))
          resp <- authorizationTry match {
            case Right(userCookie) =>
                Ok()
                  .map(_.removeCookie("authcookie"))
                  .map(_.addCookie(ResponseCookie("authcookie", userCookie)))
            case Left(_) =>
              Forbidden("Not right data")
          }
        } yield resp
    }
  }

  val format: SimpleDateFormat = new SimpleDateFormat("YYYY-MM-dd hh:mm:ss")
  def apiRoots: HttpRoutes[IO] = {
    HttpRoutes.of[IO] {
      case POST -> Root / "api" / "goods" =>
        for {
          goods <- IO.fromFuture(IO(db.run(Tables.Goods.result)))
          resp <- Ok(goods)
        } yield resp
      case POST -> Root / "api" / "position" =>
        for {
          position <- IO.fromFuture(IO(db.run(Tables.Position.result)))
          resp <- Ok(position)
        } yield resp
      case req@GET -> Root / "api" / "infoaccount" =>
        for {
          cookies <- IO(ru.vivt.commons.Entities.toKeyValue(req.cookies.filter(_.name == "authcookie").map(_.content).mkString("&")))
          infoAccount <- IO{
            import models.Tables._
            import io.circe.syntax._
            val user = getUser(cookies("username"), cookies("password")).get
            val client = runQ(Client.filter(_.iduser === user.idUser).result.headOption)
            val employee = runQ((Employee join Position)
              .on(_.idposition === _.idposition).filter(_._1.iduser === user.idUser)
              .result.headOption)
            if (client.isDefined) {
              client.get.asJson
            } else {
              val employeeGet = employee.get
              Map("fullName" -> employeeGet._1.fullName,
                "nameposition" -> employeeGet._2.nameposition.toString,
                "salary" -> employeeGet._2.salary.get.toString,
                "note" -> employeeGet._2.note.getOrElse("null"),
                "passport" -> employeeGet._1.passport.get.toString).asJson
            }
          }
          resp <- Ok(infoAccount)
        } yield resp
      case req@POST -> Root / "api" / "sellGoods" =>
        for {
          cookie <- IO(req.cookies.filter(_.name == "authcookie").map(_.content).mkString("&"))
          body <- req.as[String]
          keyValue <- IO(ru.vivt.commons.Entities.toKeyValue(cookie + "&" + body))
          _ <- IO.println("sellGoods: " + keyValue)
          _ <- IO.fromFuture(IO(db.run(Procedure.sellGoods(
            getClientOnLogin(keyValue("client")).idclient.toInt,
            getEmployee(getUser(keyValue("username"), keyValue("password")).get).idemployee.toInt,
            keyValue("sum").toInt,
            keyValue("goods").split(",").map(_.toInt), format.format(Calendar.getInstance(TimeZone.getTimeZone("Europe/Moscow")).getTime())))))
          resp <- Ok(keyValue)
        } yield resp
    }
  }
}
