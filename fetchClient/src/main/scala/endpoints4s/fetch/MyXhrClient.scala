package endpoints4s.fetch

import endpoints4s.extraAlgebra.{ApiSchemas, CustomErrors, MyEntity, RestfulClient}

class MyXhrClient(override val settings: EndpointsSettings, override val entityName: String = "MyEntity")
    extends endpoints4s.fetch.future.EndpointsWithCustomErrors
    with RestfulClient[MyEntity]
    with ApiSchemas
    with BearerAuthentication
    with CustomErrors
    with JsonEntitiesFromSchemas {

  override type Credentials = String

  override def fromCredentials(credentials: String): String = credentials

  override implicit val tSchema: JsonSchema[MyEntity] = myEntitySchema
}
