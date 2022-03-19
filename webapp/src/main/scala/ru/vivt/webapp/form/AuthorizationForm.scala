package ru.vivt.webapp.form

import org.scalajs.dom
import org.scalajs.dom.ext.Ajax
import org.scalajs.dom.{document, html, window}
import ru.vivt.webapp.utils.ItemHtml

import scala.concurrent.ExecutionContext.Implicits.global
import scala.scalajs.js.annotation.{JSExport, JSExportTopLevel}
import scala.util.{Failure, Success}
import io.circe.generic.auto._
import io.circe.jawn.decode
import org.scalajs.dom.document
import org.scalajs.dom.ext.Ajax
//import ru.vivt.commons.Goods
import ru.vivt.webapp.form.GoodsForm.simpleTable
import cats.implicits.catsSyntaxEitherId
import io.circe.jawn.decode
import io.circe.parser.parse
import io.circe.{Decoder, Encoder, HCursor, Json}
import io.circe.syntax._

case class Goods(idgoods: Int, namegoods: String, characteristics: String, note: Option[String])


object AuthorizationForm extends ItemHtml {
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
          val rows: Array[Array[String]] = goods.map(g => Array[String](g.idgoods.toString ,g.namegoods, g.characteristics, g.note.getOrElse(null)))
          document.getElementById(container).innerHTML = simpleTable(header, rows)
        case Failure(_) =>
          if (alertContainer.nonEmpty) {
            document.getElementById(alertContainer).innerHTML = "Ошибка при регестрации"
          }
      }
  }

  @JSExportTopLevel("addFormAuthorization")
  def addFormAuthorization(container: String, containerAlert: String) = {
    addForm(container, getBodyLoginIn(_, containerAlert))
  }

  @JSExportTopLevel("addFormInputUser")
  def addFormInputUser(container: String, containerAlert: String): Unit = {
    addForm(container, getBodyInputUser(_, containerAlert))
  }

  @JSExportTopLevel("addFormEmployee")
  def addFormEmployee(container: String, login: String, password: String, alertContainer: String) = {
    addForm(container, getBodyEmployee(_, login, password, alertContainer))
  }

  @JSExportTopLevel("authorization")
  def authorization(login: String, password: String, alertContainer: String): Unit = {
    Ajax.post("/login", s"username=${getInput(login)}&password=${getInput(password)}")
      .onComplete {
        case Success(xhr) =>
          println(xhr.responseText)
          window.location.replace("/app/main.html")
        case Failure(_) =>
          if (alertContainer.nonEmpty) {
            document.getElementById(alertContainer).innerHTML = "Ошибка при авторизации"
          }
      }
  }

  @JSExportTopLevel("registrationClient")
  def registrationClient(login: String, password: String, alertContainer: String): Unit = {
    Ajax.post("/registration/client", s"username=${getInput(login)}&password=${getInput(password)}")
      .onComplete {
        case Success(xhr) =>
          println(xhr.responseText)
          window.location.replace("/app/login.html")
        case Failure(_) =>
          if (alertContainer.nonEmpty) {
            document.getElementById(alertContainer).innerHTML = "Ошибка при регестрации"
          }
      }
  }

  @JSExportTopLevel("registrationEmployee")
  def registrationEmployee(login: String,
                           password: String,
                           fullName: String,
                           passport: String,
                           position: String,
                           alertContainer: String): Unit = {
    Ajax.post("/registration/employee",
      s"login=$login" +
        s"&password=$password" +
        s"&fullName=${getInput("fullname-input")}" +
        s"&passport=${getInput("passport")}" +
        s"&position=${getInputSelection("position-input")}")
      .onComplete {
        case Success(xhr) =>
          println(xhr.responseText)
          window.location.replace("/app/login.html")
        case Failure(_) =>
          if (alertContainer.nonEmpty) {
            document.getElementById(alertContainer).innerHTML = "Ошибка при регестрации"
          }
      }
  }


  def getBodyEmployee(container: String, login: String, password: String, alertContainer: String): String = {
    s"""
       |<div form="$container">
       |        <div class="mb-3">
       |            <label for="exampleInputEmail1" class="form-label">Полное имя</label>
       |            <input type="text" id="fullname-input" class="form-control"
       |                   aria-describedby="emailHelp">
       |        </div>
       |        <div class="mb-3">
       |            <label for="exampleInputPassword" class="form-label">Данные паспорта</label>
       |            <input type="text" id="passport" class="form-control">
       |        </div>
       |        <select class="form-select mb-3" id="position-input" aria-label="Должность">
       |            <option selected>Выбор должности</option>
       |            <option value="1">salesman</option>
       |            <option value="2">administrator</option>
       |        </select>
       |        <button class="btn btn-primary" onClick="registrationEmployee(
       |          '${getInput(login)}',
       |          '${getInput(password)}',
       |          'fullName-input',
       |          'passport',
       |          'position-input',
       |          '${alertContainer}')">Отправить</button>
       |    </div>
       |""".stripMargin
  }


  def getBodyInputUser(container: String, containerAlert: String): String = {
    s"""
       |<div id="$container">
       |        <div class="mb-3">
       |            <label for="exampleInputEmail1" class="form-label">Имя
       |                пользователя</label>
       |            <input type="text" id="username-input" class="form-control"
       |                   aria-describedby="emailHelp">
       |        </div>
       |        <div class="mb-3">
       |            <label for="exampleInputPassword" class="form-label">Пароль</label>
       |            <input type="password" id="password-input" class="form-control">
       |        </div>
       |        Я являюсь:
       |        <button class="btn btn-primary" onclick="addFormEmployee('$container', 'username-input', 'password-input', '$containerAlert')">сотрудником</button>
       |        <button class="btn btn-primary" onclick="registrationClient('username-input', 'password-input', '$containerAlert')">клиентом</button>
       |    </div>
       |""".stripMargin
  }

  def getBodyLoginIn(container: String, containerAlert: String) = {
    s"""    <div id="$container">
       |        <div class="mb-3">
       |            <label for="exampleInputEmail1" class="form-label">Имя пользователя</label>
       |            <input type="text" id="username-input" class="form-control"
       |                   aria-describedby="emailHelp">
       |        </div>
       |        <div class="mb-3">
       |            <label for="exampleInputPassword" class="form-label">Пароль</label>
       |            <input type="password" id="password-input" class="form-control">
       |        </div>
       |        <div class="mb-3"><a href="#" onclick="addFormInputUser('$container', '$containerAlert')"">Зарегистрироваться</a></div>
       |        <button type="submit" class="btn btn-primary" onclick="authorization('username-input', 'password-input', '$containerAlert')">Отправить</button>
       |    </div>""".stripMargin
  }
}
