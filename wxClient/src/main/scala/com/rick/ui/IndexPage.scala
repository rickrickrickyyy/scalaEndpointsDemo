package com.rick.ui

import buildinfo.BuildInfo
import com.rick.wechat.facade._
import com.rick.wechat.future.{Data, PageLifeCycle}
import endpoints4s.extraAlgebra.MyEntity
import endpoints4s.xhr.{EndpointsSettings, MyXhrClient}

import java.util.concurrent.atomic.AtomicBoolean
import scala.concurrent.ExecutionContext
import scala.scalajs.js
import scala.scalajs.js.{ThisFunction1, UndefOr}
import scala.util.{Failure, Success}

class IndexPage extends js.Object with PageLifeCycle[IndexData] with Data[IndexData] {
  var image: Option[String] = None
  var shoeId: Option[String] = None
  val isUploadingImage: AtomicBoolean = new AtomicBoolean(false)

  override val data: UndefOr[IndexData] = js.defined {
    new IndexData("defaultImage")
  }

  override val onLoad: UndefOr[ThisFunction1[Page, IndexData, _]] = js.defined { (page, data) =>
    image = data.image.toOption
    implicit val ec: ExecutionContext = ExecutionContext.Implicits.global
    val settings = EndpointsSettings().withBaseUri(Some(BuildInfo.BASE_URL))

    val client = new MyXhrClient(settings)
    client
      .getItemsImpl()(((1, 1), Option(8), Option("fff"), Option(true), "cren"))
      .future
      .onComplete(
        {
          case Failure(exception) => System.out.println("failed: " + exception.getMessage)
          case Success(value) =>
            value match {
              case Left(value)  => System.out.println("right: " + value.data.mkString(","))
              case Right(value) => System.out.println("left: " + value.toString)
            }
        }
      )
    page.setData(data, Wechat.callback)
  }
}
