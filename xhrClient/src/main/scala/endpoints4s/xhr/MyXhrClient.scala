package endpoints4s.xhr

import endpoints4s.extraAlgebra.{ApiSchemas, CustomErrors, MyEntity, RestfulClient}
import org.scalajs.dom.window.btoa

class MyXhrClient(override val settings: EndpointsSettings, override val entityName: String = "MyEntity")
    extends endpoints4s.xhr.future.EndpointsWithCustomErrors
    with RestfulClient[MyEntity]
    with ApiSchemas
    with BearerAuthentication
    with CustomErrors
    with JsonEntitiesFromSchemas {

  override type Credentials = String

  override def fromCredentials(credentials: String): String = credentials

  override implicit val tSchema: JsonSchema[MyEntity] = myEntitySchema
}
