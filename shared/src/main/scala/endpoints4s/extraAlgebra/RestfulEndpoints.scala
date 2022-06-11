package endpoints4s.extraAlgebra

import endpoints4s.algebra

trait RestfulEndpoints extends algebra.EndpointsWithCustomErrors with BearerAuthentication with CustomErrors with algebra.JsonEntitiesFromSchemas {
  protected val API_SEGMENT = "api"

  type SINGLE_RESPONSE[T]
  type MULTIPLE_RESPONSE[T]

  implicit def multipleResponseSchema[T](implicit tSchemas: JsonSchema[T]): JsonSchema[MULTIPLE_RESPONSE[T]]
  implicit def singleResponseSchema[T](implicit tSchemas: JsonSchema[T]): JsonSchema[SINGLE_RESPONSE[T]]

  def getItem[T](
      entityName: String
  )(implicit schema: JsonSchema[T]): Endpoint[(String, Credentials), Either[SINGLE_RESPONSE[T], ERROR_RESPONSE]] = {
    authenticatedEndpoint(
      Get,
      path / API_SEGMENT / entityName / remainingSegments(),
      ok(jsonResponse[SINGLE_RESPONSE[T]])
    )
  }

  def deleteItem[T](
      entityName: String
  )(implicit schema: JsonSchema[T]): Endpoint[(String, Credentials), Either[SINGLE_RESPONSE[T], ERROR_RESPONSE]] =
    authenticatedEndpoint(
      Delete,
      path / API_SEGMENT / entityName / remainingSegments(),
      ok(jsonResponse[SINGLE_RESPONSE[T]])
    )

  def updateItem[T](
      entityName: String
  )(implicit schema: JsonSchema[T]): Endpoint[(String, T, Credentials), Either[SINGLE_RESPONSE[T], ERROR_RESPONSE]] =
    authenticatedEndpoint(
      Put,
      path / API_SEGMENT / entityName / remainingSegments(),
      ok(jsonResponse[SINGLE_RESPONSE[T]]),
      jsonRequest[T](schema)
    )

  def upsertItem[T](
      entityName: String
  )(implicit schema: JsonSchema[T]): Endpoint[(String, T, Credentials), Either[SINGLE_RESPONSE[T], ERROR_RESPONSE]] =
    authenticatedEndpoint(
      Post,
      path / API_SEGMENT / entityName / remainingSegments(),
      ok(jsonResponse[SINGLE_RESPONSE[T]]),
      jsonRequest[T](schema)
    )

  def postItemNoId[T](
      entityName: String
  )(implicit schema: JsonSchema[T]): Endpoint[(T, Credentials), Either[SINGLE_RESPONSE[T], ERROR_RESPONSE]] =
    authenticatedEndpoint(
      Post,
      path / API_SEGMENT / entityName,
      ok(jsonResponse[SINGLE_RESPONSE[T]]),
      jsonRequest[T](schema)
    )

  def patchItemWithField[T, E](entityName: String, fieldName: String)(implicit
      schema: JsonSchema[T]
  ): Endpoint[(String, Map[String, String], Credentials), Either[SINGLE_RESPONSE[T], ERROR_RESPONSE]] =
    authenticatedEndpoint(
      Patch,
      path / API_SEGMENT / entityName / segment[String]() / fieldName,
      ok(jsonResponse[SINGLE_RESPONSE[T]]),
      jsonRequest[Map[String, String]]
    )

  def patchItem[T](
      entityName: String,
      fieldName: String
  )(implicit schema: JsonSchema[T]): Endpoint[(String, Credentials), Either[SINGLE_RESPONSE[T], ERROR_RESPONSE]] =
    authenticatedEndpoint(
      Patch,
      path / API_SEGMENT / entityName / segment[String]() / fieldName,
      ok(jsonResponse[SINGLE_RESPONSE[T]])
    )

  def clearItem[T](
      entityName: String
  )(implicit schema: JsonSchema[T]): Endpoint[Credentials, Either[MULTIPLE_RESPONSE[T], ERROR_RESPONSE]] =
    authenticatedEndpoint(
      Delete,
      path / API_SEGMENT / entityName,
      ok(jsonResponse[MULTIPLE_RESPONSE[T]])
    )

  def replaceItems[T](
      entityName: String
  )(implicit schema: JsonSchema[T]): Endpoint[(Seq[T], Credentials), Either[MULTIPLE_RESPONSE[T], ERROR_RESPONSE]] =
    authenticatedEndpoint(
      Put,
      path / API_SEGMENT / entityName,
      ok(jsonResponse[MULTIPLE_RESPONSE[T]]),
      jsonRequest[Seq[T]]
    )

  def updateItems[T](
      entityName: String
  )(implicit schema: JsonSchema[T]): Endpoint[(Seq[T], Credentials), Either[MULTIPLE_RESPONSE[T], ERROR_RESPONSE]] =
    authenticatedEndpoint(
      Patch,
      path / API_SEGMENT / entityName,
      ok(jsonResponse[MULTIPLE_RESPONSE[T]]),
      jsonRequest[Seq[T]]
    )

  def getItems[T, E](
      entityName: String,
      filters: QueryString[E]
  )(implicit
      schema: JsonSchema[T]
  ): Endpoint[(E, WithDefault[Int], WithDefault[String], WithDefault[Boolean], Credentials), Either[MULTIPLE_RESPONSE[T], ERROR_RESPONSE]] =
    authenticatedEndpoint(
      Get,
      path / API_SEGMENT / entityName /? (filters
        & optQsWithDefault[Int]("pageSize", 8)
        & optQsWithDefault[String]("orderBy", "id")
        & optQsWithDefault[Boolean]("isAsc", false)),
      ok(jsonResponse[MULTIPLE_RESPONSE[T]])
    )

}
