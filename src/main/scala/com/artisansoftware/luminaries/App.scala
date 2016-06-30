package com.artisansoftware.luminaries

import twitter4j.Status
import Twitter._


/**
  * Created by spencerward on 29/06/2016.
  */
object App {
  def main(args: Array[String]) {
    val results = Twitter.tweets(new CommandLineParser(args).toRequest)
    val output = for ((luminary, tweets) <- results) yield format(luminary, tweets)
    println(output mkString "\r\n")
  }

  def format(luminary: Luminary, tweets: List[Tweet]): String =
    "" :: header(luminary) :: "" :: tweets.map(TweetFormatter.format) mkString "\r\n"

  def header(luminary: Luminary): String =
    f"\u001B[47m${luminary.name.toUpperCase}%28s  \u001B[0m"

//    for (i <- 1 to 80) {
//      println("\u001B[" + i + "mhello world " + i + "!\u001B[39m")
//    }
}
