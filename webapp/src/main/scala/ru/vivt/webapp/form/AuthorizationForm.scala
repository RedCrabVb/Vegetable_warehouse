package ru.vivt.webapp.form

import org.scalajs.dom.ext.Ajax
import org.scalajs.dom.{document, window}
import ru.vivt.webapp.form.GoodsForm.addFormError
import ru.vivt.webapp.utils.ItemHtml

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
  }

  @JSExportTopLevel("authorization")
  def authorization(login: String, password: String, containerAlert: String): Unit = {
    Ajax.post("/login", s"username=${getInput(login)}&password=${getInput(password)}")
      .onComplete {
        case Success(xhr) =>
          println(xhr.responseText)
          window.location.replace("/app/main.html")
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
          window.location.replace("/app/login.html")
        case Failure(_) =>
          addFormError(containerAlert, "Ошибка при регестрации")
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
            window.location.replace("/app/login.html")
          case Failure(_) =>
            addFormError(containerAlert, "Ошибка при регестрации")
        }
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
       |            <option value="1">salesman</option>
       |            <option value="2">administrator</option>
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
