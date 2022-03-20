package ru.vivt.server.models

import slick.jdbc.PostgresProfile.api._
import java.sql.Timestamp
import ru.vivt.commons._

object View {
  def toClientInfo(tuple3: (Int, String, Int)) = {
    ClientInfo(tuple3._1, tuple3._2, tuple3._3)
  }

  def toSalesInfo(tuple6: (String, String, Timestamp, Int, Int, String)) = {
    SalesInfo(tuple6._1 ,tuple6._2, tuple6._3.toString, tuple6._4 == 1, tuple6._5, tuple6._6)
  }

  def toEmployee(tuple6: (Int, String, String, String, String, Int)) = {
    EmployeeInfo(tuple6._1, tuple6._2, tuple6._3, tuple6._4, tuple6._5, tuple6._6)
  }

  def clientInfo() = {
    sql"select * from clientinfo".as[(Int, String, Int)]
  }

  def salesInfo() = {
    sql"select * from salesinfo".as[(String, String, Timestamp, Int, Int, String)]
  }

  def employeeInfo() = {
    sql"select * from employeeinfo".as[(Int, String, String, String, String, Int)]
  }
}
