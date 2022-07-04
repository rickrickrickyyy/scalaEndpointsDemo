package endpoints4s.akkahttp.client

import akka.actor.ActorSystem
import akka.http.scaladsl.model.headers
import akka.http.scaladsl.model.headers.Authorization
import akka.stream.Materializer
import endpoints4s.extraAlgebra.{ApiSchemas, CustomErrors, RemarkService}

import scala.concurrent.ExecutionContext

class RemarksClient(override val settings: EndpointsSettings)(implicit
    val system: ActorSystem,
    override val EC: ExecutionContext,
    override val M: Materializer
) extends EndpointsWithCustomErrors
    with ApiSchemas
    with CustomErrors
    with RemarkService
    with ChunkedJsonEntities
    with BearerAuthentication
    with JsonEntitiesFromSchemas {
  override def fromCredentials(credentials: Credentials): Authorization = Authorization(headers.OAuth2BearerToken(credentials))

  override type Credentials = String
}
