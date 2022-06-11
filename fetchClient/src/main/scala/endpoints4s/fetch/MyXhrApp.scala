package endpoints4s.fetch

import buildinfo.BuildInfo
import endpoints4s.extraAlgebra.MyEntity

import scala.concurrent.ExecutionContext
object MyXhrApp {
  def main(args: Array[String]): Unit = {
    implicit val ec: ExecutionContext = ExecutionContext.Implicits.global
    val settings = EndpointsSettings().withBaseUri(Some(BuildInfo.BASE_URL))

    val client = new MyXhrClient(settings, "MyEntity")

    val f: PartialFunction[Either[client.SINGLE_RESPONSE[MyEntity], client.ERROR_RESPONSE], Unit] = {
      case Left(value)  => System.out.println("right: " + value.data)
      case Right(value) => System.out.println("left: " + value.toString)
    }

    val result = (for {
      a <- client.getItemImpl()("bb", "ee").future
      b <- client.getItemImpl()("bb", "ee").future
      c <- client.getItemImpl()("bb", "ee").future
    } yield {
      f(a)
      f(b)
      f(c)

      (a, b, c)
    }).recover({ case e: Throwable =>
      e.printStackTrace()
      System.out.println(e.getMessage)
    })
  }

}
