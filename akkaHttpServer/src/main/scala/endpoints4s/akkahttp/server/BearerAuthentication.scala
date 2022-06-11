package endpoints4s.akkahttp.server

import akka.http.scaladsl.model.headers.{Authorization, HttpChallenges, HttpCredentials, OAuth2BearerToken, `WWW-Authenticate`}
import akka.http.scaladsl.model.{HttpHeader, HttpResponse}
import akka.http.scaladsl.server.{Directive1, Directives}
import endpoints4s.algebra.Documentation
import endpoints4s.{Tupler, Valid, Validated}

trait BearerAuthentication extends endpoints4s.extraAlgebra.BearerAuthentication with EndpointsWithCustomErrors {

  override type Credentials

  def toCredentials(token: String): Credentials

  override def intFromStatusCode(s: StatusCode): Int = s.intValue()

  def responseWithStatusCode(
      responses: Function[StatusCode, Response[ERROR_RESPONSE]]
  ): Response[ERROR_RESPONSE] = { e =>
    responses(e.statusCode).apply(e)
  }

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
    val (m, u, e, h) = (method, url, entity, headers)
    new Request[Out] {
      type UrlData = U
      type HeadersData = (H, Option[Credentials])
      type EntityData = E

      def url: Url[UrlData] = u
      def method: Method = m
      def entity: RequestEntity[EntityData] = e
      def headers: RequestHeaders[HeadersData] = h ++ authHeader

      private[server] def aggregateAndValidate(
          urlData: UrlData,
          entityData: EntityData,
          headersData: HeadersData
      ): Validated[Out] =
        headersData match {
          case (_, None) =>
            // Note: in practice that should not happen because the method `aggregateAndValidate` is
            // only called from the final method `matches`, if `matchAndParseHeaders` succeeded.
            // However, here we override `matchAndParseHeaders` to fail in case the credentials are missing.
            sys.error(
              "This request transformation is currently unsupported. You can't transform further an authenticated request."
            )
          case (h, Some(credentials)) =>
            Valid(
              tuplerUEHCred(
                tuplerUE(urlData, entityData),
                tuplerHCred(h, credentials)
              )
            )
        }

      lazy val authHeader: RequestHeaders[Option[Credentials]] =
        httpHeaders =>
          Valid(
            httpHeaders
              .header[Authorization]
              .flatMap({
                case Authorization(OAuth2BearerToken(token)) => Some(toCredentials(token))
                case _                                       => None
              })
          )

      private[server] def matchAndParseHeadersDirective: Directive1[Validated[(UrlData, HeadersData)]] =
        matchAndProvideParsedUrlAndHeadersData(method, url, headers).flatMap {
          case Valid((_, (_, None /* credentials */ ))) =>
            Directives.complete(
              HttpResponse(
                Unauthorized,
                collection.immutable.Seq[HttpHeader](
                  `WWW-Authenticate`(HttpChallenges.oAuth2("Realm"))
                )
              )
            )
          case validatedUrlAndHeaders =>
            Directives.provide(validatedUrlAndHeaders)
        }

      def urlData(out: Out): UrlData = {
        val (ue, _) = tuplerUEHCred.unapply(out)
        val (u, _) = tuplerUE.unapply(ue)
        u
      }
    }
  }
}
