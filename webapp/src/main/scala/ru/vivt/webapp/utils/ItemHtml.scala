package ru.vivt.webapp.utils

import org.scalajs.dom.{document, html}

trait ItemHtml {
  def addForm(container: String, getBody: String => String): Unit = {
    addForm(container, getBody(container))
  }

  def addForm(container: String, getBody: String): Unit = {
    val containerTag = document.getElementById(container)
    if (containerTag != null) {
      containerTag.innerHTML = getBody
    } else {
      println(s"containerTag $container not found")
    }
  }

  def getInput(id: String): String = {
    println("Get input: " + id)
    document.getElementById(id).asInstanceOf[html.Input].value
  }

  def getInputSelection(container: String): Option[String] = {
    val containerTag = document.getElementById(container).asInstanceOf[html.Select]
    Option(try {
      val index: Int = containerTag.value.toInt + 1
      containerTag.options.apply(index).text
    } catch {
      case e: NumberFormatException => null
    })
  }

  def getInputDataList(container: String): Option[String] = {
    val containerTag = document.getElementById(container).asInstanceOf[html.Input]
    Option(try {
      containerTag.value
    } catch {
      case e: NumberFormatException => null
    })
  }

}
