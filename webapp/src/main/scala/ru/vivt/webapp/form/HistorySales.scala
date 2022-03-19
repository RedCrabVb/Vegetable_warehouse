package ru.vivt.webapp.form

import io.circe.generic.auto._
import io.circe.jawn.decode
import org.scalajs.dom.document
import org.scalajs.dom.ext.Ajax
import ru.vivt.commons.{Goods, SalesInfo}
import ru.vivt.webapp.utils.{AlertMessage, ItemHtml, Table}

import scala.scalajs.js.annotation.JSExportTopLevel
import scala.util.{Failure, Success}
import scala.concurrent.ExecutionContext.Implicits.global

object HistorySales extends Table with AlertMessage {
  @JSExportTopLevel("addFormHistorySales")
  def addFormHistorySales(container: String, alertContainer: String): Unit = {
    Ajax.post("/api/view/sales")
      .onComplete {
        case Success(xhr) =>
          decode[Array[SalesInfo]](xhr.responseText) match {
            case Right(salesInfo) =>
              val header = Array("nameSales", "loginUser", "paymentDate", "orderCompletionMark", "amount", "goodsName")
              val rows = salesInfo.map(s => Array[String](s.nameSales, s.loginUser, s.paymentDate, s.orderCompletionMark.toString, s.amount.toString, s.goodsName.toString))
              document.getElementById(container).innerHTML = simpleTable(header, rows)
            case Left(error) =>
              addFormWarning(alertContainer, "Ошибка при попытке получить данные с сервера")
          }
        case Failure(_) =>
          addFormWarning(alertContainer, "Ошибка при попытке получить данные с сервера")
      }
  }
}
