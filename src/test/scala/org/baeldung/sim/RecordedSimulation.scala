package org.baeldung.sim

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.http.request.builder.RequestBuilder
import jodd.util.RandomString
import org.slf4j.LoggerFactory

import scala.util.Random

class RecordedSimulation extends Simulation {
  val logger = LoggerFactory.getLogger(classOf[Nothing])

  val httpProtocol = http
    .baseURL("http://localhost:8080")
    .acceptHeader("application/hal+json")

  val feeder = Iterator.continually(Map("newIsbn" -> (newIsbn()), "newTitle" -> (newTitle())))

  val scn = scenario("RecordedSimulation").feed(feeder)
    .exec(
      http("get_root")
        .get("/")
        .check(status.is(200))
    )
    .pause(1)

    .exec(http("get_books")
      .get("/books")
      .check(status.is(200))
    )
    .pause(1)

    .exec(
      http("create_book")
        .post("/books")
        .notSilent
        .header("Content-Type", "application/json")
        .body(StringBody("{ \"title\": \"${newTitle}\", \"isbn\": ${newIsbn} }"))
        .check(status.is(201))
    )
    .pause(1)

    .exec(http("get_books_paginated")
      .get("/books?page=1&size=20")
      .check(status.is(200))
    )

  setUp(scn.inject(atOnceUsers(10))).protocols(httpProtocol)
  // setUp(scn.inject(rampUsers(3).over(3))).protocols(httpProtocol)

  //

  def newIsbn() = Random.nextInt(Integer.MAX_VALUE)

  def newTitle() = RandomString.getInstance().randomAlpha(10)

}
