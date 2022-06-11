package endpoints4s.fetch

import endpoints4s.Tupler
import endpoints4s.algebra.Documentation

/** @group interpreters
  */
trait BearerAuthentication extends endpoints4s.extraAlgebra.BearerAuthentication with EndpointsWithCustomErrors {

  def fromCredentials(credentials: Credentials): String

  def responseWithStatusCode(responses: Function[StatusCode, Response[ERROR_RESPONSE]]): Response[ERROR_RESPONSE] = { fetch =>
    responses(fetch.status)(fetch)
  }

  override def intFromStatusCode(statusCode: Int): Int = statusCode

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
  ): Request[Out] = {
    val basicAuthenticationHeader: RequestHeaders[Credentials] = { (credentials, xhr) =>
      xhr.setRequestHeader(
        "Authorization",
        "Bearer " + fromCredentials(credentials)
      )
      ()
    }
    request(
      method,
      url,
      entity,
      requestDocs,
      headers ++ basicAuthenticationHeader
    )
  }

}
