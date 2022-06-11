package endpoints4s.xhr

import endpoints4s.Tupler
import endpoints4s.algebra.Documentation

/** @group interpreters
  */
trait BearerAuthentication extends endpoints4s.extraAlgebra.BearerAuthentication with EndpointsWithCustomErrors {

  override type Credentials = String

  def credentialToHeader(string: Credentials): String

  def responseWithStatusCode(responses: Function[StatusCode, Response[ERROR_RESPONSE]]): Response[ERROR_RESPONSE] = { request =>
    responses(request.status)(request)
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
        "Bearer" + credentialToHeader(credentials)
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
