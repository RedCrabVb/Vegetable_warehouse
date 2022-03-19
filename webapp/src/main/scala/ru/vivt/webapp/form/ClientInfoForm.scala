package ru.vivt.webapp.form

import io.circe.generic.auto._
import io.circe.jawn.decode
import org.scalajs.dom.ext.Ajax
import ru.vivt.commons.ClientInfo
import ru.vivt.webapp.utils.{AlertMessage, Table}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.scalajs.js.annotation.JSExportTopLevel
import scala.util.{Failure, Success}
import org.scalajs.dom.document

object ClientInfoForm extends Table with AlertMessage {
  @JSExportTopLevel("addFormClientInfo")
  def addFormClientInfo(container: String, alertContainer: String): Unit = {
    Ajax.post("/api/view/client")
      .onComplete {
        case Success(xhr) =>
          decode[Array[ClientInfo]](xhr.responseText) match {
            case Right(salesInfo) =>
              val header = Array("idClient", "login", "amount")
              val rows = salesInfo.map(s => Array[String](s.idClient.toString, s.login, s.amount.toString))
              document.getElementById(container).innerHTML = simpleTable(header, rows)
            case Left(error) =>
              addFormWarning(alertContainer, "Ошибка при попытке получить данные с сервера")
          }
        case Failure(_) =>
          addFormWarning(alertContainer, "Ошибка при попытке получить данные с сервера")
      }
  }

}
