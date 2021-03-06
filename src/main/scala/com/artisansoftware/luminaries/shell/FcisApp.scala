package com.artisansoftware.luminaries.shell

import com.artisansoftware.luminaries.core.RichTextImplicits._
import com.artisansoftware.luminaries.core.{ReadLuminariesCommand, _}
import com.artisansoftware.luminaries.io.{LuminaryStore, Screen, Twitter}

object FcisApp {

  def main(args: Array[String]): Unit = {
    val command = CommandFactory.build(CommandLine(args))
    val output = execute(command)
    Screen.write(output + "\n")
  }

  def execute(command: Command): RichText =
    command match {
      case ReadTweetsCommand(hours, filter, timelineFormat) =>
        readTweets(hours, filter, timelineFormat)
      case StreamTweetsCommand(hours, filter) =>
        streamTweets(filter)
        readTweets(hours, filter, true)
      case ReadLuminariesCommand =>
        readLuminaries
      case DeleteLuminaryCommand(twitterHandle) =>
        deleteLuminary(twitterHandle)
      case AddLuminaryCommand(twitterHandle, name) =>
        addLuminary(twitterHandle, name)
      case SimpleCommand(text) =>
        text
    }

  private def readTweets(hours: Int, filter: Tweet => Boolean, timelineFormat: Boolean): RichText = {
    val luminaries = LuminaryStore.read()
    val tweets = Twitter.read(luminaries, hours).filter(filter)
    if (timelineFormat)
      TweetFormatter.formatCompact(tweets, luminaries)
    else
      TweetFormatter.format(tweets, luminaries)
  }

  private def streamTweets(filter: Tweet => Boolean): Unit = {
    val luminaries = LuminaryStore.read()
    val luminaryIds = Twitter.readIds(luminaries)
    Twitter.tweetStream(luminaryIds, onStreamedTweet(luminaries, filter))
  }

  private def onStreamedTweet(luminaries: List[Luminary], filter: Tweet => Boolean)(tweet: Tweet): Unit =
    if (filter(tweet)) Screen.write(TweetFormatter.formatCompact(tweet, luminaries))

  private def readLuminaries: RichText =
    LuminaryFormatter.format(LuminaryStore.read())

  private def addLuminary(twitterHandle: String, name: String): RichText = {
    val luminaries = LuminaryStore.read()
    val updated = Luminary(name, twitterHandle) :: luminaries
    LuminaryStore.write(updated)
    "Added luminary " + name.bold + " with twitter handle " + twitterHandle.bold
  }

  private def deleteLuminary(twitterHandle: String): RichText = {
    val luminaries = LuminaryStore.read()
    val updated = luminaries.filterNot(twitterHandle == _.twitterHandle)
    if (updated.length < luminaries.length) {
      LuminaryStore.write(updated)
      "Removed luminary with twitter handle " + twitterHandle.bold
    } else
      "There is no luminary with twitter handle ".red + twitterHandle.red.bold
  }
}
