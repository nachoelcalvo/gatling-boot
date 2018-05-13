package org.baeldung

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class RecordedSimulation extends Simulation {

    val httpProtocol = http
        .baseURL("http://localhost:8080")
        .acceptHeader("application/hal+json")

    val scn = scenario("RecordedSimulation")
        .exec(http("request_0")
            .get("/"))
        .pause(5)
        .exec(http("request_1")
            .get("/books"))

    setUp(scn.inject(atOnceUsers(10))).protocols(httpProtocol)
}
