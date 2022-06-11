package com.rick.wechat.facade

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{Future, Promise}
import scala.scalajs.js
import scala.scalajs.js.Dynamic.literal
import scala.util.{Failure, Success}

object Wechat {
  type Callback = () => Unit
  type ErrorCallback = (Throwable) => Unit
  implicit val callback: Callback = () => {}
  implicit val errorCallback: ErrorCallback = (e: Throwable) => { println(e) }

  def selectComponent(s: String): Component = {
    val current = WXGlobal.getCurrentPages().last
    current.selectComponent(s)
  }

  def selectAllComponents(s: String): js.Array[Component] = {
    val current = WXGlobal.getCurrentPages().last
    current.selectAllComponents(s)
  }

  def setData(key: String, data: Future[js.Dynamic])(implicit cb: ErrorCallback): Unit = {
    data.onComplete {
      case Success(i) => this.setData(literal(key -> i))
      case Failure(e) => cb(e)
    }
  }

  def setData(o: js.Object, f: Callback = callback): Unit = {
    val current = WXGlobal.getCurrentPages().last
    current.setData(o, f)
  }

  def login(cb: => Unit): Future[js.Dynamic] = {
    val p = Promise[js.Dynamic]()
    val scb = (ret: js.Dynamic) => p.success(ret)
    val fcb = () => p.failure(js.JavaScriptException("wx.login"))
    wxObject.login(literal(success = scb, fail = fcb, complete = () => cb))
    p.future
  }

  def getUserInfo(withCredentials: Boolean, lang: String)(cb: => Unit): Future[js.Dynamic] = {
    val p = Promise[js.Dynamic]()
    val scb = (ret: js.Dynamic) => p.success(ret.userInfo)
    val fcb = () => p.failure(js.JavaScriptException("wx.getUserInfo"))
    wxObject.getUserInfo(literal(withCredentials = withCredentials, lang = lang, success = scb, fail = fcb, complete = () => cb))
    p.future
  }

  def request(url: String, data: js.Dynamic, header: js.Dynamic, method: String)(cb: => Unit): Future[js.Dynamic] = {
    val p = Promise[js.Dynamic]()
    val scb = (ret: js.Dynamic) => p.success(ret)
    val fcb = () => p.failure(js.JavaScriptException("wx.request"))
    wxObject.request(literal(url = url, data = data, header = header, method = method, success = scb, fail = fcb, complete = () => cb))
    p.future
  }
}
