package com.rick.wechat

import faithful.{Future, Promise}

import scala.scalajs.js
import scala.scalajs.js.Object.entries

package object future {

  implicit class FutureFactoryMethod[T](callback: Callback[T]) {
    def future(f: Callback[T] => Unit): Future[T] = {
      val promise = new Promise[T]()
      callback.success = js.defined {
        promise.success
      }
      callback.fail = js.defined { e =>
        promise.failure(new Throwable(e.errMsg.getOrElse("errMsg:undefined")))
      }
      try {
        f(callback)
      } catch {
        case e: Throwable =>
          promise.failure(e)
      }
      promise.future
    }
  }
  def query(o: js.Object): String =
    "?" + entries(o).map(e => s"${e._1}=${e._2}").mkString("&")

}
