package endpoints4s.fetch

import buildinfo.BuildInfo
import com.rick.form.DomSyntax._
import com.rick.form.RenderDom._
import endpoints4s.extraAlgebra.{Command, MovingRemark}
import org.scalajs.dom
import scalatags.JsDom.all._

import scala.concurrent.ExecutionContext
import scala.scalajs.js
import scala.scalajs.js.{Promise, UndefOr, |}
import scala.util.{Failure, Success}
import rx._
object MyXhrApp {
  val mainApp: Unit = {
    implicit val ec: ExecutionContext = ExecutionContext.Implicits.global
    val settings = EndpointsSettings().withBaseUri(Some(BuildInfo.BASE_URL))

    val sClient = new RemarkClient(settings)
    val ticks = sClient.remarkEvents()("auth").future
    val remarkVar = Var(MovingRemark("", 0L))
    dom.document.body.appendChild(
      div(
        remarkVar.renderForm(),
        button(
          "点击发送虚拟弹幕(成功)",
          onclick := { () =>
            dom.document.body.appendChild(div("触发点击事件").render)
            sClient.postItemNoIdImpl()(MovingRemark("remark", System.currentTimeMillis()), "token").future.onComplete {
              case Failure(exception) => //ignored
              case Success(value) =>
                value match {
                  case Left(value)  => //ignored
                  case Right(value) => //ignored
                }
            }
          }
        ),
        button(
          "点击发送虚拟弹幕(客户端传参错误)",
          onclick := { () =>
            dom.document.body.appendChild(div("触发点击事件").render)
            sClient.postItemNoIdImpl()(MovingRemark("remark", 0), "token").future.onComplete {
              case Failure(exception) => //ignored
              case Success(value) =>
                value match {
                  case Left(value)  => //ignored
                  case Right(value) => //ignored
                }
            }
          }
        ),
        button(
          "点击发送虚拟弹幕(服务端返回自定义错误)",
          onclick := { () =>
            dom.document.body.appendChild(div("触发点击事件").render)
            sClient.postItemNoIdImpl()(MovingRemark("remark", 400001), "token").future.onComplete {
              case Failure(exception) => //ignored
              case Success(value) =>
                value match {
                  case Left(value)  => //ignored
                  case Right(value) => //ignored
                }
            }
          }
        ),
        button(
          "点击发送虚拟弹幕(服务端报错)",
          onclick := { () =>
            dom.document.body.appendChild(div("触发点击事件").render)
            sClient.postItemNoIdImpl()(Command.DONE, "token").future.onComplete {
              case Failure(exception) => //ignored
              case Success(value) =>
                value match {
                  case Left(value)  => //ignored
                  case Right(value) => //ignored
                }
            }
          }
        ),
        button(
          "点击连续发送两个请求",
          onclick := { () =>
            dom.document.body.appendChild(div("触发点击事件").render)
            for {
              a <- sClient.postItemNoIdImpl()(MovingRemark("remark", System.currentTimeMillis()), "token").future
              b <- sClient.postItemNoIdImpl()(MovingRemark("remark", System.currentTimeMillis()), "token").future
            } {
              a match {
                case Left(value)  =>
                case Right(value) =>
              }
              b match {
                case Left(value)  =>
                case Right(value) =>
              }
            }
          }
        ),
        button(
          "点击发送编辑框里面的内容",
          onclick := { () =>
            dom.document.body.appendChild(div("触发点击事件").render)
            for {
              b <- sClient.postItemNoIdImpl()(remarkVar.now, "token").future
            } {
              b match {
                case Left(value)  =>
                case Right(value) =>
              }
            }
          }
        )
      ).render
    )
    ticks.onComplete(
      {
        case Failure(exception) => System.out.println("failed: " + exception.getMessage)
        case Success(value) =>
          value match {
            case Left(value)  => read(value.getReader(), firstChunk = true): UndefOr[Promise[Unit]]
            case Right(value) => System.out.println("failed: " + value.toString)
          }

      }
    )
  }
  def main(args: Array[String]): Unit = {}

  def read[T](
      reader: dom.ReadableStreamReader[T],
      firstChunk: Boolean
  ): js.Promise[Unit] = {
    reader
      .read()
      .`then`[Unit](
        { chunk: dom.Chunk[T] =>
          if (chunk.done) {
            if (!firstChunk) {}
            (): Unit | js.Thenable[Unit]
          } else {

            dom.document.body.appendChild(div(chunk.value.toString).render)
            read(reader, firstChunk = false): Unit | js.Thenable[Unit]
          }
        },
        js.defined { e: Any =>
          System.out.println(e)
          (): Unit | js.Thenable[Unit]
        }
      )
  }

}
