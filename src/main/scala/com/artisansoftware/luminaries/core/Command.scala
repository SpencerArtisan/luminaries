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


