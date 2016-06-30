package com.artisansoftware.luminaries

import Twitter._
import Style._

class CommandLineParser(args: Array[String]) {
  type Command = () => String

  val DefaultHours = 2

  def buildCommand: Command =
    if (args(0) == "-h")
      () => help
    else
      () => listTweets

  def help: String = List(
    f"news -h        Display this help",
    f"news [hours]   Display luminary tweets") mkString "\r\n"

  def listTweets: String = {
    def format(luminary: Luminary, tweets: List[Tweet]): String = {
      val header = f"$GreyBackground${luminary.name.toUpperCase}%28s  $EndStyle"
      "" :: header :: "" :: tweets.map(TweetFormatter.format) mkString "\r\n"
    }

    val hours = if (args.length >= 1) args(0).toInt else DefaultHours
    val result: Map[Luminary, List[Tweet]] = Twitter.tweets(new TwitterRequest(Luminary.luminaries, hours))
    (for ((luminary, tweets) <- result) yield format(luminary, tweets)) mkString "\r\n"
  }
}
