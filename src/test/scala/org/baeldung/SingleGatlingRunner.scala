package org.baeldung

import io.gatling.app.Gatling
import io.gatling.core.config.GatlingPropertiesBuilder
import org.baeldung.sim.RecordedSimulation

object SingleGatlingRunner {

  def main(args: Array[String]) {
    Engine.main(args)
  }
}