package endpoints4s.akkahttp.client

import akka.http.scaladsl.model.headers._
import endpoints4s.Tupler
import endpoints4s.algebra.Documentation

/** @group interpreters
  */
trait BearerAuthentication extends endpoints4s.extraAlgebra.BearerAuthentication {
  self: EndpointsWithCustomErrors =>

  def fromCredentials(credentials: Credentials): Authorization

  override def responseWithStatusCode(
      responses: Function[StatusCode, Response[ERROR_RESPONSE]]
  ): Response[ERROR_RESPONSE] = { (status, headers) =>
    responses(status)(status, headers)
  }

  override def intFromStatusCode(s: StatusCode): Int = s.intValue()

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
    val basicAuthenticationHeader: RequestHeaders[Credentials] =
      (credentials, headers) => {
        headers :+ fromCredentials(credentials)
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
