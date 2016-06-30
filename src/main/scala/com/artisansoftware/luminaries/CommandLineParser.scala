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
    else if (switches.contains("-c"))
      () => listTweets(_.getInReplyToUserId != -1)
    else if (switches.contains("-r"))
      () => listTweets(_.isRetweet)
    else
      () => listTweets(!_.isRetweet)

  def help: String = List(
    "",
    "\tnews -h           Display this help",
    "\tnews [hours]      Display luminary tweets",
    "\tnews -c [hours]   Display luminary conversation tweets",
    "\tnews -r [hours]   Display luminary retweets",
    "") mkString "\r\n"

  def listTweets(filter: Tweet => Boolean): String = {
    val hours = if (argsExcludingSwitches.length >= 1) argsExcludingSwitches(0).toInt else DefaultHours
    formatAll(Twitter.tweets(new TwitterRequest(Luminary.luminaries, hours, filter)))
  }

  def formatAll(result: Map[Luminary, List[Tweet]]): String =
    (for ((luminary, tweets) <- result) yield format(luminary, tweets)) mkString "\r\n"

  def format(luminary: Luminary, tweets: List[Tweet]): String = {
    val header = f"$GreyBackground${luminary.name.toUpperCase}%28s  $EndStyle"
    "" :: header :: "" :: tweets.map(TweetFormatter.format) mkString "\r\n"
  }
}
