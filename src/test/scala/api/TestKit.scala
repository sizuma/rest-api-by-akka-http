package api

import akka.http.scaladsl.model._
import akka.http.scaladsl.testkit.ScalatestRouteTest
import akka.util.ByteString
import org.scalatest.{Matchers, WordSpec}
import spray.json._

trait TestKit extends WordSpec with Matchers with ScalatestRouteTest {
  def postJson(uri: String, json: String): HttpRequest = HttpRequest(
    HttpMethods.POST,
    uri = uri,
    entity = HttpEntity(MediaTypes.`application/json`, ByteString(json))
  )

  def extractJson(f: JsValue => Unit): Unit = {
    status.isSuccess() shouldEqual true
    val httpEntity = response.entity.asInstanceOf[HttpEntity.Strict]
    httpEntity.contentType shouldEqual ContentTypes.`application/json`
    f(responseAs[String].parseJson)
  }
}
