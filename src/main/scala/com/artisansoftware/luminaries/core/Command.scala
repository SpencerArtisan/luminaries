package com.artisansoftware.luminaries.core

import com.artisansoftware.luminaries.io.Twitter.Tweet

/**
  * Created by spencerward on 02/07/2016.
  */
trait Command

case class SimpleCommand(result: List[Line]) extends Command
case class StreamTweetsCommand(hours: Int, filter: Tweet => Boolean) extends Command
case class ReadTweetsCommand(hours: Int, filter: Tweet => Boolean) extends Command
case object ReadLuminariesCommand extends Command
case class DeleteLuminaryCommand(twitterHandle: String) extends Command
case class AddLuminaryCommand(twitterHandle: String, name: String) extends Command
object Help extends SimpleCommand(List(
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


