package com.artisansoftware.luminaries

import Twitter._
import Style._

class CommandLineParser(args: Array[String]) {
  type Command = () => String

  val luminaries = List(
    Luminary("Michael Crick", "MichaelLCrick"),
    Luminary("Justin Webb", "JustinOnWeb"),
    Luminary("Nick Robinson", "bbcnickrobinson"),
    Luminary("Jon Snow", "jonsnowC4"),
    Luminary("Katya Adler", "BBCkatyaadler"),
    Luminary("David Grossman", "davidgrossman"),
    Luminary("Laura Kuennsberg", "bbclaurak"),
    Luminary("Eddie Mair", "eddiemair"),
    Luminary("Sean Curran", "cripeswatson"),
    Luminary("Kamal Ahmend", "bbckamal"),
    Luminary("Chris Mason", "ChrisMasonBBC"),
    Luminary("Tim Harford", "TimHarford"),
    Luminary("Stephanie Flanders", "MyStephanomics"),
    Luminary("Newsnight", "BBCNewsnight"),
    Luminary("Andrew Rawnsley", "andrewrawnsley"),
    Luminary("Steve Richards", "steverichards14"),
    Luminary("Polly Toynbee", "pollytoynbee"),
    Luminary("Norman Smith", "BBCNormanS"),
    Luminary("Tim Montgomerie", "montie"),
    Luminary("Jim Pickard", "PickardJE"),
    Luminary("Robert Peston", "Peston"))

  val DefaultHours = 2

  def buildCommand: Command = {
    () => {
      def format(luminary: Luminary, tweets: List[Tweet]): String = {
        val header = f"$GreyBackground${luminary.name.toUpperCase}%28s  $EndStyle"
        "" :: header :: "" :: tweets.map(TweetFormatter.format) mkString "\r\n"
      }

      val hours = if (args.length >= 1) args(0).toInt else DefaultHours
      val result: Map[Luminary, List[Tweet]] = Twitter.tweets(new TwitterRequest(luminaries, hours))
      (for ((luminary, tweets) <- result) yield format(luminary, tweets)) mkString "\r\n"
    }
  }
}
