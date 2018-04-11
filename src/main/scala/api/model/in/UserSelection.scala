package api.model.in

sealed trait UserSelection

object UserSelection {
  case class ById(id: Long) extends UserSelection
  case class ByEmail(email: String) extends UserSelection
}
