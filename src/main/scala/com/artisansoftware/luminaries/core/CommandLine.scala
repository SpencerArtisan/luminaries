package com.artisansoftware.luminaries.core

case class CommandLine(args: Array[String]) {
  private val switches: Array[Char] = args.filter(_.startsWith("-")).flatMap(_.substring(1).toCharArray)
  private val nonSwitches = args.filterNot(_.startsWith("-"))

  def hasSwitch(switch: Char): Boolean = switches.contains(switch)
  val numericArgs: Array[Int] = nonSwitches.filter(_.forall(_.isDigit)).map(_.toInt)
  val textArgs: Array[String] = nonSwitches.filterNot(_.forall(_.isDigit))
}
