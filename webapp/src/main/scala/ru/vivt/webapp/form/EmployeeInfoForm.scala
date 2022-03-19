package ru.vivt.webapp.form

import io.circe.generic.auto._
import io.circe.jawn.decode
import org.scalajs.dom.document
import org.scalajs.dom.ext.Ajax
import ru.vivt.commons.EmployeeInfo
import ru.vivt.webapp.utils.{AlertMessage, Table}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.scalajs.js.annotation.JSExportTopLevel
import scala.util.{Failure, Success}


object EmployeeInfoForm extends Table with AlertMessage {
  @JSExportTopLevel("addFormEmployeeInfo")
  def addFormEmployeeInfo(container: String, alertContainer: String): Unit = {
    Ajax.post("/api/view/employee")
      .onComplete {
        case Success(xhr) =>
          decode[Array[EmployeeInfo]](xhr.responseText) match {
            case Right(salesInfo) =>
              val header = Array("idEmployee", "login", "fullName", "passport", "position", "salary")
              val rows = salesInfo.map(s => Array[String](s.idEmployee.toString, s.login, s.fullName, s.passport, s.position, s.salary.toString))
              document.getElementById(container).innerHTML = simpleTable(header, rows)
            case Left(error) =>
              addFormError(alertContainer, "Ошибка при разборе данных с сервера")
          }
        case Failure(_) =>
          addFormError(alertContainer, "Ошибка при попытке получить данные с сервера")
      }
  }
}
