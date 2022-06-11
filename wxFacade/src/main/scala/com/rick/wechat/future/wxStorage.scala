package com.rick.wechat.future

import scala.scalajs.js
import scala.scalajs.js.UndefOr

//数据缓存
trait GetStorageResult[T] extends Data[T] with ErrMsg

trait GetStorageInfoResult extends js.Object {
  val keys: js.UndefOr[js.Array[String]] = js.undefined
  val currentSize: js.UndefOr[Long] = js.undefined
  val limitSize: js.UndefOr[Long] = js.undefined
}

private class clearStorage extends Callback[ErrMsg]

private class getStorage[T](override val key: js.UndefOr[String]) extends Callback[GetStorageResult[T]] with Key

private class getStorageInfo extends Callback[GetStorageInfoResult]

private class removeStorage(override val key: js.UndefOr[String]) extends Callback[ErrMsg] with Key

private class setStorage[T](override val key: js.UndefOr[String], override val data: UndefOr[T]) extends Callback[ErrMsg] with Key with Data[T]
