package com.artisansoftware.luminaries.core

import com.artisansoftware.luminaries.io.Twitter.Tweet

object CommandFactory {
  private val DefaultHours = 3
  private val Boring = List("football", "soccer")

  def build(commandLine: CommandLine): Command =
    if (commandLine.hasSwitch('h'))
      help
    else if (commandLine.hasSwitch('l'))
      ReadLuminariesCommand
    else if (commandLine.hasSwitch('d'))
      DeleteLuminaryCommand(commandLine.textArgs(0))
    else if (commandLine.hasSwitch('a'))
      AddLuminaryCommand(commandLine.textArgs(0), commandLine.textArgs.drop(1) mkString " ")
    else {
      val hours: Int = if (commandLine.numericArgs.isEmpty) DefaultHours else commandLine.numericArgs(0)
      if (commandLine.hasSwitch('s'))
        StreamTweetsCommand(hours, filter(commandLine))
      else
        ReadTweetsCommand(hours, filter(commandLine))
    }

  private def filter(commandLine: CommandLine)(tweet: Tweet): Boolean =
    (!tweet.isRetweet || commandLine.hasSwitch('r')) &&
      (tweet.getInReplyToUserId == -1 || commandLine.hasSwitch('c')) &&
      !contains(tweet, Boring) &&
      (commandLine.textArgs.isEmpty || contains(tweet, commandLine.textArgs))

  def contains(tweet: Tweet, words: Seq[String] = Boring): Boolean =
    words.exists(word => tweet.getText.toUpperCase.contains(word.toUpperCase))

  val help = new SimpleCommand(List(
    new Line(""),
    new Line("Command Arguments:"),
    new Line("\t[number of hours] [keywords]"),
    new Line(""),
    new Line("Switches:"),
    new Line("\t[without switches]          Display recent tweets"),
    new Line("\t-c                          Include conversation tweets (default is off)"),
    new Line("\t-r                          Include retweets (default is off)"),
    new Line("\t-s                          Maintain live stream of results"),
    new Line("\t-l                          List luminaries"),
    new Line("\t-a [twitter handle] [name]  Add a new luminary"),
    new Line("\t-d [twitter handle]         Delete a luminary"),
    new Line("\t-h                          Display this help"),
    new Line("")
  ))
}
