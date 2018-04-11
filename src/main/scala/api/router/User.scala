package api.router

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server
import akka.http.scaladsl.server.Directives._
import api.exceptions.{UserAlreadyExists, UserNotFound}
import api.model.in
import api.model.internal
import api.model.out
import api.model.Protocol._
import api.manager
import api.model.in.UserSelection

import scala.util.{Failure, Success}
import scala.concurrent.ExecutionContext.Implicits.global

trait User extends SprayJsonSupport{
  private[this] val userManager: manager.User = new manager.User.UserInMemoryImplementation

  val userRouter: server.Route =
    path("user") {
      post {
          entity(as[in.UserCreateRequest]) { userCreateRequest => {
              onComplete(userManager.create(userCreateRequest)) {
                case Failure(UserNotFound()) => complete((StatusCodes.BadRequest, "invalid email"))
                case Failure(error) => reject
                case Success(user) =>
                  val succeedUserCreation = out.SucceedUserCreation(user.id)
                  complete(succeedUserCreation)
              }
          }
        }
      }
    } ~ path("user" / LongNumber) { id => {
      get {
        onComplete(userManager(UserSelection.ById(id))) {
          case Failure(UserAlreadyExists()) => complete((StatusCodes.NotFound, "user not found"))
          case Failure(_) => reject
          case Success(user) => complete(out.SucceedUserSearch(user.id, user.email))
        }
      }
    }}
}
