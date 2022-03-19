package ru.vivt.webapp.form

import io.circe.generic.auto._
import io.circe.jawn.decode
import org.scalajs.dom.document
import org.scalajs.dom.ext.Ajax
import ru.vivt.commons.Goods
import ru.vivt.webapp.utils.{ItemHtml, Table}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.scalajs.js.annotation.{JSExport, JSExportTopLevel}
import scala.util.{Failure, Success}

object GoodsForm extends Table with ItemHtml {
  @JSExportTopLevel("addFormGoods")
  def addFormGoods(container: String, alertContainer: String): Unit = {
    Ajax.post("/api/goods", "")
      .onComplete {
        case Success(xhr) =>
          val goods = (decode[Array[Goods]](xhr.responseText) match {
            case Right(goodsRes) =>
              goodsRes
            case Left(error) =>
              ???
          })
          val header = Array("idGoods", "nameGoods", "characteristics", "note")
          val rows = goods.map(g => Array[String](g.idgoods.toString ,g.namegoods, g.characteristics, g.note.getOrElse(null)))
          document.getElementById(container).innerHTML = simpleTable(header, rows)
        case Failure(_) =>
          if (alertContainer.nonEmpty) {
            document.getElementById(alertContainer).innerHTML = "Ошибка при регестрации"
          }
      }
  }
}
