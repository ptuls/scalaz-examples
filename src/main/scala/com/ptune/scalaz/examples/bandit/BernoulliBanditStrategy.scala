package com.ptune.scalaz.examples.bandit

import breeze.stats.distributions.Beta

import scalaz.Scalaz.get
import scalaz.State


/**
  * All strategies for the multiarm bandit: Thompson sampling and epsilon-greedy
  */
object BernoulliBanditStrategy {
  def pullThompson(): State[Array[BanditCounter], Int] = {
    for {
      currentState <- get[Array[BanditCounter]]
    } yield {
      val thetas = sample(currentState)
      thetas.zipWithIndex.maxBy(_._1)._2
    }
  }

  def pullGreedy(): State[Array[BanditCounter], Int] = {
    for {
      currentState <- get[Array[BanditCounter]]
    } yield {
      meanBetaDist(currentState).zipWithIndex.maxBy(_._1)._2
    }
  }

  private def sample(counters: Array[BanditCounter]): Array[Double] = {
    val distributions = counters.map { c =>
      Beta.distribution((c.successCount.toDouble, c.failureCount.toDouble))
    }
    distributions.map(_.draw)
  }

  private def meanBetaDist(counters: Array[BanditCounter]): Array[Double] = {
    counters.map { c =>
      c.successCount.toDouble / (c.successCount + c.failureCount).toDouble
    }
  }
}
