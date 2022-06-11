package endpoints4s.akkahttp.openapi

import endpoints4s.extraAlgebra.{ApiSchemas, CustomErrors, RestfulEndpoints}
import endpoints4s.openapi.{EndpointsWithCustomErrors, JsonEntitiesFromSchemas, Urls}

class MyApiDoc extends RestfulEndpoints with BearerAuthentication with EndpointsWithCustomErrors with JsonEntitiesFromSchemas with CustomErrors with ApiSchemas with Urls {

  def documentedEndpoint(): Seq[DocumentedEndpoint] =
    Seq(
      getItem("MyEntity")(myEntitySchema),
      deleteItem("MyEntity")(myEntitySchema),
      updateItem("MyEntity")(myEntitySchema),
      upsertItem("MyEntity")(myEntitySchema),
      postItemNoId("MyEntity")(myEntitySchema),
      patchItemWithField("MyEntity", "double")(myEntitySchema),
      clearItem("MyEntity")(myEntitySchema),
      replaceItems("MyEntity")(myEntitySchema),
      updateItems("MyEntity")(myEntitySchema),
      getItems("MyEntity", qs[Int]("what")(intQueryString) & qs[Int]("what2")(intQueryString))(myEntitySchema)
    )

  override def responseWithStatusCode(responses: Function[Int, List[DocumentedResponse]]): List[DocumentedResponse] =
    responses.apply(BadRequest)

  override def intFromStatusCode(statusCode: Int): Int = statusCode
}
