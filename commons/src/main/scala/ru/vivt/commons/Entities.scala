package ru.vivt.commons


case class ClientInfo(idClient: Int, login: String, amount: Int)
case class SalesInfo(nameSales: String, loginUser: String, paymentDate: String, orderCompletionMark: Boolean, amount: Int, goodsName: String)
case class EmployeeInfo(idEmployee: Int, fullName: String, login: String, passport: String, position: String, salary: Int)
case class Goods(idgoods: Int, namegoods: String, characteristics: String, note: Option[String])
case class Position(idposition: Long, nameposition: String, salary: Option[Long] = None, note: Option[String] = None)

object Entities {

}
