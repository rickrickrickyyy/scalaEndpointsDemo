package endpoints4s.akkahttp.server

import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.{ActorRefResolver, ActorSystem}
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Route
import akka.stream.Materializer
import com.typesafe.config.ConfigFactory
import endpoints4s.akkahttp.openapi.Documentations
import endpoints4s.akkahttp.server
import endpoints4s.extraAlgebra._

import java.nio.file.Paths
import scala.concurrent.{ExecutionContext, Future}

class MyApiServer
    extends RestfulClient[MyEntity]
    with ApiSchemas
    with server.BearerAuthentication
    with server.ChunkedJsonEntities
    with server.EndpointsWithCustomErrors
    with CustomErrors
    with server.JsonEntitiesFromSchemas {

  override val entityName: String = "MyEntity"

  override implicit val tSchema: JsonSchema[MyEntity] = myEntitySchema

  override type Credentials = String

  override def toCredentials(token: String): String = token

  def getItem(): Route = {
    getItemImpl().implementedByAsync((e: (String, String)) => throw new IllegalArgumentException(""))
  }
  def deleteItem(): Route = {
    deleteItemImpl().implementedByAsync((e: (String, String)) => throw new IllegalArgumentException(""))
  }
  def updateItem(): Route = {
    updateItemImpl().implementedByAsync((e: (String, MyEntity, String)) => throw new IllegalArgumentException(""))
  }
  def upsertItem(): Route = {
    upsertItemImpl().implementedByAsync((e: (String, MyEntity, String)) => throw new IllegalArgumentException(""))
  }
  def postItemNoId(): Route = {
    postItemNoIdImpl().implementedByAsync((e: (MyEntity, String)) => throw new IllegalArgumentException(""))
  }
  def patchItemWithField(): Route = {
    patchItemWithField("MyEntity", "double")(myEntitySchema).implementedByAsync((e: (String, Map[String, String], String)) => throw new IllegalArgumentException(""))
  }
  def clearItem(): Route = {
    clearItemImpl().implementedByAsync((e: String) => throw new IllegalArgumentException(""))
  }
  def replaceItems(): Route = {
    replaceItemsImpl().implementedByAsync((e: (Seq[MyEntity], String)) => throw new IllegalArgumentException(""))
  }
  def updateItems(): Route = {
    updateItemsImpl().implementedByAsync((e: (Seq[MyEntity], String)) => throw new IllegalArgumentException(""))
  }
  def getItems(): Route = {
    getItemsImpl().implementedByAsync((e) => Future.successful(Left(JsonItemsMsg(Seq(MyEntity("hhh", e._1._1, e._4)), isLastPage = false))))
  }

}

object MyApiServer {
  import akka.http.scaladsl.server.Directives._

  def main(args: Array[String]): Unit = {
    val config = ConfigFactory.load(ConfigFactory.defaultApplication())
    val isDebug = config.getBoolean("is-debug")

    val restfulHttpSystem = ActorSystem[Nothing](
      Behaviors.setup[Nothing](context => {
        implicit val actorSystem: ActorSystem[Nothing] = context.system
        implicit val EC: ExecutionContext = ExecutionContext.Implicits.global
        implicit val M: Materializer = Materializer(actorSystem)
        val server = new MyApiServer()
        Http()
          .newServerAt(interface = "0.0.0.0", port = 80)
          .bindFlow(
            server.getItem()
              ~ server.getItem
              ~ server.deleteItem
              ~ server.updateItem
              ~ server.upsertItem
              ~ server.postItemNoId
              ~ server.clearItem
              ~ server.replaceItems
              ~ server.updateItems
              ~ server.getItems
              ~ mainResources(isDebug)
              ~ Documentations.routes()
          )
        Behaviors.empty
      }),
      "RestfulHttpSystem"
    )
    Option(ActorRefResolver(restfulHttpSystem))
  }

  def mainResources(isDebug: Boolean): Route = {
    if (isDebug) {
      encodeResponse {
        getFromDirectory(Paths.get("akkaHttpServer/src/main/resources/assets").toAbsolutePath.toString)
      }
    } else {
      encodeResponse {
        getFromResourceDirectory("assets")
      }
    }
  }
}
