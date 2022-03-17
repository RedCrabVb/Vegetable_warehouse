package ru.vivt.server

object CodeGen extends App {
  slick.codegen.SourceCodeGenerator.run(
    "slick.jdbc.PostgresProfile",
    "org.postgresql.Driver",
    "jdbc:postgresql://localhost:5432/vegetable_warehouse?user=postgres&password=password",
    "\\src\\main\\scala",
    "ru/vivt/server/models", None, None, true, false
  )
}
