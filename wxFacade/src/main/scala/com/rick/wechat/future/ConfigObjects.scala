package com.rick.wechat.future

import com.rick.wechat.facade.Page
import faithful.Future

import scala.scalajs.js

trait Success[T] extends js.Object {
  var success: js.UndefOr[js.Function1[T, _]] = js.undefined
}

trait Fail extends js.Object {
  var fail: js.UndefOr[js.Function1[ErrMsg, _]] = js.undefined
}

trait Complete extends js.Object {
  val complete: js.UndefOr[js.Function0[_]] = js.undefined
}

//TODO: T <: js.Any    ? 不知道需要这样限制不
private class Callback[T] extends Success[T] with Fail

trait Key extends js.Object {
  val key: js.UndefOr[String] = js.undefined
}

trait Data[T] extends js.Object {
  val data: js.UndefOr[T] = js.undefined
}

trait Code extends js.Object {
  val code: js.UndefOr[String] = js.undefined
}

trait Reason extends js.Object {
  val reason: js.UndefOr[String] = js.undefined
}

trait Header extends js.Object {
  val header: js.UndefOr[js.Dictionary[String]] = js.undefined
}

trait Profile extends js.Object {
  val profile: js.UndefOr[js.Object] = js.undefined
}

trait Url extends js.Object {
  val url: js.UndefOr[String] = js.undefined
}

trait Lang extends js.Object {
  val lang: js.UndefOr[String] = js.undefined
}

trait ExtraData[T] extends js.Object {
  val extraData: js.UndefOr[T] = js.undefined
}

trait Timeout extends js.Object {
  val timeout: js.UndefOr[Long] = js.undefined
}

trait ErrMsg extends js.Object {
  val errMsg: js.UndefOr[String] = js.undefined
}

trait PageLifeCycle[T] extends js.Object {
  val onLoad: js.UndefOr[js.ThisFunction1[Page, T, _]] = js.undefined

  val onShow: js.UndefOr[js.ThisFunction0[Page, _]] = js.undefined

  val onReady: js.UndefOr[js.ThisFunction0[Page, _]] = js.undefined

  val onHide: js.UndefOr[js.ThisFunction0[Page, _]] = js.undefined

  val onUnload: js.UndefOr[js.ThisFunction0[Page, _]] = js.undefined
}

trait onPullDownRefresh extends js.Object {
  val onPullDownRefresh: js.UndefOr[js.ThisFunction0[Page, _]] = js.undefined
}

trait onReachBottom extends js.Object {
  val onReachBottom: js.UndefOr[js.ThisFunction0[Page, _]] = js.undefined
}

trait onPageScrollEvent extends js.Object {
  val scrollTop: js.UndefOr[Int] = js.undefined
}

trait onPageScroll extends js.Object {
  val onPageScroll: js.UndefOr[js.ThisFunction1[Page, onPageScrollEvent, _]] = js.undefined
}

trait onAddToFavoritesEvent extends js.Object {
  val webViewUrl: js.UndefOr[String] = js.undefined
}

trait onAddToFavoritesResult extends js.Object {
  val title: js.UndefOr[String] = js.undefined
  val imageUrl: js.UndefOr[String] = js.undefined
  val query: js.UndefOr[String] = js.undefined
}

trait onAddToFavorites extends js.Object {
  val onAddToFavorites: js.UndefOr[js.ThisFunction1[Page, onAddToFavoritesEvent, onAddToFavoritesResult]] = js.undefined
}

trait onShareAppMessageEvent extends js.Object {
  val from: js.UndefOr[String] = js.undefined
  val target: js.UndefOr[js.Object] = js.undefined
  val webViewUrl: js.UndefOr[String] = js.undefined
}

trait onShareAppMessageResult extends js.Object {
  val title: js.UndefOr[String] = js.undefined
  val path: js.UndefOr[String] = js.undefined
}

trait onShareAppMessage extends js.Object {
  val onShareAppMessage: js.UndefOr[js.ThisFunction1[Page, onShareAppMessageEvent, onShareAppMessageResult]] =
    js.undefined
}

trait onShareTimelineResult extends js.Object {
  val title: js.UndefOr[String] = js.undefined
  val query: js.UndefOr[String] = js.undefined
  val imageUrl: js.UndefOr[String] = js.undefined
}

trait onShareTimeline extends js.Object {
  val onShareTimeline: js.UndefOr[js.ThisFunction0[Page, onShareTimelineResult]] = js.undefined
}

trait onResize extends js.Object {
  val onResize: js.UndefOr[js.ThisFunction1[Page, js.Object, _]] = js.undefined
}

@js.native
trait Rect extends js.Object {
  val left: js.UndefOr[Int] = js.native
  val top: js.UndefOr[Int] = js.native
  val width: js.UndefOr[Int] = js.native
  val height: js.UndefOr[Int] = js.native
}

@js.native
trait TabItem extends js.Object {
  val index: js.UndefOr[String] = js.native
  val pagePath: js.UndefOr[String] = js.native
  val text: js.UndefOr[String] = js.native
}

trait onTabItemTap extends js.Object {
  val onTabItemTap: js.UndefOr[js.ThisFunction1[Page, TabItem, _]] = js.undefined
}

@js.native
trait safeArea extends js.Object {
  val left: js.UndefOr[Int] = js.native
  val right: js.UndefOr[Int] = js.native
  val top: js.UndefOr[Int] = js.native
  val bottom: js.UndefOr[Int] = js.native
  val width: js.UndefOr[Int] = js.native
  val height: js.UndefOr[Int] = js.native
}

