package com.rick.wechat.facade

import scala.scalajs.js
import scala.scalajs.js.annotation.JSGlobal

@js.native
@JSGlobal
object console extends js.Any {
  def debug(any: Any*): Unit = js.native

  def log(any: Any*): Unit = js.native

  def info(any: Any*): Unit = js.native

  def warn(any: Any*): Unit = js.native

  def error(any: Any*): Unit = js.native

  def group(label: String): Unit = js.native

  def groupEnd: Unit = js.native
}
