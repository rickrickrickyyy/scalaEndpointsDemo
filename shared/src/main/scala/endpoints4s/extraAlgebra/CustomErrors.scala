package endpoints4s.extraAlgebra

import endpoints4s.Invalid
import endpoints4s.algebra.EndpointsWithCustomErrors

/** Interpreter for the [[Errors]] algebra that uses endpoints4s built-in error types:
  *
  *   - [[Invalid]] for client errors,
  *   - and `Throwable` for server error.
  *
  * Both types of errors are serialized into a JSON array containing string error values.
  *
  * @group interpreters
  */
trait CustomErrors extends endpoints4s.algebra.Errors with ErrorSchema { this: EndpointsWithCustomErrors =>
  type ClientErrors = Error[StatusCode]
  type ServerError = Error[StatusCode]

  final def invalidToClientErrors(invalid: Invalid): ClientErrors =
    Error(BadRequest, invalid.errors)

  final def clientErrorsToInvalid(clientErrors: ClientErrors): Invalid =
    Invalid(clientErrors.errors)

  final def throwableToServerError(throwable: Throwable): ServerError =
    Error(InternalServerError, Seq(throwable.getMessage))

  final def serverErrorToThrowable(serverError: ServerError): Throwable =
    new Throwable(serverError.errors.headOption.getOrElse("Unknown"))

  /** Response entity format for [[Invalid]] values
    */
  def clientErrorsResponseEntity: ResponseEntity[ClientErrors] = jsonResponse[ClientErrors]

  /** Response entity format for `Throwable` values
    */
  def serverErrorResponseEntity: ResponseEntity[ServerError] = jsonResponse[ServerError]

}
