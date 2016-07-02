package com.artisansoftware.luminaries.shell

import com.artisansoftware.luminaries.core.RichTextImplicits._
import com.artisansoftware.luminaries.core.{ReadLuminariesCommand, _}
import com.artisansoftware.luminaries.io.Twitter.Tweet
import com.artisansoftware.luminaries.io.{LuminaryStore, Screen, Twitter}

object FcisApp {

  def main(args: Array[String]): Unit = {
    val command = CommandFactory.build(new CommandLine(args))
    val output = execute(command)
    Screen.write(output:_*)
  }

  def execute(command: Command): List[Line] = {
    val output: List[Line] = command match {
      case ReadTweetsCommand(hours, filter) =>
        readTweets(hours, filter)
      case StreamTweetsCommand(hours, filter) =>
        streamTweets(filter)
        readTweets(hours, filter)
      case ReadLuminariesCommand =>
        readLuminaries
      case DeleteLuminaryCommand(twitterHandle) =>
        List(deleteLuminary(twitterHandle))
      case AddLuminaryCommand(twitterHandle, name) =>
        List(addLuminary(twitterHandle, name))
      case SimpleCommand(text) =>
        text
    }
    output
  }

  private def readTweets(hours: Int, filter: Tweet => Boolean): List[Line] = {
    val luminaries = LuminaryStore.read()
    val tweets = Twitter.read(luminaries, hours).filter(filter)
    TweetFormatter.format(tweets, luminaries)
  }

  private def streamTweets(filter: Tweet => Boolean): Unit = {
    val luminaries = LuminaryStore.read()
    val luminaryIds = Twitter.readIds(luminaries)
    Twitter.tweetStream(luminaryIds, onStreamedTweet(luminaries, filter))
  }

  private def onStreamedTweet(luminaries: List[Luminary], filter: Tweet => Boolean)(tweet: Tweet): Unit =
    if (filter(tweet)) Screen.write(TweetFormatter.format(List(tweet), luminaries))

  private def readLuminaries: List[Line] =
    LuminaryFormatter.format(LuminaryStore.read())

  private def addLuminary(twitterHandle: String, name: String): Line = {
    val luminaries = LuminaryStore.read()
    val updated = Luminary(name, twitterHandle) :: luminaries
    LuminaryStore.write(updated)
    new Line("Added luminary ", name.bold, " with twitter handle ", twitterHandle.bold)
  }

  private def deleteLuminary(twitterHandle: String): Line = {
    val luminaries = LuminaryStore.read()
    val updated = luminaries.filterNot(twitterHandle == _.twitterHandle)
    if (updated.length < luminaries.length) {
      LuminaryStore.write(updated)
      new Line("Removed luminary with twitter handle ", twitterHandle.bold)
    } else
      new Line("There is no luminary with twitter handle ".red, twitterHandle.red.bold)
  }

}