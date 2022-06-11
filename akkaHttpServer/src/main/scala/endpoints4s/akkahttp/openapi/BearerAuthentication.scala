package endpoints4s.akkahttp.openapi

import akka.http.scaladsl.model.headers.{HttpCredentials, OAuth2BearerToken}
import endpoints4s.Tupler
import endpoints4s.algebra.Documentation
import endpoints4s.openapi.EndpointsWithCustomErrors
import endpoints4s.openapi.model.{SecurityRequirement, SecurityScheme}

/** Interpreter for [[endpoints4s.algebra.BasicAuthentication]] that produces
  * OpenAPI documentation.
  *
  * @group interpreters
  */
trait BearerAuthentication extends EndpointsWithCustomErrors with endpoints4s.extraAlgebra.BearerAuthentication {

  def basicAuthenticationSchemeName: String = "HttpBearer"

  override type Credentials = HttpCredentials

  private[endpoints4s] def authenticatedRequest[U, E, H, UE, HCred, Out](
      method: Method,
      url: Url[U],
      entity: RequestEntity[E],
      headers: RequestHeaders[H],
      requestDocs: Documentation
  )(implicit
      tuplerUE: Tupler.Aux[U, E, UE],
      tuplerHCred: Tupler.Aux[H, Credentials, HCred],
      tuplerUEHCred: Tupler.Aux[UE, HCred, Out]
  ): Request[Out] =
    request(
      method,
      url,
      entity,
      requestDocs,
      headers.xmap(h => tuplerHCred(h, OAuth2BearerToken("realm")))(t => tuplerHCred.unapply(t)._1)
    )(
      tuplerUE,
      tuplerUEHCred
    ) // Documentation about authentication is done below by overriding authenticatedEndpoint

  override def authenticatedEndpoint[U, E, R, H, UE, HCred, Out](
      method: Method,
      url: Url[U],
      response: Response[R],
      requestEntity: RequestEntity[E] = emptyRequest,
      requestHeaders: RequestHeaders[H] = emptyRequestHeaders,
      unauthenticatedDocs: Documentation = None,
      requestDocs: Documentation = None,
      endpointDocs: EndpointDocs = EndpointDocs()
  )(implicit
      tuplerUE: Tupler.Aux[U, E, UE],
      tuplerHCred: Tupler.Aux[H, Credentials, HCred],
      tuplerUEHCred: Tupler.Aux[UE, HCred, Out]
  ): Endpoint[Out, Option[R]] =
    super
      .authenticatedEndpoint(
        method,
        url,
        response,
        requestEntity,
        requestHeaders,
        unauthenticatedDocs,
        requestDocs,
        endpointDocs
      )(tuplerUE, tuplerHCred, tuplerUEHCred)
//      .withSecurityRequirements(
//        SecurityRequirement(
//          basicAuthenticationSchemeName,
//          SecurityScheme(`type` = "http", description = Some("Http Bearer Authentication"), name = Some("name"), in = Some("in"), scheme = Some("bearer"), bearerFormat = Some("JWT"))
//        )
//      )
}
