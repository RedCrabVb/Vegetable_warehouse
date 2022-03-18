package ru.vivt.webapp.utils

trait AlertMessage {
  def alertMessage(message: String) = {
    s"""
       |<div class="alert alert-warning alert-dismissible fade show" role="alert">
       |    <strong>$message</strong>
       |    <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
       |</div>""".stripMargin
  }
}
