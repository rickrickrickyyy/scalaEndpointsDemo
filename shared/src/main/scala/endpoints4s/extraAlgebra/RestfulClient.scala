package endpoints4s.extraAlgebra

import endpoints4s.generic

trait RestfulClient[T] extends RestfulEndpoints {
  val entityName: String
  implicit val tSchema: JsonSchema[T]

  def getItemImpl(): Endpoint[(String, Credentials), Either[SINGLE_RESPONSE[T], ERROR_RESPONSE]] = {
    getItem[T](entityName)(tSchema)
  }

  def deleteItemImpl(): Endpoint[(String, Credentials), Either[SINGLE_RESPONSE[T], ERROR_RESPONSE]] = {
    deleteItem[T](entityName)
  }

  def updateItemImpl(): Endpoint[(String, T, Credentials), Either[SINGLE_RESPONSE[T], ERROR_RESPONSE]] = {
    updateItem[T](entityName)(tSchema)
  }

  def upsertItemImpl(): Endpoint[(String, T, Credentials), Either[SINGLE_RESPONSE[T], ERROR_RESPONSE]] = {
    upsertItem[T](entityName)(tSchema)
  }

  def postItemNoIdImpl(): Endpoint[(T, Credentials), Either[SINGLE_RESPONSE[T], ERROR_RESPONSE]] = {
    postItemNoId[T](entityName)(tSchema)
  }

  def patchItemWithFieldImpl[E](fieldName: String): Endpoint[(String, Map[String, String], Credentials), Either[SINGLE_RESPONSE[T], ERROR_RESPONSE]] = {
    patchItemWithField[T, E](entityName, fieldName)(tSchema)
  }

  def patchItemImpl(fieldName: String): Endpoint[(String, Credentials), Either[SINGLE_RESPONSE[T], ERROR_RESPONSE]] = {
    patchItem[T](entityName, fieldName)(tSchema)
  }

  def clearItemImpl(): Endpoint[Credentials, Either[MULTIPLE_RESPONSE[T], ERROR_RESPONSE]] = {
    clearItem[T](entityName)(tSchema)
  }

  def replaceItemsImpl(): Endpoint[(Seq[T], Credentials), Either[MULTIPLE_RESPONSE[T], ERROR_RESPONSE]] = {
    replaceItems[T](entityName)(tSchema)
  }

  def updateItemsImpl(): Endpoint[(Seq[T], Credentials), Either[MULTIPLE_RESPONSE[T], ERROR_RESPONSE]] = {
    updateItems[T](entityName)(tSchema)
  }

  def getItemsImpl(): Endpoint[((Int, Int), WithDefault[Int], WithDefault[String], WithDefault[Boolean], Credentials), Either[MULTIPLE_RESPONSE[T], ERROR_RESPONSE]] = {
    getItems[T, (Int, Int)](entityName, qs[Int]("what") & qs[Int]("what2"))(tSchema)
  }

}
