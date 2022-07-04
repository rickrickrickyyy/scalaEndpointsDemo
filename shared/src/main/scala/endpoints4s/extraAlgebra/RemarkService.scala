package endpoints4s.extraAlgebra

import endpoints4s.{Invalid, Valid, generic}

trait RemarkService
    extends RestfulClient[Command]
    with generic.JsonSchemas
    with endpoints4s.algebra.ChunkedResponseEntities
    with endpoints4s.algebra.ChunkedJsonResponseEntities
    with endpoints4s.algebra.JsonEntitiesFromSchemas {

  override val entityName: String = "remark"

  implicit val movingRemarkSchema: Record[MovingRemark] = genericRecord[MovingRemark].xmapPartial(e => {
    if (e.timeStamp == 0) {
      Invalid("timeStamp should not equals 0")
    } else {
      Valid(e)
    }
  })(identity)

  implicit val staticRemarkSchema: JsonSchema[StaticRemark] = genericJsonSchema[StaticRemark]

  override implicit val tSchema: JsonSchema[Command] = {
    implicit val circleGenericRecord: GenericJsonSchema.GenericRecord[MovingRemark] =
      new GenericJsonSchema.GenericRecord(movingRemarkSchema)
    genericJsonSchema[Command]
  }

  def remarkEvents(): Endpoint[Credentials, Either[Chunks[Command], ERROR_RESPONSE]] =
    authenticatedEndpoint(Get, path / API_SEGMENT / entityName / "events", ok(jsonChunksResponse[Command](newLineDelimiterFraming)))

}
