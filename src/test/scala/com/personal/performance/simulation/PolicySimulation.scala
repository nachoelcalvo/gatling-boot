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

  val scnAddress = scenario("PolicySimulation").feed(feeder)
    .exec(http("get_policies_by_city")
    .get("/policies/addresses/${city}")
    .check(status.is(200))
  )

//  setUp(scnAddress.inject(constantUsersPerSec(10) during (10))).protocols(httpProtocol)
  setUp(scnAddress.inject(
    nothingFor(4 seconds),
    atOnceUsers(10),
    rampUsers(10) over (5 seconds),
    constantUsersPerSec(20) during (15 seconds),
    constantUsersPerSec(20) during (15 seconds) randomized,
    rampUsersPerSec(10) to 20 during (10 seconds),
    rampUsersPerSec(10) to 20 during (10 seconds) randomized)).protocols(httpProtocol)

  def getCity() = Random.shuffle(cities.toList).head
}
