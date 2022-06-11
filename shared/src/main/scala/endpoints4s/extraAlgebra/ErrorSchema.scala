package endpoints4s.extraAlgebra

import endpoints4s.algebra

trait ErrorSchema extends algebra.JsonEntitiesFromSchemas {
  implicit lazy val statusCodeSchema: JsonSchema[StatusCode] =
    intJsonSchema.xmap(e => statusCodeFromInt(e))(e => intFromStatusCode(e))

  implicit lazy val errorSchema: JsonSchema[Error[StatusCode]] =
    field[StatusCode]("statusCode")
      .zip(field[Seq[String]]("errors"))
      .xmap(e => Error(e._1, e._2))(e => (e.statusCode, e.errors))
      .withExample(Error(InternalServerError, Seq("错误信息")))

  def intFromStatusCode(s: StatusCode): Int

  def statusCodeFromInt(s: Int): StatusCode = {
    s match {
      case IntStatusCodes.BadRequest                  => BadRequest
      case IntStatusCodes.Unauthorized                => Unauthorized
      case IntStatusCodes.PaymentRequired             => PaymentRequired
      case IntStatusCodes.Forbidden                   => Forbidden
      case IntStatusCodes.NotFound                    => NotFound
      case IntStatusCodes.MethodNotAllowed            => MethodNotAllowed
      case IntStatusCodes.NotAcceptable               => NotAcceptable
      case IntStatusCodes.ProxyAuthenticationRequired => ProxyAuthenticationRequired
      case IntStatusCodes.RequestTimeout              => RequestTimeout
      case IntStatusCodes.Conflict                    => Conflict
      case IntStatusCodes.Gone                        => Gone
      case IntStatusCodes.LengthRequired              => LengthRequired
      case IntStatusCodes.PreconditionFailed          => PreconditionFailed
      case IntStatusCodes.PayloadTooLarge             => PayloadTooLarge
      case IntStatusCodes.UriTooLong                  => UriTooLong
      case IntStatusCodes.UnsupportedMediaType        => UnsupportedMediaType
      case IntStatusCodes.RangeNotSatisfiable         => RangeNotSatisfiable
      case IntStatusCodes.ExpectationFailed           => ExpectationFailed
      case IntStatusCodes.MisdirectedRequest          => MisdirectedRequest
      case IntStatusCodes.UnprocessableEntity         => UnprocessableEntity
      case IntStatusCodes.Locked                      => Locked
      case IntStatusCodes.FailedDependency            => FailedDependency
      case IntStatusCodes.TooEarly                    => TooEarly
      case IntStatusCodes.UpgradeRequired             => UpgradeRequired
      case IntStatusCodes.PreconditionRequired        => PreconditionRequired
      case IntStatusCodes.TooManyRequests             => TooManyRequests
      case IntStatusCodes.RequestHeaderFieldsTooLarge => RequestHeaderFieldsTooLarge
      case IntStatusCodes.UnavailableForLegalReasons  => UnavailableForLegalReasons

      case IntStatusCodes.InternalServerError => InternalServerError
      case IntStatusCodes.NotImplemented      => NotImplemented
      case _                                  => NotImplemented
    }

  }
}
