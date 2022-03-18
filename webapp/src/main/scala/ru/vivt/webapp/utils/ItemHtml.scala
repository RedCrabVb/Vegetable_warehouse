package ru.vivt.webapp.utils

import org.scalajs.dom.{document, html}

trait ItemHtml {
  def getInput(name: String): String = {
    println("name: " + name)
    document.getElementById(name).asInstanceOf[html.Input].value
  }

  def getInputSelection(container: String): String = {
    val containerTag = document.getElementById(container).asInstanceOf[html.Select]
    val index: Int = containerTag.value.toInt;
    containerTag.options.apply(index).text;
  }

}
