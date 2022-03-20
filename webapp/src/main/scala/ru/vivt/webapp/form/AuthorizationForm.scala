package ru.vivt.webapp.form

import io.circe.Json
import org.scalajs.dom.ext.{Ajax, pimpRichAnimatedNumber}
import ru.vivt.commons.{ClientInfo, Goods, Position}
import ru.vivt.webapp.form.GoodsForm.addFormError
import ru.vivt.webapp.form.SellGoodsForm.addForm
import ru.vivt.webapp.utils.ItemHtml
import io.circe.generic.auto._
import io.circe.jawn.decode
import org.scalajs.dom.ext.Ajax
import org.scalajs.dom.{document, html, window}
import ru.vivt.commons.Entities.toKeyValue

import scala.concurrent.ExecutionContext.Implicits.global
import scala.scalajs.js.annotation.JSExportTopLevel
import scala.util.{Failure, Success}


object AuthorizationForm extends ItemHtml {


  @JSExportTopLevel("addFormAuthorization")
  def addFormAuthorization(container: String, containerAlert: String) = {
    addForm(container, getBodyLoginIn(_, containerAlert))
  }

  @JSExportTopLevel("addFormInputUser")
  def addFormInputUser(container: String, containerAlert: String): Unit = {
    addForm(container, getBodyInputUser(_, containerAlert))
  }

  @JSExportTopLevel("addFormEmployee")
  def addFormEmployee(container: String, login: String, password: String, containerAlert: String) = {
    addForm(container, getBodyEmployee(_, login, password, containerAlert))
    addPositionList("position-input")
  }

  @JSExportTopLevel("authorization")
  def authorization(login: String, password: String, containerAlert: String): Unit = {
    Ajax.post("/login", s"username=${getInput(login)}&password=${getInput(password)}")
      .onComplete {
        case Success(xhr) =>
          println(xhr.responseText)
          window.location.replace("/app/main")
        case Failure(_) =>
          addFormError(containerAlert, "Ошибка при авторизации")
      }
  }

  @JSExportTopLevel("registrationClient")
  def registrationClient(login: String, password: String, containerAlert: String): Unit = {
    Ajax.post("/registration/client", s"username=${getInput(login)}&password=${getInput(password)}")
      .onComplete {
        case Success(xhr) =>
          println(xhr.responseText)
          window.location.replace("/login")
        case Failure(_) =>
          addFormError(containerAlert, "Ошибка при регестрации")
      }
  }

  @JSExportTopLevel("infoAccount")
  def infoAccount(container: String) = {
    Ajax.get("/api/infoaccount")
      .onComplete {
        case Success(xhr) =>
          println(xhr.responseText)

          def getCookie() : Map[String,String] = {

            Option(document.cookie).getOrElse("")
              .split("; ")
              .filter(t=>t.length > 0)
              .map(t=> (t, t.split("=")))
              .map(t=> (t._2(0), if (t._2.length > 1) t._1.substring(t._1.indexOf("=")+1) else null))
              .toMap
          }

          val login = toKeyValue(getCookie()("authcookie"))("username")
          println(login)

          val jsonXhr = decode[Json](xhr.responseText).right.get.asObject.get.toMap
          document.getElementById(container).innerHTML = if (jsonXhr.contains("passport")) {
              s"""
                 |<p>Логин: ${login}</p>
                 |<p>ФИО: ${jsonXhr("fullName").asString.orNull}</p>
                 |<p>Паспорт: ${jsonXhr("passport").asString.orNull}</p>
                 |<p>Должность: ${jsonXhr("nameposition").asString.get}</p>
                 |<p>Оклад: ${jsonXhr("salary").asString.get}</p>
                 |<p>Заметка: ${jsonXhr("note").asString.orNull}</p>""".stripMargin
          } else {
            s"""
               |<p>Логин: ${login}
               |<p>Сумма средств: ${jsonXhr("amount").asNumber.get}</p>
               |""".stripMargin
          }
      }
  }

  @JSExportTopLevel("registrationEmployee")
  def registrationEmployee(login: String,
                           password: String,
                           fullName: String,
                           passport: String,
                           position: String,
                           containerAlert: String): Unit = {
    if (getInputSelection(position).isEmpty) {
      addFormError(containerAlert, "Выберите должность сотрудника")
    } else {
      Ajax.post("/registration/employee",
        s"login=$login" +
          s"&password=$password" +
          s"&fullName=${getInput(fullName)}" +
          s"&passport=${getInput(passport)}" +
          s"&position=${getInputSelection(position).get}")
        .onComplete {
          case Success(xhr) =>
            println(xhr.responseText)
            window.location.replace("/login")
          case Failure(_) =>
            addFormError(containerAlert, "Ошибка при регестрации")
        }
    }
  }

  def addPositionList(container: String): Unit = {
    Ajax.post("/api/position", "")
      .onComplete {
        case Success(xhr) =>
          decode[Array[Position]](xhr.responseText) match {
            case Right(positions) =>
              val positionsList = "<option selected>Выбор должности</option>\n" + positions.zipWithIndex.map(g => (g._1.nameposition, g._2)).map(x => s"<option value='${x._2}'>${x._1}</option>").mkString("\n")
              addForm(container, positionsList)
          }
        case Failure(exception) => println("addGoodsList error: " + exception)
      }
  }


  def getBodyEmployee(container: String, login: String, password: String, containerAlert: String): String = {
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
       |        </select>
       |        <button class="btn btn-primary" onClick="registrationEmployee(
       |          '${getInput(login)}',
       |          '${getInput(password)}',
       |          'fullname-input',
       |          'passport',
       |          'position-input',
       |          '$containerAlert')">Зарегистрироваться</button>
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
