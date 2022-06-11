package com.rick.wechat.future

import com.rick.wechat.facade.wxObject
import faithful.Future

import scala.scalajs.js
import scala.scalajs.js.typedarray.ArrayBuffer
import scala.scalajs.js.{UndefOr, |}

object wx {
  //wxRoute.scala
  def switchTab(path: String): Future[Unit] = new navigateToCallBack(path, None).future(wxObject.switchTab)

  def reLaunch[T <: js.Object](path: String, data: Option[T] = None): Future[Unit] =
    new navigateToCallBack(path, data).future(wxObject.reLaunch)

  def navigateTo[T <: js.Object](path: String, data: Option[T] = None): Future[js.Object] =
    new navigateToCallBack[T, js.Object](path, data).future(wxObject.navigateTo)

  def redirectTo[T <: js.Object](path: String, data: Option[T] = None): Future[js.Object] =
    new navigateToCallBack(path, data).future(wxObject.redirectTo)

  def navigateBack(delta: UndefOr[Int]): Future[Unit] = new navigateBack(delta).future(wxObject.navigateBack)

  //wxOpenApi.scala
  def login(): Future[Code] = new login().future(wxObject.login)

  def checkSession(): Future[Unit] = new checkSession().future(wxObject.checkSession)

  def navigateToMiniProgram[T](appId: String, path: UndefOr[String], extraData: UndefOr[T], envVersion: UndefOr[String]): Future[Unit] =
    new navigateToMiniProgram(appId, path, extraData, envVersion).future(wxObject.navigateToMiniProgram)

  def navigateBackMiniProgram[T](extraData: UndefOr[T]): Future[Unit] =
    new navigateBackMiniProgram(extraData).future(wxObject.navigateBackMiniProgram)

  def getUserInfo(withCredentials: UndefOr[Boolean] = js.undefined, lang: UndefOr[String] = js.undefined): Future[GetUserInfoResponse] =
    new getUserInfo(withCredentials, lang).future(wxObject.getUserInfo)

  //wxStorage.scala
  def clearStorage(): Future[ErrMsg] = new clearStorage().future(wxObject.clearStorage)

  def getStorageInfo(): Future[GetStorageInfoResult] = new getStorageInfo().future(wxObject.getStorageInfo)

  def removeStorage(key: UndefOr[String]): Future[ErrMsg] = new removeStorage(key).future(wxObject.removeStorage)

  def setStorage[T](key: UndefOr[String], data: UndefOr[T]): Future[ErrMsg] =
    new setStorage(key, data).future(wxObject.setStorage)

  def getStorage[T](key: UndefOr[String]): Future[GetStorageResult[T]] =
    new getStorage[T](key).future(wxObject.getStorage)

  def getSystemInfo(): Future[SystemInfo] = new Callback[SystemInfo].future(wxObject.getSystemInfo)

  def startDeviceMotionListening(interval: String): Future[Unit] =
    new Interval(js.defined { interval }).future(wxObject.startDeviceMotionListening)

  def stopDeviceMotionListening(): Future[Unit] =
    new Callback[Unit].future(wxObject.stopDeviceMotionListening)

  def previewImage(currentArg: String, urlsArg: js.Array[String], showmenuArg: Boolean = true): Future[Unit] =
    new Callback[Unit].future(_ =>
      wxObject.previewImage(new PreviewImage {
        override val current: UndefOr[String] = js.defined(currentArg)
        override val urls: UndefOr[js.Array[String]] = js.defined(urlsArg)
        override val showmenu: UndefOr[Boolean] = js.defined(showmenuArg)
      })
    )

  def getSetting(): Future[WxSettings] =
    new Callback[WxSettings].future(wxObject.getSetting)

  def connectSocket(
      url: String,
      header: js.UndefOr[js.Dictionary[String]] = js.undefined,
      protocols: js.UndefOr[js.Array[String]] = js.undefined,
      tcpNoDelay: js.UndefOr[Boolean] = js.undefined,
      perMessageDeflate: js.UndefOr[Boolean] = js.undefined,
      timeout: js.UndefOr[Long] = js.undefined,
      language: js.UndefOr[String] = js.undefined
  ): Future[js.Object] =
    new wxWebSocket(url, header, protocols, tcpNoDelay, perMessageDeflate, timeout, language).future(wxObject.connectSocket)

  def sendBinaryMsg(o: js.UndefOr[js.typedarray.ArrayBuffer]) =
    new BinaryMessage(o).future(wxObject.sendSocketMessage)
  //
  def sendTextMsg(o: js.UndefOr[String]) = new TextMessage(o).future(wxObject.sendSocketMessage)

  def closeSocket(code: UndefOr[String] = js.undefined, reason: UndefOr[String] = js.undefined) =
    new CloseSocket(code, reason).future(wxObject.sendSocketMessage)

  def onSocketClose(callback: js.Function1[SocketClosed, Unit]) =
    wxObject.onSocketClose(callback)

  def onSocketError(callback: js.Function1[ErrMsg, Unit]) =
    wxObject.onSocketError(callback)

  def onSocketOpen(callback: js.Function1[Header, Unit]) =
    wxObject.onSocketOpen(callback)

  def onSocketMessage(callback: js.Function1[Data[String | ArrayBuffer], Unit]) =
    wxObject.onSocketMessage(callback)

}
