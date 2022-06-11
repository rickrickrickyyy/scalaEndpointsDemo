package com.rick.ui

import com.rick.wechat.facade._

import scala.scalajs.js.annotation.{JSExport, JSExportTopLevel}

@JSExportTopLevel("Pages")
object Pages {

  @JSExport
  def indexPage(): Unit = {
    val debug = new IndexPage()
    Page(debug)
  }
}
