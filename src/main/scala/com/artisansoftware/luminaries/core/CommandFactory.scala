package com.artisansoftware.luminaries.core

import com.artisansoftware.luminaries.io.Twitter.Tweet

/**
  * Created by spencerward on 02/07/2016.
  */
object CommandFactory {
  private val DefaultHours = 3

  def build(commandLine: CommandLine): Command = {
    if (commandLine.hasSwitch('h'))
      Help
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
  }

  private def filter(commandLine: CommandLine)(t: Tweet): Boolean =
    (!t.isRetweet || commandLine.hasSwitch('r')) &&
      (t.getInReplyToUserId == -1 || commandLine.hasSwitch('c')) &&
      (commandLine.textArgs.isEmpty ||
        commandLine.textArgs.exists(keyword => t.getText.toUpperCase.contains(keyword.toUpperCase)))
}
