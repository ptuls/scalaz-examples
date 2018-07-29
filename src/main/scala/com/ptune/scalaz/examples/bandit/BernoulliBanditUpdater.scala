package com.ptune.scalaz.examples.bandit

import breeze.stats.distributions.Bernoulli

import scalaz.Scalaz.modify
import scalaz.State

object BernoulliBanditUpdater {

  /**
    * Update counters based on the chosen arm
    * @param dists Bernoulli distributions for the simulation
    * @param chosenArm arm chosen by the algorithm
    * @return updated state of the bandit counters
    */
  def update(dists: Array[Bernoulli],
             chosenArm: Int): State[Array[BanditCounter], Unit] = {
    for {
      nextState <- modify[Array[BanditCounter]](currentState =>
        updateState(currentState, chosenArm, getReward(dists, chosenArm)))
    } yield nextState
  }

  private def updateState(counters: Array[BanditCounter],
                          chosenArm: Int,
                          reward: Int): Array[BanditCounter] = {
    val newBanditCounter = BanditCounter(
      counters(chosenArm).successCount + reward,
      counters(chosenArm).failureCount + (1 - reward))

    counters.zipWithIndex.map {
      case (banditCounter: BanditCounter, index: Int) =>
        if (index == chosenArm) {
          newBanditCounter
        } else {
          banditCounter
        }
    }
  }

  private def getReward(dists: Array[Bernoulli], chosenArm: Int): Int = {
    assert(chosenArm >= 0 && chosenArm < dists.length)
    if (dists(chosenArm).sample()) 1 else 0
  }
}
