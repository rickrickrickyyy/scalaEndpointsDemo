package endpoints4s.extraAlgebra

import endpoints4s.Tupler
import endpoints4s.algebra.{Documentation, EndpointsWithCustomErrors, Responses}

/** Provides vocabulary to describe endpoints that use Basic HTTP authentication.
  *
  * This trait works fine, but developers are likely to implement their own
  * authentication mechanism, specific to their application.
  *
  * @group algebras
  */
trait BearerAuthentication extends EndpointsWithCustomErrors with Responses with ErrorSchema {
  type Credentials

  type ERROR_RESPONSE = Error[StatusCode]

  def responseWithStatusCode(
      responses: Function[StatusCode, Response[ERROR_RESPONSE]]
  ): Response[ERROR_RESPONSE]

  /** A response that can either be Forbidden (403) or the given `Response[A]`.
    *
    * The returned `Response[Option[A]]` signals “forbidden” with a `None` value.
    *
    * @param responseA Inner response (in case the authentication succeeds)
    * @param docs Description of the authentication error
    */
  private[endpoints4s] final def authenticated[A](
      responseA: Response[A],
      docs: Documentation = None
  ): Response[Either[A, ERROR_RESPONSE]] = {
    responseA.orElse(responseWithStatusCode(statusCode => {
      response(statusCode, jsonResponse[ERROR_RESPONSE], docs)
    }))
  }

  /** A request with the given `method`, `url`, `entity` and `headers`, but
    * which also contains the Basic Authentication credentials in its
    * “Authorization” header.
    *
    * The `Out` type aggregates together the URL information `U`, the entity
    * information `E`, the headers information `H`, and the `Credentials`.
    *
    * In case the authentication credentials are missing from the request,
    * servers reject the request with an Unauthorized (401) status code.
    */
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
  ): Request[Out]

  /** Describes an endpoint protected by Basic HTTP authentication
    * @group operations
    */
  def authenticatedEndpoint[U, E, R, H, UE, HCred, Out](
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
  ): Endpoint[Out, Either[R, ERROR_RESPONSE]] =
    endpoint(
      authenticatedRequest(
        method,
        url,
        requestEntity,
        requestHeaders,
        requestDocs
      ),
      authenticated(response, unauthenticatedDocs),
      endpointDocs
    )

}
