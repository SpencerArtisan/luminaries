package com.artisansoftware.luminaries.core

class CommandLine(args: Array[String]) {
  def hasSwitch(switch: Char): Boolean = switches.contains(switch)

  val switches: Array[Char] = args.filter(_.startsWith("-")).flatMap(_.substring(1).toCharArray)
  val nonSwitches = args.filterNot(_.startsWith("-"))
  val numericArgs: Array[Int] = nonSwitches.filter(_.forall(_.isDigit)).map(_.toInt)
  val textArgs: Array[String] = nonSwitches.filterNot(_.forall(_.isDigit))
  val allTextArgs: String = textArgs mkString " "
}
