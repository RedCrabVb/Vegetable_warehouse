package ru.vivt.webapp.form

import io.circe.generic.auto._
import io.circe.jawn.decode
import org.scalajs.dom.document
import org.scalajs.dom.ext.Ajax
import ru.vivt.commons.Goods
import ru.vivt.webapp.form.HistorySales.addFormError
import ru.vivt.webapp.utils.{AlertMessage, ItemHtml, Table}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.scalajs.js.annotation.{JSExport, JSExportTopLevel}
import scala.util.{Failure, Success}

object GoodsForm extends Table with ItemHtml with AlertMessage {
  @JSExportTopLevel("addFormGoods")
  def addFormGoods(container: String, alertContainer: String): Unit = {
    Ajax.post("/api/goods", "")
      .onComplete {
        case Success(xhr) =>
          decode[Array[Goods]](xhr.responseText) match {
            case Right(goods) =>
              val header = Array("idGoods", "nameGoods", "characteristics", "note")
              val rows = goods.map(g => Array[String](g.idgoods.toString ,g.namegoods, g.characteristics, g.note.getOrElse(null)))
              document.getElementById(container).innerHTML = simpleTable(header, rows)
            case Left(error) =>
              addFormError(alertContainer, "Ошибка при разборе данных с сервера")
          }

        case Failure(_) =>
          addFormError(alertContainer, "Ошибка при попытке получить данные с сервера")
      }
  }
}
