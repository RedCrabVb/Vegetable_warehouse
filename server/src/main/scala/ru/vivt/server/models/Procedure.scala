package ru.vivt.server.models

import slick.jdbc.PostgresProfile
import slick.jdbc.PostgresProfile.api._
import slick.sql.SqlAction

object Procedure {
  implicit val strArrayParameter: slick.jdbc.SetParameter[Array[Int]] =
    slick.jdbc.SetParameter[Array[Int]]{ (param, pointedParameters) =>
      pointedParameters.setObject(param.toArray, java.sql.Types.ARRAY)
    }

  def sellGoods(idClient: Int, idSeller: Int, sum: Int, idGoods: Int, date: String): SqlAction[Int, PostgresProfile.api.NoStream, PostgresProfile.api.Effect] = {
    sellGoods(idClient, idSeller, sum, Array(idGoods), date)
  }

  def sellGoods(idClient: Int, idSeller: Int, sum: Int, idGoods: Array[Int], date: String): SqlAction[Int, NoStream, Effect] = {
    sqlu"call sell_goods($idClient::bigint, $idSeller::bigint, $sum::bigint, to_date($date, 'DD.MM.YYYY'), $idGoods)"
  }

  def registrationEmployee(fullName: String, passport: String, position: String, login: String, password: String ) = {
    sqlu"call registration_employee($fullName, $passport, $position, $login, $password)"
  }

  def registrationClient(login: String, password: String) = {
    sqlu"call registration_client($login, $password)"
  }
}
