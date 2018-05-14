package org.baeldung.sim

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import jodd.util.RandomString

import scala.util.Random

class RecordedSimulation extends Simulation {

  def newIsbn() = Random.nextInt(Integer.MAX_VALUE)

  def newTitle() = RandomString.getInstance().randomAlpha(10)

  val httpProtocol = http
    .baseURL("http://localhost:8080")
    .acceptHeader("application/hal+json")


  val scn = scenario("RecordedSimulation")
    .exec(http("get_root")
      .get("/"))
    .pause(1)

    .exec(http("get_books")
      .get("/books"))
    .pause(1)

    .exec(http("create_book")
      .post("/books")
      .header("Content-Type", "application/json")
      // .body(StringBody(s"""{ "title": "${newTitle()}", "isbn": ${newIsbn()}}""")))
      .body(StringBody("{ \"title\": \"" + newTitle() + "\", \"isbn\": " + Random.nextInt(Integer.MAX_VALUE) + " }")))
    .pause(1)

    .exec(http("get_books_paginated")
      .get("/books?page=1&size=20")

    )

  setUp(scn.inject(atOnceUsers(100))).protocols(httpProtocol)

}
