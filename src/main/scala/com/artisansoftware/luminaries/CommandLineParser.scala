package com.artisansoftware.luminaries

import com.artisansoftware.luminaries.Style._
import com.artisansoftware.luminaries.Twitter._

class CommandLineParser(args: Array[String]) {
  val DefaultHours = 2
  val argsExcludingSwitches = args.filterNot(_.startsWith("-"))
  val switches: Array[Char] = args.filter(_.startsWith("-")).
    flatMap((switch) => switch.substring(1).toCharArray)

  def execute: String = {
    val filter = (t: Tweet) =>
        (!t.isRetweet || switches.contains('r')) &&
          (t.getInReplyToUserId == -1 || switches.contains('c'))

    if (switches.contains('h'))
      help
    else if (switches.contains('l'))
      luminaries
    else {
      if (switches.contains('s')) streamTweets(filter)
      listTweets(filter)
    }
  }

  def help: String = List(
    "",
    "Usage",
    "\tnews -h                  Display this help",
    "\tnews -l                  List luminaries",
    "\tnews [switches] [hours]  Display luminary tweets",
    "where switches are",
    "\t-c                       Conversation tweets (default is off)",
    "\t-r                       Retweets (default is off)",
    "\t-s                       Maintain live stream of results",
    "") mkString "\r\n"

  def luminaries: String =
    Luminary.luminaries.map(format) mkString "\r\n"

  def streamTweets(filter: Tweet => Boolean): Unit =
    Twitter.tweetStream(new TwitterRequest(Luminary.luminaries, hours, filter), printSingle)

  def listTweets(filter: Tweet => Boolean): String =
    formatAll(Twitter.tweets(new TwitterRequest(Luminary.luminaries, hours, filter)))

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
