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
    val q = (Employee join Position on (_.idposition === _.idposition) filter (_._1.iduser === user.idUser)).map(_._2).result.headOption
    runQ(q)
  }

  def getClientOnLogin(login: String) = {
    import models.Tables._

    val q = (Client join User on (_.iduser === _.idUser) filter(_._2.login === login)).result.headOption
    runQ(q).orNull._1
  }

  def getEmployee(user: models.Tables.UserRow) = {
   import models.Tables._

   val q = Employee.filter(_.iduser === user.idUser).result.headOption
   runQ(q).orNull
  }

  def checkPrivileges(keyValue: Map[String, String], level: Array[Long]): IO[Either[String, String]] = {
    println("checkPrivileges: " + keyValue)
    lazy val user = getUser(keyValue("username"), keyValue("password"))
    lazy val position = getPosition(user.get)

    lazy val checkUser = (keyValue.contains("username") && keyValue.contains("password")
    && user.isDefined && (level.contains(0) || position.isDefined && level.contains(position.get.idposition)))

    IO {Either.cond(level.contains(-1) || checkUser,
      s"username=${keyValue("username")}&password=${keyValue("password")}",
      "no access rights")}
  }


}
