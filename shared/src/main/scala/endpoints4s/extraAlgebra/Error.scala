package endpoints4s.extraAlgebra

case class Error[StatusCode](statusCode: StatusCode, errors: Seq[String])
