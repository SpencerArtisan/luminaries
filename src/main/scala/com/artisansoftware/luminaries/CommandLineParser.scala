package com.artisansoftware.luminaries

import Twitter._
import Style._

class CommandLineParser(args: Array[String]) {
  type Command = () => String

  val DefaultHours = 2
  val argsExcludingSwitches = args.filterNot(_.startsWith("-"))
  val switches = args.filter(_.startsWith("-"))

  def buildCommand: Command =
    if (switches.contains("-h"))
      () => help
    else if (switches.contains("-r"))
      () => listTweets(true)
    else
      () => listTweets(false)

  def help: String = List(
    "news -h           Display this help",
    "news [hours]      Display luminary tweets",
    "news -r [hours]   Display luminary retweets") mkString "\r\n"

  def listTweets(retweets: Boolean): String = {
    val hours = if (argsExcludingSwitches.length >= 1) argsExcludingSwitches(0).toInt else DefaultHours
    formatAll(Twitter.tweets(new TwitterRequest(Luminary.luminaries, hours, retweets)))
  }

  def formatAll(result: Map[Luminary, List[Tweet]]): String =
    (for ((luminary, tweets) <- result) yield format(luminary, tweets)) mkString "\r\n"

  def format(luminary: Luminary, tweets: List[Tweet]): String = {
    val header = f"$GreyBackground${luminary.name.toUpperCase}%28s  $EndStyle"
    "" :: header :: "" :: tweets.map(TweetFormatter.format) mkString "\r\n"
  }
}
