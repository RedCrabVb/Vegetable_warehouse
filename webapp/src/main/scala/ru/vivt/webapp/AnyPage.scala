package ru.vivt.webapp

import org.scalajs.dom.document
import ru.vivt.webapp.utils.{ItemHtml, NavigationBar}

class AnyPage extends NavigationBar with ItemHtml {
  def process(): Unit = {
    addForm("alert", getNavBar())
  }
}
