package endpoints4s.akkahttp.client

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.headers.Authorization
import akka.http.scaladsl.model.{Uri, headers}
import akka.stream.Materializer
import buildinfo.BuildInfo
import endpoints4s.extraAlgebra.{ApiSchemas, CustomErrors, MyEntity, RestfulClient}

import scala.concurrent.duration._
import scala.concurrent.{ExecutionContext, ExecutionContextExecutor}

class MyJvmApiClient(override val settings: EndpointsSettings)(implicit
    val system: ActorSystem,
    override val EC: ExecutionContext,
    override val M: Materializer,
    override val entityName: String = "MyEntity"
) extends RestfulClient[MyEntity]
    with EndpointsWithCustomErrors
    with ApiSchemas
    with BearerAuthentication
    with JsonEntitiesFromSchemas
    with CustomErrors {
  override def fromCredentials(credentials: Credentials): Authorization = Authorization(headers.OAuth2BearerToken(credentials))

  override type Credentials = String

  override implicit val tSchema: JsonSchema[MyEntity] = myEntitySchema
}

object MyJvmApiClient {
  def main(args: Array[String]): Unit = {
    implicit val system: ActorSystem = ActorSystem("MigrationSystem")
    implicit val EC: ExecutionContextExecutor = system.dispatcher
    implicit val M: Materializer = Materializer(system)

    val baseUrl = Uri(BuildInfo.BASE_URL);
    val address = baseUrl.authority.host.address
    val port = if (baseUrl.authority.port == 0) {
      80
    } else {
      baseUrl.authority.port
    }

    val executor: AkkaHttpRequestExecutor = AkkaHttpRequestExecutor.default({
      Http()
        .cachedHostConnectionPool(address, port)
    })
    val settings: EndpointsSettings = EndpointsSettings(executor, toStrictTimeout = 30.seconds)
    lazy val client = new MyJvmApiClient(settings)(system, EC, M)
    val f: PartialFunction[Either[client.MULTIPLE_RESPONSE[MyEntity], client.ERROR_RESPONSE], Unit] = {
      case Left(value)  => System.out.println("right: " + value.data)
      case Right(value) => System.out.println("left: " + value.toString)
    }

    val result = (for {
      a <- client.getItemsImpl()(((1, 2), None, None, None, "cr"))
      b <- client.getItemsImpl()(((1, 2), None, None, None, "cr"))
      c <- client.getItemsImpl()(((1, 2), None, None, None, "cr"))
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
