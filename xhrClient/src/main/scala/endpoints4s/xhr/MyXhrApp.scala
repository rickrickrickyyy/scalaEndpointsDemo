package endpoints4s.xhr

import buildinfo.BuildInfo
import endpoints4s.extraAlgebra.MyEntity

import scala.concurrent.ExecutionContext
import scala.util.{Failure, Success}
object MyXhrApp {
  def main(args: Array[String]): Unit = {
    implicit val ec: ExecutionContext = ExecutionContext.Implicits.global
    val settings = EndpointsSettings().withBaseUri(Some(BuildInfo.BASE_URL))

    val client = new MyXhrClient(settings, "MyEntity")

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
  }

}
