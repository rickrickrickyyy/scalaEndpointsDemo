package com.rick.wechat.future

import faithful.Future

object wxExtra {
  implicit class CameraFrameListenerExtra(l: CameraFrameListener) {
    def start(): Future[Unit] = {
      new Callback[Unit].future(l.start)
    }
    def stop(): Future[Unit] = {
      new Callback[Unit].future(l.stop)
    }
  }

}
