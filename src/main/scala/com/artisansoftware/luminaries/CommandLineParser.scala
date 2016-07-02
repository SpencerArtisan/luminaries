package com.artisansoftware.luminaries

import com.artisansoftware.luminaries.Style._
import com.artisansoftware.luminaries.Twitter._

class CommandLineParser(args: Array[String]) {
  private val DefaultHours = 2
  private val argsExcludingSwitches = args.filterNot(_.startsWith("-"))
  private val switches = args.filter(_.startsWith("-")).
    flatMap((switch) => switch.substring(1).toCharArray)

  def execute: String =
    if (switches.contains('h'))
      help
    else if (switches.contains('l'))
      luminaries
    else if (switches.contains('a'))
      addLuminary()
    else if (switches.contains('d'))
      deleteLuminary()
    else if (Repository.luminaries.isEmpty)
      f"${Red}Please add some luminaries$EndColour"
    else {
      if (switches.contains('s')) streamTweets()
      listTweets
    }

  private def help: String = List(
    "",
    "Command Arguments:",
    "\t[number of hours] [keywords]",
    "",
    "Switches:",
    "\t[without switches]          Display recent tweets",
    "\t-c                          Include conversation tweets (default is off)",
    "\t-r                          Include retweets (default is off)",
    "\t-s                          Maintain live stream of results",
    "\t-l                          List luminaries",
    "\t-a [twitter handle] [name]  Add a new luminary",
    "\t-d [twitter handle]         Delete a luminary",
    "\t-h                          Display this help",
    "") mkString "\r\n"

  private def luminaries: String =
    Repository.luminaries.map(format) mkString "\r\n"

  private def addLuminary(): String = {
    val twitterHandle = argsExcludingSwitches(0)
    val name = argsExcludingSwitches.tail mkString " "
    Repository + Luminary(name, twitterHandle)
    f"Added luminary $Bold$name$EndStyle with twitter handle $Bold$twitterHandle$EndStyle"
  }

  private def deleteLuminary(): String = {
    val twitterHandle = argsExcludingSwitches(0)
    val luminary: Luminary = Luminary(null, twitterHandle)
    if (Repository.contains(luminary)) {
      Repository - luminary
      f"Removed luminary with twitter handle $Bold$twitterHandle$EndStyle"
    } else
      f"${Red}There is no luminary with twitter handle $Bold$twitterHandle$EndStyle$EndColour"
  }

  private def streamTweets(): Unit =
    Twitter.tweetStream(new TwitterRequest(Repository.luminaries, hours, filter), printSingle)

  private def listTweets: String =
    formatAll(Twitter.tweets(new TwitterRequest(Repository.luminaries, hours, filter)))

  private def hours: Int =
    if (argsExcludingSwitches.length >= 1 && argsExcludingSwitches(0).forall(_.isDigit)) argsExcludingSwitches(0).toInt else DefaultHours

  private def filter(t: Tweet): Boolean =
    (!t.isRetweet || switches.contains('r')) &&
      (t.getInReplyToUserId == -1 || switches.contains('c')) &&
      (keywords.isEmpty || keywords.exists(t.getText.toUpperCase.contains(_)))

  private def keywords: Array[String] =
    argsExcludingSwitches.filterNot(_.forall(_.isDigit)).map(_.toUpperCase)

  private def formatAll(result: Map[Luminary, List[Tweet]]): String =
    (for ((luminary, tweets) <- result) yield format(luminary, tweets)) mkString "\r\n"

  private def format(luminary: Luminary, tweets: List[Tweet]): String =
    "" :: header(luminary) :: "" :: tweets.map(TweetFormatter.format) mkString "\r\n"

  private def format(luminary: Luminary): String =
    f"$Bold${luminary.name}%-28s$EndStyle ${luminary.twitterHandle}"

  private def printSingle(luminary: Luminary, tweet: Tweet): Unit = {
    println(header(luminary))
    println(TweetFormatter.format(tweet))
  }

  private def header(luminary: Luminary): String =
    f"$GreyBackground${luminary.name.toUpperCase}%28s  $EndStyle"
}
