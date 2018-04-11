package api.manager

import api.exceptions.{UserAlreadyExists, UserNotFound}
import api.model.in
import api.model.in.{UserCreateRequest, UserSelection}
import api.model.internal

import scala.concurrent.{ExecutionContext, Future}

trait User {

  def apply(selection: in.UserSelection)(implicit executionContext: ExecutionContext): Future[internal.User]

  def create(request: in.UserCreateRequest)(implicit executionContext: ExecutionContext): Future[internal.User]

}

object User {
  class UserInMemoryImplementation extends User {
    var emailMap = Map.empty[String, internal.User]
    var idMap = Map.empty[Long, internal.User]
    var usedId: Long = 0
    val lock = new AnyRef

    override def apply(selection: UserSelection)(implicit executionContext: ExecutionContext): Future[internal.User] = Future (
      lock.synchronized(
        selection match {
          case UserSelection.ByEmail(email) if emailMap contains email => emailMap(email)
          case UserSelection.ById(id) if idMap contains id => idMap(id)
          case _ => throw UserAlreadyExists()
        }
      )
    )

    override def create(request: UserCreateRequest)(implicit executionContext: ExecutionContext): Future[internal.User] = Future (
      lock.synchronized(
        if(emailMap.contains(request.email)) throw UserNotFound()
        else {
          val user = internal.User(usedId, request.email, request.password)
          emailMap += (user.email -> user)
          idMap += (user.id -> user)
          usedId = usedId + 1
          user
        }
      )
    )
  }
}