package com.rick.wechat.future

import scala.scalajs.js
import scala.scalajs.js.UndefOr

private class wxWebSocket(
    val url: String,
    val header: js.UndefOr[js.Dictionary[String]] = js.undefined,
    val protocols: js.UndefOr[js.Array[String]] = js.undefined,
    val tcpNoDelay: js.UndefOr[Boolean] = js.undefined,
    val perMessageDeflate: js.UndefOr[Boolean] = js.undefined,
    val timeout: js.UndefOr[Long] = js.undefined,
    val language: js.UndefOr[String] = js.undefined
) extends Callback[js.Object]

private class CloseSocket(override val code: UndefOr[String] = js.undefined, override val reason: UndefOr[String] = js.undefined) extends Callback[Unit] with Code with Reason

private class SocketClosed(override val code: UndefOr[String] = js.undefined, override val reason: UndefOr[String] = js.undefined) extends Code with Reason

private class TextMessage(override val data: UndefOr[String] = js.undefined) extends Callback[Unit] with Data[String]

private class BinaryMessage(override val data: UndefOr[js.typedarray.ArrayBuffer] = js.undefined) extends Callback[Unit] with Data[js.typedarray.ArrayBuffer]

//@js.native
//@JSGlobal("wx")
//object SocketTask extends js.Object {
//  def sendSocketMessage(o: js.Object): Unit = js.native
//
//  def closeSocket(o: js.Object): Unit = js.native
//
//  def onSocketOpen(callback: js.Function1[Header with Profile, Unit]): Unit = js.native
//
//  def onSocketClose(callback: js.Function1[Reason with Code, Unit]): Unit = js.native
//
//  def onSocketError(callback: js.Function1[ErrMsg, Unit]): Unit = js.native
//
//  def onSocketMessage(callback: js.Function1[Data[String | js.typedarray.ArrayBuffer], Unit]): Unit = js.native
//}
//object SocketTaskMethod {
//  def sendSocketMsg(o: BinaryMessage) = SocketTask.sendSocketMessage(o)
//
//  def sendSocketMsg(o: TextMessage) = SocketTask.sendSocketMessage(o)
//}
