import models._
import slick.dbio.DBIOAction
import slick.jdbc.JdbcBackend.Database
import slick.jdbc.PostgresProfile.api._

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration

object Main {

  def main(args: Array[String]): Unit = {
    val db = Database.forConfig("mydb")
    try {
      val sql = DBIO.seq(
        Tables.Goods.sortBy(_.idgoods).result.map(_.foreach(println(_))),
        Procedure.sellGoods(5, 11, 29, Array(1), "01.03.3009"),
        View.selectInfo().map(_.foreach(x => println(View.toClientInfo(x)))),
        View.salesInfo().map(_.foreach(x => println(View.toSalesInfo(x)))),
        View.employeeInfo().map(_.foreach(x => println(View.toEmployee(x))))
      )
      Await.result(db.run(sql), Duration.Inf)


    } finally db.close()
    println("Hello world")
  }
}
