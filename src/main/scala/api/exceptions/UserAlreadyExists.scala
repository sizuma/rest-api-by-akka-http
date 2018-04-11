package api.exceptions

case class UserAlreadyExists() extends Throwable("user already exists")
