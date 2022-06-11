package endpoints4s.extraAlgebra

case class JsonItemsMsg[T](data: Seq[T], isLastPage: Boolean)
