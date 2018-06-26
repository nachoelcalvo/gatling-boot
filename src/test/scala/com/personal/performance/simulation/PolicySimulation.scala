package com.personal.performance.simulation

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration._
import org.slf4j.LoggerFactory

import scala.util.Random


class PolicySimulation extends Simulation {

  val cities = Array("madrid", "barcelona", "valencia")

  val feeder = Iterator.continually(Map("city" -> (getCity())))

  val logger = LoggerFactory.getLogger(classOf[Nothing])

  val httpProtocol = http
    .baseURL("http://localhost:8080")

  val scn = scenario("EchoSimulation").exec(http("get_policies")
    .get("/policies/all")
    .check(status.is(200))
  )

  val scnCity = scenario("PolicySimulation").feed(feeder)
    .exec(http("get_policies_by_city")
    .get("/policies/addresses/${city}")
    .check(status.is(200))
  )

  //setUp(scn.inject(constantUsersPerSec(10) during (10))).protocols(httpProtocol)

  //Sample set up for a test scenario
  setUp(scnCity.inject(
    nothingFor(4 seconds),
    atOnceUsers(5),
    rampUsers(10) over (5 seconds),
    constantUsersPerSec(15) during (10 seconds),
    constantUsersPerSec(15) during (10 seconds) randomized,
    rampUsersPerSec(10) to 30 during (10 seconds),
    rampUsersPerSec(10) to 30 during (10 seconds) randomized)).protocols(httpProtocol)

  def getCity() = Random.shuffle(cities.toList).head
}
