package api.router

import akka.http.scaladsl.server
import akka.http.scaladsl.server.Directives.rejectEmptyResponse

object Router extends User {
  def apply(): server.Route = rejectEmptyResponse {
    userRouter
  }
}
