package ru.vivt.webapp.form

import io.circe.generic.auto._
import io.circe.jawn.decode
import org.scalajs.dom.{document, html}
import org.scalajs.dom.ext.Ajax
import ru.vivt.commons.{ClientInfo, Goods}
import ru.vivt.webapp.form.ClientInfoForm.{addFormError, simpleTable}
import ru.vivt.webapp.form.GoodsForm.{addFormError, simpleTable}
import ru.vivt.webapp.form.HistorySales.addFormError
import ru.vivt.webapp.utils.{AlertMessage, ItemHtml, Table}
import scalatags.JsDom.all.li

import scala.concurrent.ExecutionContext.Implicits.global
import scala.scalajs.js.annotation.{JSExport, JSExportTopLevel}
import scala.util.{Failure, Success}

object SellGoodsForm extends ItemHtml with AlertMessage {
  @JSExportTopLevel("addFormGoodsSell")
  def addFormGoodsSell(container: String, alertContainer: String): Unit = {
    Ajax.post("/api/goods", "")
      .onComplete {
        case Success(xhr) =>
          decode[Array[Goods]](xhr.responseText) match {
            case Right(goods) =>
              addForm(container, getBody(container))
              addGoodsList("goodsDataList")
              getClientList("clientDatList")
            case Left(error) =>
              addFormError(alertContainer, "Ошибка при разборе данных с сервера")
          }
        case Failure(_) =>
          addFormError(alertContainer, "Ошибка при попытке получить данные с сервера")
      }
  }

  def getClientList(container: String): Unit = {
    Ajax.post("/api/view/client")
      .onComplete {
        case Success(xhr) =>
          decode[Array[ClientInfo]](xhr.responseText) match {
            case Right(salesInfo) =>
              document.getElementById(container).innerHTML = salesInfo.map(_.login).map(x => s"<option value='$x'/>").mkString("\n")
          }
        case Failure(exception) => println("getClientList error: " + exception)
      }
  }

  def addGoodsList(container: String): Unit = {
    Ajax.post("/api/goods", "")
      .onComplete {
        case Success(xhr) =>
          decode[Array[Goods]](xhr.responseText) match {
            case Right(goods) =>
              val goodsList = goods.map(g => (g.idgoods, g.namegoods)).map(x => s"<option value='${x._1}|${x._2}'/>").mkString("\n")
              addForm(container, goodsList)
          }
        case Failure(exception) => println("addGoodsList error: " + exception)
      }
  }

  @JSExportTopLevel("addGoodsToInputList")
  def addGoodsToInputList(container: String, goodsDataList: String, goodsInput: String, alertContainer: String) = {
    val goodsList = document.getElementById(container).innerHTML
    val newItem = getInputDataList(goodsInput)
    document.getElementById(goodsInput).asInstanceOf[html.Input].value = ""
    if (newItem.isDefined && document.getElementById(goodsDataList).innerHTML.contains("value=\"" + newItem.get + "\"")) {
      document.getElementById(container).innerHTML = goodsList + s"\n<li class='list-group-item'>${newItem.get}</li>"
    } else {
      addFormError(alertContainer, "Ошибка при попытке добавления товара ")
    }
  }

  @JSExportTopLevel("createKeyValue")
  def createKeyValue(containerClient: String, containerGoods: String, containerSum: String) = {
    def getAllOption(html: String) = {
      val pattern = "(<li class=\"list-group-item\">(.+)\\|(.+)</li>)".r
      val allValue = pattern.findAllIn(html).matchData.map(x => (x.group(2).toInt))
      allValue
    }

    val idGoods = getAllOption(document.getElementById(containerGoods).innerHTML).mkString(",")
    println(idGoods)
    s"""client=${getInput(containerClient)}
       |&sum=${getInput(containerSum)}
       |&goods=${idGoods}""".stripMargin
  }


  def getBody(container: String) = {
    s"""
       |<div class="d-flex 2" style="padding: 30%; padding-top: 10%">
       |    <div>
       |         <div id="alert-SellGoodsForm-$container"></div>
       |        <div class="mb-3 row">
       |            <label for="clientlogin-input" class="form-label">Id клиента</label>
       |            <input class="form-control" list="clientDatList" id="clientlogin-input"
       |                   placeholder="Введите для поиска...">
       |            <datalist id="clientDatList">
       |            </datalist>
       |        </div>
       |        <div class="mb-3 row">
       |            <label for="clientlogin-input" class="form-label">Сумма сделки</label>
       |            <input type="text" class="form-control" id="sum-input">
       |        </div>
       |        <div class="mb-3 row">
       |            <div class="mb-3 row">
       |                <label for="goods_id-input" class="form-label">Id Товара</label>
       |                <input class="form-control" list="goodsDataList" id="goods_id-input"
       |                       placeholder="Введите для поиска...">
       |                <datalist id="goodsDataList">
       |                </datalist>
       |            </div>
       |            <div class="mb-2 row">
       |                <button class="btn btn-outline-secondary" type="button" onclick="addGoodsToInputList('goodsUL', 'goodsDataList', 'goods_id-input', 'alert-SellGoodsForm-$container')">Добавить товар</button>
       |                <button class="btn btn-primary mt-2" type="button" onclick="createKeyValue('clientlogin-input', 'goodsUL', 'sum-input')">Отправить данные</button>
       |                <div>
       |                    <label class="form-label mt-3">Товары:</label>
       |                    <ul class="list-group" id="goodsUL">
       |                    </ul>
       |                </div>
       |            </div>
       |        </div>
       |    </div>
       |</div>""".stripMargin
  }
}
