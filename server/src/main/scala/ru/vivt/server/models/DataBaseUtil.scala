package ru.vivt.server.models

import cats.effect.IO
import ru.vivt.server.models
import slick.dbio.Effect
import slick.jdbc.JdbcBackend.Database
import slick.jdbc.PostgresProfile.api._
import slick.sql.SqlAction

import scala.concurrent.Await
import scala.util.Try

object DataBaseUtil {
  val db = Database.forConfig("mydb")

  def toKeyValue(str: String): Map[String, String] = {
    str.split("&").map(x => {
      val arr = x.split("="); (arr(0), Try(arr(1)).getOrElse(null))
    }).toMap
  }

  def getUser(login: String, password: String): Option[models.Tables.UserRow] = {
    runQ(Tables.User.filter(user =>
      user.login === login &&
        user.password === password
    ).result.headOption)
  }

  def runQ[T](q: SqlAction[Option[T], NoStream, Effect.Read]) = {
    val future = db.run(q)
    Await.result(future, scala.concurrent.duration.Duration.Inf)
  }

  def getPosition(user: models.Tables.UserRow): Option[models.Tables.PositionRow] = {
    import models.Tables._
    val q = (Employee joinLeft Position on (_.idposition === _.idposition) filter (_._1.iduser === user.idUser)).result.headOption
    runQ(q).orNull._2
  }

  def checkPrivileges(keyValue: Map[String, String], level: Int): IO[Either[String, String]] = {
    lazy val user = getUser(keyValue("username"), keyValue("password"))
    lazy val checkUser = (keyValue.contains("username") && keyValue.contains("password")
    && user.isDefined && (level == 1 || getPosition(user.get).get.idposition == level))


    IO {Either.cond(level == 0 || checkUser,
      s"username=${keyValue("username")}&password=${keyValue("password")}",
      "no access rights")}
  }


}
