package ru.vivt.webapp.form

import org.scalajs.dom.document

import scala.scalajs.js.annotation.JSExportTopLevel

object Authorization {
  def addForm(container: String, getBody: String => String) = {
    val containerTag = document.getElementById(container)
    if (containerTag != null) {
      containerTag.innerHTML = getBody(container)
    }
  }
  @JSExportTopLevel("addFormAuthorization")
  def addFormAuthorization(container: String) = {
    addForm(container, getBodyLoginIn(_))
  }

  @JSExportTopLevel("addFormInputUser")
  def addFormInputUser(container: String) = {
    addForm(container, getBodyInputUser(_))
  }

  @JSExportTopLevel("addFormEmployee")
  def addFormEmployee(container: String, login: String, password: String) = {
    addForm(container, getBodyEmployee(_))
  }

  @JSExportTopLevel("sendData")
  def sendData(fields: Array[String]) = {
    println("sendData")
    //send data on registration
    //set status alert
  }

  def getBodyEmployee(container: String) = {
    s"""
       |<div form="$container">
       |        <div class="mb-3">
       |            <label for="exampleInputEmail1" class="form-label">Полное имя</label>
       |            <input type="text" name="username" class="form-control"
       |                   id="exampleInputEmail1"
       |                   aria-describedby="emailHelp">
       |        </div>
       |        <div class="mb-3">
       |            <label for="exampleInputPassword1" class="form-label">Данные паспорта</label>
       |            <input type="password" name="password" class="form-control"
       |                   id="exampleInputPassword1">
       |        </div>
       |        <select class="form-select mb-3" aria-label="Должность">
       |            <option selected>Выбор должности</option>
       |            <option value="1">Продовец</option>
       |            <option value="2">Администратор</option>
       |        </select>
       |        <button class="btn btn-primary" onClick="sendData()">Отправить</button>
       |    </div>
       |""".stripMargin
  }


  def getBodyInputUser(container: String) = {
    s"""
       |<div id="$container">
       |        <div class="mb-3">
       |            <label for="exampleInputEmail1" class="form-label">Имя
       |                пользователя</label>
       |            <input type="text" name="username" class="form-control"
       |                   id="exampleInputEmail1"
       |                   aria-describedby="emailHelp">
       |        </div>
       |        <div class="mb-3">
       |            <label for="exampleInputPassword1" class="form-label">Пароль</label>
       |            <input type="password" name="password" class="form-control"
       |                   id="exampleInputPassword1">
       |        </div>
       |        Я являюсь:
       |        <button class="btn btn-primary" onclick="addFormEmployee('$container', 'test', 'test')">сотрудник</button>
       |        <button class="btn btn-primary" onclick="sendData()">клиент</button>
       |    </div>
       |""".stripMargin
  }

  def getBodyLoginIn(container: String) = {
  s"""    <form action="/login" method="post" id="$container">
       |        <div class="mb-3">
       |            <label for="exampleInputEmail1" class="form-label">Имя пользователя</label>
       |            <input type="text" name="username" class="form-control"
       |                   id="exampleInputEmail1"
       |                   aria-describedby="emailHelp">
       |        </div>
       |        <div class="mb-3">
       |            <label for="exampleInputPassword1" class="form-label">Пароль</label>
       |            <input type="password" name="password" class="form-control"
       |                   id="exampleInputPassword1">
       |        </div>
       |        <div class="mb-3"><a href="#" onclick="addFormInputUser('$container')"">Зарегистрироваться</a></div>
       |        <button type="submit" class="btn btn-primary">Отправить</button>
       |    </form>""".stripMargin
  }
  }
