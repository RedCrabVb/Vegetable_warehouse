package ru.vivt.webapp.utils

import org.scalajs.dom.document

trait AlertMessage {
  def alertMessage(message: String) = {
    s"""
       |<div class="alert alert-danger alert-dismissible fade show" role="alert">
       |    <strong>$message</strong>
       |    <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
       |</div>""".stripMargin
  }

  def addFormError(container: String, message: String) = {
    if (container.nonEmpty && document.getElementById(container) != null) {
      document.getElementById(container).innerHTML = alertMessage(message)
    }
  }
}
