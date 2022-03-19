package ru.vivt.webapp.form

import io.circe.generic.auto._
import io.circe.jawn.decode
import org.scalajs.dom.document
import org.scalajs.dom.ext.Ajax
import ru.vivt.commons.Goods
import ru.vivt.webapp.utils.{ItemHtml, Table}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.scalajs.js.annotation.{JSExport, JSExportTopLevel}
import scala.util.{Failure, Success}

object GoodsForm extends Table with ItemHtml {

}
