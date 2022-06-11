package endpoints4s.extraAlgebra

object IntStatusCodes extends endpoints4s.algebra.StatusCodes {
  type StatusCode = Int
  val OK = 200
  val Created = 201
  val Accepted = 202
  override val NonAuthoritativeInformation = 203
  val NoContent = 204
  override val ResetContent = 205
  override val PartialContent = 206
  override val MultiStatus = 207
  override val AlreadyReported = 208
  override val IMUsed = 226

  override val NotModified = 304

  val BadRequest = 400
  val Unauthorized = 401
  override val PaymentRequired = 402
  val Forbidden = 403
  val NotFound = 404
  override val MethodNotAllowed = 405
  override val NotAcceptable = 406
  override val ProxyAuthenticationRequired = 407
  override val RequestTimeout = 408
  override val Conflict = 409
  override val Gone = 410
  override val LengthRequired = 411
  override val PreconditionFailed = 412
  val PayloadTooLarge = 413
  override val UriTooLong = 414
  override val UnsupportedMediaType = 415
  override val RangeNotSatisfiable = 416
  override val ExpectationFailed = 417
  override val MisdirectedRequest = 421
  override val UnprocessableEntity = 422
  override val Locked = 423
  override val FailedDependency = 424
  override val TooEarly = 425
  override val UpgradeRequired = 426
  override val PreconditionRequired = 428
  val TooManyRequests = 429
  override val RequestHeaderFieldsTooLarge = 431
  override val UnavailableForLegalReasons = 451

  val InternalServerError = 500
  val NotImplemented = 501

}
