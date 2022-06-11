/** All documentation for facades is thanks to Mozilla Contributors at https://developer.mozilla.org/en-US/docs/Web/API
  * and available under the Creative Commons Attribution-ShareAlike v2.5 or later.
  * http://creativecommons.org/licenses/by-sa/2.5/
  *
  * Everything else is under the MIT License http://opensource.org/licenses/MIT
  */
package endpoints4s.xhr

import scala.scalajs.js
import scala.scalajs.js.annotation.JSGlobal

class XMLHttpRequest {

  var url: String = ""
  var header: js.Dictionary[String] = js.Dictionary.empty[String]
  var timeout: Long = 30000
  var method: String = "GET"
  var dataType: String = "text"
  var responseType: String = "text"
  var enableHttp2: Boolean = false
  var enableQuic: Boolean = false
  var enableCache: Boolean = false
  var success: js.Function1[WXHttpResponse, _] = { response: WXHttpResponse =>
    this.response = Some(response)
    onload()
  }
  val fail: js.Function1[ErrMsg, _] = { e =>
    onerror()
  }
  var data: Option[js.Any] = _

  var requestTask: Option[RequestTask] = None
  var response: Option[WXHttpResponse] = None

  def status: Int = response.fold(404)(_.statusCode)
  def responseText: String = response.fold("")(_.data)
  def getResponseHeader(header: String): String = response.fold("")(_.header.getOrElse(header, ""))

  var onload: js.Function1[Any, _] = { _ => {} }
  var onerror: js.Function1[Any, _] = { _ => {} }

  def open(method: String, url: String, async: Boolean = false, user: String = "", password: String = ""): Unit = {
    this.method = method;
    this.url = url;
  }

  def send(data: js.Any): Unit = {
    this.data = Some(data)
    requestTask = Some(wxHttp.request(requestEntity()))
  }

  def setRequestHeader(header: String, value: String): Unit = {
    this.header.put(header, value)
  }

  def abort(): Unit = {
    requestTask.fold({})({ e => e.abort() })
  }

  def requestEntity(): scalajs.js.Dynamic = {
    scalajs.js.Dynamic.literal(
      url = url,
      data = data.getOrElse(""),
      header = header,
      timeout = timeout,
      method = method,
      dataType = dataType,
      responseType = responseType,
      enableHttp2 = enableHttp2,
      enableQuic = enableQuic,
      enableCache = enableCache,
      success = success,
      fail = fail,
      complete = () => ()
    )
  }
}

trait ErrMsg extends js.Object {
  val errMsg: js.UndefOr[String] = js.undefined
}

@js.native
trait WXHttpResponse extends js.Object {
  val data: String = js.native
  val statusCode: Int = js.native
  val header: js.Dictionary[String] = js.native
  val cookies: js.Array[String] = js.native
  val profile: js.Dictionary[String] = js.native
}

@js.native
@JSGlobal("wx")
object wxHttp extends js.Object {
  def request(d: js.Dynamic): RequestTask = js.native

  def uploadFile(d: js.Dynamic): Unit = js.native

  def downloadFile(d: js.Dynamic): Unit = js.native
}

@js.native
@JSGlobal
class RequestTask extends js.Object {
  def abort(): Unit = js.native
}
