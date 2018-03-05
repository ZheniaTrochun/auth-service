package api

import akka.http.scaladsl.server.Directives._

trait HealthRoute {

  val healthRoute =
    pathSingleSlash(complete("I'm alive! ALIVE!")) ~ path("ping")(complete("PONG!"))
}
