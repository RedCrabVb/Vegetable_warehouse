package ru.vivt.server.models

import slick.jdbc.PostgresProfile.api._

import java.sql.Date

case class ClientInfo(idClient: Int, login: String, amount: Int)
case class SalesInfo(nameSales: String, loginUser: String, paymentDate: String, orderCompletionMark: Boolean, amount: Int, goodsName: String)
case class EmployeeInfo(idEmployee: Int, fullName: String, login: String, passport: String, position: String, salary: Int)

object View {
  def toClientInfo(tuple3: (Int, String, Int)) = {
    ClientInfo(tuple3._1, tuple3._2, tuple3._3)
  }

  def toSalesInfo(tuple6: (String, String, Date, Int, Int, String)) = {
    SalesInfo(tuple6._1 ,tuple6._2, tuple6._3.toString, tuple6._4 == 1, tuple6._5, tuple6._6)
  }

  def toEmployee(tuple6: (Int, String, String, String, String, Int)) = {
    EmployeeInfo(tuple6._1, tuple6._2, tuple6._3, tuple6._4, tuple6._5, tuple6._6)
  }

  def clientInfo() = {
    sql"select * from clientinfo".as[(Int, String, Int)]
  }

  def salesInfo() = {
    sql"select * from salesinfo".as[(String, String, Date, Int, Int, String)]
  }

  def employeeInfo() = {
    sql"select * from employeeinfo".as[(Int, String, String, String, String, Int)]
  }
}
