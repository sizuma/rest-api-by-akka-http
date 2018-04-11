package api.router

import api.TestKit
import akka.http.scaladsl.model.StatusCodes
import spray.json.{JsNumber, JsString}

class UserTest extends TestKit with User {

  "userRouter" should {
    "GET /user/[存在しないユーザーID] をすると404が返ってくる" in {
      Get("/user/123456789") ~> userRouter ~> check {
        status shouldEqual StatusCodes.NotFound
        responseAs[String] shouldEqual "user not found"
      }
    }

    var user001Id: Option[Long] = None
    "POST /user にユーザー情報を投げると200とIDが返ってくる" in {
      postJson("/user",
        """{
          | "email": "user001@test.com",
          | "password": "password"
          |}""".stripMargin) ~> userRouter ~> check {
        extractJson(json => {
          val obj = json.asJsObject()
          val JsNumber(id) = obj.fields("id")
          user001Id = Some(id.toLong)
        })
      }
    }

    "POST /user にすでに存在するユーザー情報を投げると400が返ってくる" in {
      postJson("/user",
        """{
          | "email": "user001@test.com",
          | "password": "password"
          |}""".stripMargin) ~> userRouter ~> check {
        status shouldEqual StatusCodes.BadRequest
        responseAs[String] shouldEqual "invalid email"
      }
    }

    "GET /user/[存在するユーザID] をするとユーザー情報が返ってくる" in {
      user001Id.isDefined shouldEqual true
      Get(s"/user/${user001Id.get}") ~> userRouter ~> check {
        extractJson(json => {
          val obj = json.asJsObject()
          val fields = obj.fields
          val JsNumber(id) = fields("id")
          val JsString(email) = fields("email")
          id shouldEqual user001Id.get
          email shouldEqual "user001@test.com"
          fields contains "password" shouldEqual false
        })
      }
    }

  }
}
