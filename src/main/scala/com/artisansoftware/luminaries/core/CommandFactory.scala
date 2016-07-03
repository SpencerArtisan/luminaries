package com.artisansoftware.luminaries.core

import com.artisansoftware.luminaries.core.RichTextImplicits._

object CommandFactory {
  private val DefaultHours = 3
  private val Boring = List("football", "soccer")

  def build(commandLine: CommandLine): Command =
    if (commandLine.hasSwitch('l'))
      ReadLuminariesCommand
    else if (commandLine.hasSwitch('d'))
      if (commandLine.textArgs.nonEmpty)
        DeleteLuminaryCommand(commandLine.textArgs(0))
      else
        help
    else if (commandLine.hasSwitch('a'))
      if (commandLine.textArgs.length > 1)
        AddLuminaryCommand(commandLine.textArgs(0), commandLine.textArgs.drop(1) mkString " ")
      else
        help
    else if (commandLine.hasSwitch('h'))
      help
    else
      if (commandLine.hasSwitch('s'))
        StreamTweetsCommand(hours(commandLine), filter(commandLine))
      else
        ReadTweetsCommand(hours(commandLine), filter(commandLine))

  def hours(commandLine: CommandLine): Int =
    if (commandLine.numericArgs.isEmpty) DefaultHours else commandLine.numericArgs(0)

  private def filter(commandLine: CommandLine)(tweet: Tweet): Boolean =
    (!tweet.isRetweet || commandLine.hasSwitch('r')) &&
      (tweet.getInReplyToUserId == -1 || commandLine.hasSwitch('c')) &&
      !contains(tweet, Boring) &&
      (commandLine.textArgs.isEmpty || contains(tweet, commandLine.textArgs))

  def contains(tweet: Tweet, words: Seq[String] = Boring): Boolean =
    words.exists(word => tweet.getText.toUpperCase.contains(word.toUpperCase))

  val help = new SimpleCommand(
    "\nCommand Arguments:\n" +
      "\t[number of hours] [keywords]\n" +
      "Switches:\n" +
      "\t[without switches]          Display recent tweets\n" +
      "\t-c                          Include conversation tweets (default is off)\n" +
      "\t-r                          Include retweets (default is off)\n" +
      "\t-s                          Maintain live stream of results\n" +
      "\t-l                          List luminaries\n" +
      "\t-a [twitter handle] [name]  Add a new luminary\n" +
      "\t-d [twitter handle]         Delete a luminary\n" +
      "\t-h                          Display this help\n")
}
