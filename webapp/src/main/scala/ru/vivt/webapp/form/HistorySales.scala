package ru.vivt.webapp.form

import io.circe.generic.auto._
import io.circe.jawn.decode
import org.scalajs.dom.document
import org.scalajs.dom.ext.Ajax
import ru.vivt.commons.SalesInfo
import ru.vivt.webapp.utils.{AlertMessage, Table}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.scalajs.js.annotation.JSExportTopLevel
import scala.util.{Failure, Success}

object HistorySales extends Table with AlertMessage {

  @JSExportTopLevel("addFormHistorySales")
  def addFormHistorySales(container: String, alertContainer: String): Unit = {
    Ajax.post("/api/view/sales")
      .onComplete {
        case Success(xhr) =>
          decode[Array[SalesInfo]](xhr.responseText) match {
            case Right(salesInfo) =>

              val header = Array("Имя продавца", "Логин пользователя", "Дата оплаты", "Статус оплаты", "Сумма", "Имя товаров")
              val salesInfoNotRepeat = salesInfo.groupMapReduce(_.paymentDate){//_.paymentDate may not be unique, there may be a bug
                case SalesInfo(nameSales, loginUser, paymentDate, orderCompletionMark, amount, goodsName) =>
                  (nameSales, loginUser, paymentDate, orderCompletionMark, amount, List(goodsName))
              } {
                case ((ns, lu, pdate, mark, amount, goods), (_, _, _, _, _, goods2)) => (ns, lu, pdate, mark, amount, goods ::: goods2)
              }.valuesIterator

              val rows = salesInfoNotRepeat.map(s => Array[String](s._1, s._2, s._3,
                if (s._4) "Завершено" else "Не Завершено", s._5.toString, s._6.mkString(", "))).toArray

              document.getElementById(container).innerHTML = simpleTable(header, rows)
            case Left(error) =>
              addFormError(alertContainer, "Ошибка при разборе данных с сервера")
          }
        case Failure(_) =>
          addFormError(alertContainer, "Ошибка при попытке получить данные с сервера")
      }
  }
}
