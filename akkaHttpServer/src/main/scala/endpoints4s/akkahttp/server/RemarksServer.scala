package endpoints4s.akkahttp.server

import akka.NotUsed
import akka.actor.typed.{ActorRef, ActorSystem}
import akka.http.scaladsl.model.{StatusCode, StatusCodes}
import akka.http.scaladsl.server.Route
import akka.stream.scaladsl.Source
import akka.stream.typed.scaladsl.ActorSource
import akka.stream.{Materializer, OverflowStrategy}
import endpoints4s.akkahttp.server
import endpoints4s.extraAlgebra._

import scala.concurrent.duration.DurationInt
import scala.concurrent.{ExecutionContext, Future}

class RemarksServer(implicit val actorSystem: ActorSystem[Nothing], implicit val EC: ExecutionContext, implicit val M: Materializer)
    extends RestfulClient[Command]
    with ApiSchemas
    with server.BearerAuthentication
    with server.EndpointsWithCustomErrors
    with CustomErrors
    with RemarkService
    with server.ChunkedJsonEntities
    with server.JsonEntitiesFromSchemas {
  val bufferSize = 50000

  val value: (ActorRef[Command], Source[Command, NotUsed]) = ActorSource
    .actorRef[Command](
      completionMatcher = PartialFunction.empty,
      failureMatcher = PartialFunction.empty[Command, Throwable],
      bufferSize = bufferSize,
      overflowStrategy = OverflowStrategy.dropHead
    )
    .buffer(50000, OverflowStrategy.backpressure)
    .keepAlive[Command](3.seconds, () => Command.HEART_BEAT)
    .preMaterialize()

  val ref: Option[ActorRef[Command]] = Option(value._1)
  val source: Option[Source[Command, NotUsed]] = Option(value._2)
  source.foreach(_.run())

  override type Credentials = String

  override def toCredentials(token: String): String = token

  def eventsImpl(): Route = {
    remarkEvents().implementedBy({ _ =>
      Left(source.getOrElse(Source.empty))
    })
  }

  def postItemImpl(): Route = {
    postItemNoIdImpl().implementedByAsync({ e =>
      Future(
        e._1 match {
          case d: Done => {
            throw new UnsupportedOperationException("Done msg is not supported")
          }
          case MovingRemark(remark, timeStamp) if (timeStamp == 400001) => {
            Right(Error(StatusCodes.custom(499, "Custom Error"), Seq("自定义错误")))
          }
          case _ => {
            ref.foreach(r => r ! e._1)
            Left(JsonItemMsg[Command](e._1))
          }
        }
      )
    })
  }

}
