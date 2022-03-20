package ru.vivt.commons

import scala.util.Try


case class ClientInfo(idClient: Int, login: String, amount: Int)
case class SalesInfo(nameSales: String, loginUser: String, paymentDate: String, orderCompletionMark: Boolean, amount: Int, goodsName: String)
case class EmployeeInfo(idEmployee: Int, fullName: String, login: String, passport: String, position: String, salary: Int)
case class Goods(idgoods: Int, namegoods: String, characteristics: String, note: Option[String])
case class Position(idposition: Long, nameposition: String, salary: Option[Long] = None, note: Option[String] = None)

object Entities {
  def toKeyValue(str: String): Map[String, String] = {
    str.split("&").map(x => {
      val arr = x.split("="); (arr(0), Try(arr(1)).getOrElse(null))
    }).toMap
  }
}
