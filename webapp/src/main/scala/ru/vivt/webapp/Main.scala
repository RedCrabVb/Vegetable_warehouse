package ru.vivt.webapp

import org.scalajs.dom
import org.scalajs.dom.document

object Main extends App {
  def appendPar(targetNode: dom.Node, text: String): Unit = {
    val parNode = document.createElement("p")
    parNode.textContent = text
    targetNode.appendChild(parNode)
  }

  appendPar(document.body, "Hello scalajs")
}
