package com.ptune.scalaz.examples.state

import scalaz._
import Scalaz._

sealed trait Switch
case object LightSwitch extends Switch

sealed trait LightStatus
case object On extends LightStatus
case object Off extends LightStatus

case class LightBulb(lightStatus: LightStatus) {
  override def toString: String = s"Light bulb state: ${this.lightStatus.toString}"
}

/***
  * Simple state monad example: flip a switch to change the state of the lightbulb.
  * The main idea is that the state is encapsulated in the State monad, that the
  * object representing the lightbulb does not have to be mutated.
  */
object LightBulbExampleMain {
  def main(args: Array[String]): Unit = {
    val initState = LightBulb(Off)
    val newState = flipSwitch(LightSwitch).run(initState)._1
    println(initState)
    println(newState)
    println(flipSwitch(LightSwitch).run(newState)._1)
  }

  def flipSwitch(s: Switch): State[LightBulb, Unit] = {
    for {
      currStatus <- get[LightBulb]
      newStatus <- if (currStatus.lightStatus == On) {
        put(currStatus.copy(Off))
      } else {
        put(currStatus.copy(On))
      }
    } yield newStatus
  }

}
