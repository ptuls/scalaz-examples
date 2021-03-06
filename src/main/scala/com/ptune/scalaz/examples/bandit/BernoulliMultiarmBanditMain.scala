package com.ptune.scalaz.examples.bandit

import breeze.stats.distributions.Bernoulli
import com.ptune.scalaz.examples.bandit.BernoulliBanditStrategy._
import com.ptune.scalaz.examples.bandit.BernoulliBanditUpdater._
import com.typesafe.scalalogging.LazyLogging

import scala.annotation.tailrec
import scalaz._

/**
  * Multiarm bandit for the Bernoulli distribution simulator, with the use of the
  * State monad to record state of the counters and arm pull action. State monad
  * fits into the reinforcement learning paradigm.
  */
object BernoulliMultiarmBanditMain extends LazyLogging {
  def main(args: Array[String]): Unit = {
    val bernoulliDistParams = Array(0.1, 0.6, 0.3, 0.4)
    val bernoulliDists =
      bernoulliDistParams.map(Bernoulli.distribution)

    /**
      * Number of trials of the simulator
      */
    val numTrials = 10000

    /**
      * MaxEnt-style prior distribution, corresponds to Beta(1,1), i.e., uniform distribution
      */
    val initCounters = bernoulliDistParams.map(_ => BanditCounter(1, 1))

    logger.info("Running simulation")
    logger.info("Final state means (Thompson Sampling):")
    val finalCountersTS =
      simulate(numTrials)(initCounters, bernoulliDists, pullThompson())
    printFinalCounterStates(finalCountersTS)

    logger.info("")
    logger.info("Final state means (Epsilon-greedy):")
    val finalCountersEG =
      simulate(numTrials)(initCounters, bernoulliDists, pullGreedy())
    printFinalCounterStates(finalCountersEG)
  }

  def printFinalCounterStates(counters: Array[BanditCounter]): Unit = {
    counters.zipWithIndex.foreach {
      case (banditCounter: BanditCounter, index: Int) =>
        logger.info(s"Bandit counter $index: ${banditCounter.successCount.toDouble /
          (banditCounter.successCount.toDouble + banditCounter.failureCount.toDouble)}")
    }
  }

  @tailrec
  def simulate(trials: Int)(
      init: Array[BanditCounter],
      bernoulliDists: Array[Bernoulli],
      pullStrategy: State[Array[BanditCounter], Int]
  ): Array[BanditCounter] = {
    assert(trials >= 0, "number of trials must be non-negative")

    val pullState = pullStrategy.run(init)
    val updateState = update(bernoulliDists, pullState._2).run(pullState._1)._1
    if (trials > 0) {
      simulate(trials - 1)(updateState, bernoulliDists, pullStrategy)
    } else {
      updateState
    }
  }
}
