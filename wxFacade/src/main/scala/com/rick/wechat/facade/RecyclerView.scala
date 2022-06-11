package com.rick.wechat.facade

import com.rick.wechat.future.Rect

import scala.scalajs.js
import scala.scalajs.js.annotation.{JSGlobal, JSImport}

@js.native
@JSGlobal
class RecycleContext[T] extends js.Object {
  def append(list: js.Array[T], callback: js.Function) = js.native

  def splice(begin: Int, count: Int, list: js.Array[T], callback: js.Function0[Unit]) = js.native

  def update(begin: Int, list: js.Array[T], callback: js.Function0[Unit]) = js.native

  def destroy() = js.native

  def forceUpdate(callback: js.Function0[Unit], reinitSlot: Boolean) = js.native

  def getBoundingClientRect(index: Int): Rect = js.native

  def getScrollTop(): Int = js.native

  def transformRpx(rpx: Int): Int = js.native

  def getViewportItems(inViewportPx: Int): Int = js.native
}

@js.native
@JSImport("miniprogram-recycle-view", JSImport.Default)
object RecycleContext extends js.Object {
  def apply[T](o: RecycleContextOptions): RecycleContext[T] = js.native

  def apply[T](o: RecycleContextOptionsFun[T]): RecycleContext[T] = js.native
}

class RecycleViewItemSize(val width: Int, val height: Int) extends js.Object

class RecycleContextOptions(val id: String, val dataKey: String, val page: Page, val itemSize: RecycleViewItemSize, val useInPage: Boolean, val root: js.UndefOr[Page]) extends js.Object

class RecycleContextOptionsFun[T](
    val id: String,
    val dataKey: String,
    val page: Page,
    val itemSize: js.ThisFunction2[RecycleContext[T], T, Int, RecycleViewItemSize],
    val useInPage: Boolean,
    val root: js.UndefOr[Page]
) extends js.Object

object RecycleContextOptions {
  def apply[T](id: String, dataKey: String, page: Page, itemSize: js.ThisFunction2[RecycleContext[T], T, Int, RecycleViewItemSize]): RecycleContextOptionsFun[T] =
    new RecycleContextOptionsFun(id, dataKey, page, itemSize, false, js.undefined)

  def apply(id: String, dataKey: String, page: Page, itemSize: RecycleViewItemSize): RecycleContextOptions =
    new RecycleContextOptions(id, dataKey, page, itemSize, false, js.undefined)

  def apply[T](id: String, dataKey: String, page: Page, itemSize: js.ThisFunction2[RecycleContext[T], T, Int, RecycleViewItemSize], root: Page): RecycleContextOptionsFun[T] =
    new RecycleContextOptionsFun(
      id,
      dataKey,
      page,
      itemSize,
      true,
      js.defined {
        root
      }
    )

  def apply(id: String, dataKey: String, page: Page, itemSize: RecycleViewItemSize, root: Page): RecycleContextOptions =
    new RecycleContextOptions(
      id,
      dataKey,
      page,
      itemSize,
      true,
      js.defined {
        root
      }
    )

}
