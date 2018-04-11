package api

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import api.router.Router

object Boot {
  def main(args: Array[String]): Unit = {
    implicit lazy val system = ActorSystem("my-system")
    implicit lazy val materializer = ActorMaterializer()
    implicit val ec = system.dispatcher

    val interface = "localhost"
    val port = 8080
    val binding = Http().bindAndHandle(Router(), interface, port)
    binding.onFailure {
      case err: Exception =>
        System.err.println(err)
    }
  }
}
