package com.rick.wechat.future

import scala.scalajs.js

@js.native
trait DeviceMotion extends js.Object {
  val alpha: js.UndefOr[Double] = js.native
  val beta: js.UndefOr[Double] = js.native
  val gamma: js.UndefOr[Double] = js.native
}
private class Interval(val interval: js.UndefOr[String]) extends Callback[Unit]
