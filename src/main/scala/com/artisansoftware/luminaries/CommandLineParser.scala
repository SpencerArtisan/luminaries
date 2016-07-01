package com.artisansoftware.luminaries

import com.artisansoftware.luminaries.Style._
import com.artisansoftware.luminaries.Twitter._

class CommandLineParser(args: Array[String]) {
  private val DefaultHours = 2
  private val argsExcludingSwitches = args.filterNot(_.startsWith("-"))
  private val switches = args.filter(_.startsWith("-")).
    flatMap((switch) => switch.substring(1).toCharArray)
  private val filter = (t: Tweet) =>
    (!t.isRetweet || switches.contains('r')) &&
      (t.getInReplyToUserId == -1 || switches.contains('c'))

  def execute: String =
    if (switches.contains('h'))
      help
    else if (switches.contains('l'))
      luminaries
    else if (switches.contains('a'))
      addLuminary()
    else if (switches.contains('d'))
      deleteLuminary()
    else {
      if (switches.contains('s')) streamTweets()
      listTweets
    }

  def help: String = List(
    "",
    "Usage",
    "\tnews -h                          Display this help",
    "\tnews -l                          List luminaries",
    "\tnews -a [twitter handle] [name]  Add a new luminary",
    "\tnews -d [twitter handle]         Delete a luminary",
    "\tnews [switches] [hours]          Display luminary tweets",
    "where switches are",
    "\t-c                               Conversation tweets (default is off)",
    "\t-r                               Retweets (default is off)",
    "\t-s                               Maintain live stream of results",
    "") mkString "\r\n"

  def luminaries: String =
    Repository.luminaries.map(format) mkString "\r\n"

  def addLuminary(): String = {
    val twitterHandle = argsExcludingSwitches(0)
    val name = argsExcludingSwitches.tail mkString " "
    Repository + Luminary(name, twitterHandle)
    f"Added luminary $Bold$name$EndStyle with twitter handle $Bold$twitterHandle$EndStyle"
  }

  def deleteLuminary(): String = {
    val twitterHandle = argsExcludingSwitches(0)
    val luminary: Luminary = Luminary(null, twitterHandle)
    if (Repository.contains(luminary)) {
      Repository - luminary
      f"Removed luminary with twitter handle $Bold$twitterHandle$EndStyle"
    } else
      f"${Red}There is no luminary with twitter handle $Bold$twitterHandle$EndStyle$EndColour"
  }

  def streamTweets(): Unit =
    Twitter.tweetStream(new TwitterRequest(Repository.luminaries, hours, filter), printSingle)

  def listTweets: String =
    formatAll(Twitter.tweets(new TwitterRequest(Repository.luminaries, hours, filter)))

  def hours: Int =
    if (argsExcludingSwitches.length >= 1) argsExcludingSwitches(0).toInt else DefaultHours

  def formatAll(result: Map[Luminary, List[Tweet]]): String =
    (for ((luminary, tweets) <- result) yield format(luminary, tweets)) mkString "\r\n"

  def format(luminary: Luminary, tweets: List[Tweet]): String =
    "" :: header(luminary) :: "" :: tweets.map(TweetFormatter.format) mkString "\r\n"

  def format(luminary: Luminary): String =
    f"$Bold${luminary.name}%-28s$EndStyle ${luminary.twitterHandle}"

  def printSingle(luminary: Luminary, tweet: Tweet): Unit = {
    println(header(luminary))
    println(TweetFormatter.format(tweet))
  }

  def header(luminary: Luminary): String =
    f"$GreyBackground${luminary.name.toUpperCase}%28s  $EndStyle"
}
