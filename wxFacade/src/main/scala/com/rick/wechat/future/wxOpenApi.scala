package com.rick.wechat.future

import scala.scalajs.js

private class login extends Callback[Code]

private class checkSession() extends Callback[Unit]

private class navigateToMiniProgram[T](
    val appId: String,
    val path: js.UndefOr[String] = js.undefined,
    override val extraData: js.UndefOr[T] = js.undefined,
    val envVersion: js.UndefOr[String] = js.undefined
) extends Callback[Unit]
    with ExtraData[T]

private class navigateBackMiniProgram[T](override val extraData: js.UndefOr[T] = js.undefined) extends Callback[Unit] with ExtraData[T]

class UserInfo(
    val nickName: js.UndefOr[String] = js.undefined,
    val avatarUrl: js.UndefOr[String] = js.undefined,
    val gender: js.UndefOr[Int] = js.undefined,
    val country: js.UndefOr[String] = js.undefined,
    val province: js.UndefOr[String] = js.undefined,
    val city: js.UndefOr[String] = js.undefined,
    val language: js.UndefOr[String] = js.undefined
) extends js.Object

class GetUserInfoResponse(
    val userInfo: js.UndefOr[UserInfo] = js.undefined,
    val rawData: js.UndefOr[String] = js.undefined,
    val signature: js.UndefOr[String] = js.undefined,
    val encryptedData: js.UndefOr[String] = js.undefined,
    val iv: js.UndefOr[String] = js.undefined,
    val cloudID: js.UndefOr[String] = js.undefined
) extends js.Object

private class getUserInfo(val withCredentials: js.UndefOr[Boolean] = js.undefined, override val lang: js.UndefOr[String] = js.undefined) extends Callback[GetUserInfoResponse] with Lang
