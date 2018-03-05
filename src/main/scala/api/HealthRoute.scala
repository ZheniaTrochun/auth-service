package api

import akka.http.scaladsl.server.Directives._

trait HealthRoute {
  val healthRoute = path("/")(complete("I'm alive! ALIVE!"))
}
