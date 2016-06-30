package com.artisansoftware.luminaries

/**
  * Created by spencerward on 30/06/2016.
  */
class CommandLineParser(args: Array[String]) {
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

  def toRequest: TwitterRequest = {
    val hours = if (args.length >= 1) args(0).toInt else DefaultHours
    new TwitterRequest(luminaries, hours)
  }

}
