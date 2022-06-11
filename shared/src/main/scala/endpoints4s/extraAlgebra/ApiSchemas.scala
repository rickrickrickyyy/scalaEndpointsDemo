package endpoints4s.extraAlgebra

import endpoints4s.generic

trait ApiSchemas extends generic.JsonSchemas {
  type SINGLE_RESPONSE[T] = JsonItemMsg[T]
  type MULTIPLE_RESPONSE[T] = JsonItemsMsg[T]

  implicit def multipleResponseSchema[T](implicit tSchemas: JsonSchema[T]): JsonSchema[JsonItemsMsg[T]] =
    genericJsonSchema[JsonItemsMsg[T]]

  implicit def singleResponseSchema[T](implicit tSchemas: JsonSchema[T]): JsonSchema[JsonItemMsg[T]] =
    genericJsonSchema[JsonItemMsg[T]]

  implicit val myEntitySchema: JsonSchema[MyEntity] = genericJsonSchema[MyEntity]
    .withTitle("测试抬头")
    .withDescription("用来测试的类")
    .withExample(MyEntity("我的类", 100, 2))
//    .xmapPartial(e => Invalid(Seq("用来测试的类", "用来测试的类")))(identity)

}
