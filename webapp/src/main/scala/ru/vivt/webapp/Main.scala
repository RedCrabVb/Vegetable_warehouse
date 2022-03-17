package ru.vivt.webapp

import org.scalajs.dom
import org.scalajs.dom.document
import org.scalajs.dom.raw.Element
import scalatags.JsDom.all._
object Main extends App {
  def appendPar(targetNode: dom.Node, text: String): Unit = {
    val parNode = html(
      head(
        script("some script")
      ),
      body(
        h1("This is my title"),
        div(
          p("This is my first paragraph"),
          p("This is my second paragraph")
        ),
        button("test", (`class` := "btn btn-primary"))
      )
    ).render

//    val parNode: Element = document.createElement("p")
//    parNode.textContent = text
    targetNode.appendChild(parNode)
  }


  appendPar(document.body, "Hello scalajs")
}
