package com.ptune.scalaz.examples.bandit

sealed trait Counter
case class BanditCounter(successCount: Int, failureCount: Int) extends Counter
