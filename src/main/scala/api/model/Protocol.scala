package api.model

import api.model.in.UserSelection
import spray.json.{DefaultJsonProtocol, JsNumber, JsObject, JsString, JsValue, RootJsonFormat}

object Protocol extends DefaultJsonProtocol {
  implicit val inUserCreateRequest = jsonFormat2(in.UserCreateRequest)
  implicit val inUserSelection = new RootJsonFormat[in.UserSelection] {
    override def write(obj: UserSelection): JsValue = obj match {
      case UserSelection.ByEmail(email) => JsObject("email" -> JsString(email))
      case UserSelection.ById(id) => JsObject("id" -> JsNumber(id))
    }

    override def read(json: JsValue): UserSelection = json.asJsObject.fields match {
      case map if map.contains("email") =>
        val JsString(email) = map("email")
        UserSelection.ByEmail(email)
      case map if map.contains("id") =>
        val JsNumber(id) = map("id")
        UserSelection.ById(id.toLong)
    }
  }

  implicit val outSucceedUserCreation = jsonFormat1(out.SucceedUserCreation)
  implicit val outSucceedUserSearch = jsonFormat2(out.SucceedUserSearch)
}
