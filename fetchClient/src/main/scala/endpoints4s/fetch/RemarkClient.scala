package endpoints4s.fetch

import endpoints4s.extraAlgebra.{ApiSchemas, Command, RemarkService}

class RemarkClient(override val settings: EndpointsSettings)
    extends endpoints4s.fetch.future.EndpointsWithCustomErrors
    with RemarkService
    with ApiSchemas
    with ChunkedJsonResponseEntities
    with BearerAuthentication
    with JsonEntitiesFromSchemas {
  override def fromCredentials(credentials: Credentials): String = credentials

  override type Credentials = String

}
