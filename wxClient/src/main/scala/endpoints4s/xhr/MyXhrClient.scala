package endpoints4s.xhr

import endpoints4s.extraAlgebra.{ApiSchemas, CustomErrors, MyEntity, RestfulClient}
import endpoints4s.xhr

class MyXhrClient(override val settings: EndpointsSettings)
    extends future.EndpointsWithCustomErrors
    with RestfulClient[MyEntity]
    with BearerAuthentication
    with xhr.JsonEntitiesFromSchemas
    with CustomErrors
    with ApiSchemas {
  override def credentialToHeader(string: String): String = string

  override val entityName: String = "MyEntity"

  override implicit val tSchema: JsonSchema[MyEntity] = myEntitySchema
}
