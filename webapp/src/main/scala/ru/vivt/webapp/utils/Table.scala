package ru.vivt.webapp.utils

trait Table {
  def simpleTable(header: Array[String], rows: Array[Array[String]]): String = {

    val headerHtml = {
      val col = (for (head <- header) yield s"<th scope='col'>$head</th>").mkString("\n")
      s"""
         |<thead>
         |  <tr>
         |    <th scope="col">#</th>
         |    $col
         |  </tr>
         |</thead>""".stripMargin
    }

    val rowHtml: String = {
      (for (row <- rows.zipWithIndex) yield {
        val rowsHtml = (for (r <- row._1) yield s"<td scope='row'>$r</td>").mkString("\n")
        s"""
           |<tr>
           |  <th scope="row">${row._2}</th>
           |  $rowsHtml
           |</tr>""".stripMargin
      }).mkString("\n")
    }

    s"""<table class="table">
       |   $headerHtml
       |  <tbody>
       |     $rowHtml
       |  </tbody>
       |</table>""".stripMargin
  }
}
