package ru.vivt.webapp.utils

trait NavigationBar {
  def getNavBar() = {
    s"""
       |        <nav class="navbar navbar-expand-lg navbar-light bg-light" >
       |        <div class="container-fluid">
       |            <a class="navbar-brand" href="/app/main.html">Овощной склад</a>
       |            <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarSupportedContent"
       |                    aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
       |                <span class="navbar-toggler-icon"></span>
       |            </button>
       |            <div class="collapse navbar-collapse" id="navbarSupportedContent">
       |                <ul class="navbar-nav me-auto mb-2 mb-lg-0">
       |                    <li class="nav-item active">
       |                        <a class="nav-link" href="/app/goods.html">Товары</a>
       |                    </li>
       |                    <li class="nav-item">
       |                        <a class="nav-link" href="/app/sellGoods.html">Продать товар</a>
       |                    </li>
       |                    <li class="nav-item">
       |                        <a class="nav-link" href="/app/clientInfo.html">Посмотреть информацию о клиентах</a>
       |                    </li>
       |                    <li class="nav-item">
       |                        <a class="nav-link" href="/app/employeeInfo.html">Посмотреть информацию о сотрудниках</a>
       |                    </li>
       |                    <li class="nav-item">
       |                        <a class="nav-link" href="/app/historySales.html">История операций</a>
       |                    </li>
       |                </ul>
       |                <div class="navbar-nav ms-auto mb-2 mb-lg-1" >
       |                    <button class="btn btn-outline-secondary" onclick="logout">Выход</button>
       |                </div>
       |            </div>
       |        </div>
       |        </nav>
       |        """.stripMargin
  }
}