@js.native
trait SystemInfo extends js.Object {
  val brand: js.UndefOr[String] = js.native
  val model: js.UndefOr[String] = js.native
  val language: js.UndefOr[String] = js.native
  val version: js.UndefOr[String] = js.native
  val system: js.UndefOr[String] = js.native
  val platform: js.UndefOr[String] = js.native
  val SDKVersion: js.UndefOr[String] = js.native
  val theme: js.UndefOr[String] = js.native

  val pixelRatio: js.UndefOr[Double] = js.native
  val screenWidth: js.UndefOr[Int] = js.native
  val screenHeight: js.UndefOr[Int] = js.native
  val windowWidth: js.UndefOr[Int] = js.native
  val windowHeight: js.UndefOr[Int] = js.native
  val statusBarHeight: js.UndefOr[Int] = js.native
  val fontSizeSetting: js.UndefOr[Int] = js.native
  val benchmarkLevel: js.UndefOr[Int] = js.native

  val albumAuthorized: js.UndefOr[Int] = js.native
  val cameraAuthorized: js.UndefOr[Int] = js.native
  val locationAuthorized: js.UndefOr[Int] = js.native
  val microphoneAuthorized: js.UndefOr[Int] = js.native
  val notificationAuthorized: js.UndefOr[Int] = js.native
  val notificationAlertAuthorized: js.UndefOr[Int] = js.native
  val notificationBadgeAuthorized: js.UndefOr[Int] = js.native
  val notificationSoundAuthorized: js.UndefOr[Int] = js.native
  val bluetoothEnabled: js.UndefOr[Int] = js.native
  val locationEnabled: js.UndefOr[Int] = js.native
  val wifiEnabled: js.UndefOr[Int] = js.native
  val locationReducedAccuracy: js.UndefOr[Int] = js.native

  val safeArea: js.UndefOr[Int] = js.native

}
//TODO:把这部分的东西放到别的地方去
@js.native
trait CameraContext extends js.Object {
  def onCameraFrame(o: js.Function1[CameraFrame, Unit]): CameraFrameListener = js.native
  def takePhoto(o: js.Object): Unit = js.native
}
object CameraContext {
  def takePhoto(cameraContext: CameraContext, qualityArg: String): Future[TempImagePath] = {
    new TakePhoto(js.defined { qualityArg }).future(callback => cameraContext.takePhoto(callback))
  }
}

private class TakePhoto(val quality: js.UndefOr[String] = js.undefined) extends Callback[TempImagePath]

@js.native
trait TempImagePath extends js.Object {
  val tempImagePath: js.UndefOr[String] = js.native
}
@js.native
trait CameraFrame extends Data[js.typedarray.ArrayBuffer] {
  val width: js.UndefOr[Int] = js.native
  val height: js.UndefOr[Int] = js.native
}
@js.native
trait CameraFrameListener extends Data[js.typedarray.ArrayBuffer] {
  def start(o: Callback[Unit]): Unit = js.native

  def stop(o: Callback[Unit]): Unit = js.native
}
@js.native
trait SelectorQuery extends js.Object {
  val select: js.Function1[String, NodesRef] = js.native
  val exec: js.Function1[js.Function1[js.Dynamic, js.Any], NodesRef] = js.native
}
@js.native
trait NodesRef extends js.Object {
//  val fields: js.Function1[NodeRefFieldsConfig, SelectorQuery] = js.native
  val fields: js.Function1[js.Object, js.Object] = js.native
  val node: js.Function1[js.Function1[js.Object, js.Any], SelectorQuery] = js.native
  //  val node: js.Function1[js.Function1[js.Object, js.Any], SelectorQuery] = js.native
}
class NodeRefFieldsConfig extends js.Object {
  val id: js.UndefOr[Boolean] = js.undefined
  val dataset: js.UndefOr[Boolean] = js.undefined
  val mark: js.UndefOr[Boolean] = js.undefined
  val rect: js.UndefOr[Boolean] = js.undefined
  val size: js.UndefOr[Boolean] = js.undefined
  val scrollOffset: js.UndefOr[Boolean] = js.undefined
  val properties: js.Array[String] = js.Array()
  val computedStyle: js.Array[String] = js.Array()
  val context: js.UndefOr[Boolean] = js.undefined
  val node: js.UndefOr[Boolean] = js.undefined
}

trait NodeRefFields extends js.Object {
  val id: js.UndefOr[String] = js.undefined
  val node: js.UndefOr[Boolean] = js.undefined
}

trait PreviewImage extends js.Object {
  val urls: js.UndefOr[js.Array[String]] = js.undefined
  val showmenu: js.UndefOr[Boolean] = js.undefined
  val current: js.UndefOr[String] = js.undefined
}

object WxScope extends js.Object {
  val userInfo = "scope.userInfo"

  val userLocation = "scope.userLocation"

  val address = "scope.address"

  val invoiceTitle = "scope.invoiceTitle"

  val invoice = "scope.invoice"

  val werun = "scope.werun"

  val record = "scope.record"

  val writePhotosAlbum = "scope.writePhotosAlbum"

  val camera = "scope.camera"
}
class SubscriptionsSetting extends js.Object {

//Boolean mainSwitch
//订阅消息总开关，true为开启，false为关闭
//
//Object itemSettings
}
trait WxSettings extends js.Object {
//  属性	类型	说明	最低版本
  val authSetting: js.UndefOr[js.Dynamic] = js.undefined
  val subscriptionsSetting: js.UndefOr[SubscriptionsSetting] = js.undefined
  val miniprogramAuthSetting: js.UndefOr[js.Dynamic] = js.undefined
}
