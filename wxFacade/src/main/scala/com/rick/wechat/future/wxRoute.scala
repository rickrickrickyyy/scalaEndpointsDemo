package com.rick.wechat.future

import scala.scalajs.js
import scala.scalajs.js.UndefOr
//TODO:
class EventChannel()

//private class RouteCallback[T <: js.Object](path: String, data: Option[T] = None)
//  extends navigateToCallBack[T, Unit](path, data)

private class navigateToCallBack[T <: js.Object, EventChannel](path: String, data: Option[T] = None) extends Callback[EventChannel] with Url {
  override val url: UndefOr[String] = js.defined {
    path + data.map(query).getOrElse("")
  }
}

private class navigateBack(val delta: UndefOr[Int] = js.undefined) extends Callback[Unit]
