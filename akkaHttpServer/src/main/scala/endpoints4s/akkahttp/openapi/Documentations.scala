package endpoints4s.akkahttp.openapi

import endpoints4s.akkahttp.server
import endpoints4s.openapi.model.{Info, OpenApi}

object Documentations extends server.Endpoints with server.JsonEntitiesFromEncodersAndDecoders {

  def routes() =
    endpoint(get(path / "doc" / "MyApiDoc.json"), ok(jsonResponse[OpenApi]))
      .implementedBy(_ => {
        val doc = new MyApiDoc()
        val info = Info("MyApiDoc", "1.0.0")
        val api = doc.openApi(info)(doc.documentedEndpoint(): _*)
        api
      })
}
