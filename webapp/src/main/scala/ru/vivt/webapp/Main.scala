package ru.vivt.webapp

import org.scalajs.dom
import org.scalajs.dom.{document, window}
import org.scalajs.dom.raw.Element
import scalatags.JsDom.all._

import scala.scalajs.js.annotation.JSExportTopLevel
object Main extends App {
 new AnyPage().process()

 @JSExportTopLevel("logout")
 def logout() = {
  window.location.replace("/logout")
 }
}
