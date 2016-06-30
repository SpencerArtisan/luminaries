package com.artisansoftware.luminaries

import twitter4j.Status

object Luminary {
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
}
case class Luminary(name: String, twitterHandle: String) {
}
