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

  def getInput(name: String): String = {
    document.getElementById(name).asInstanceOf[html.Input].value
  }

  def getInputSelection(container: String): String = {
    val containerTag = document.getElementById(container).asInstanceOf[html.Select]
    val index: Int = containerTag.value.toInt;
    containerTag.options.apply(index).text;
  }

}
