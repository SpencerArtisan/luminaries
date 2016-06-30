package com.artisansoftware.luminaries

import twitter4j.Status

object Luminary {
  val luminaries = List(
    Luminary("Michael Crick", "MichaelLCrick", 185306056),
    Luminary("Justin Webb", "JustinOnWeb", 179887262),
    Luminary("Nick Robinson", "bbcnickrobinson", 25984418),
    Luminary("Jon Snow", "jonsnowC4", 128216887),
    Luminary("Katya Adler", "BBCkatyaadler", 393637870),
    Luminary("David Grossman", "davidgrossman", 271815348),
    Luminary("Laura Kuennsberg", "bbclaurak", 61183568),
    Luminary("Eddie Mair", "eddiemair", 304510244),
    Luminary("Sean Curran", "cripeswatson", 226542784),
    Luminary("Kamal Ahmend", "bbckamal", 79292795),
    Luminary("Chris Mason", "ChrisMasonBBC", 22460658),
    Luminary("Tim Harford", "TimHarford", 32493647),
    Luminary("Stephanie Flanders", "MyStephanomics", 259288589),
    Luminary("Newsnight", "BBCNewsnight", 20543416),
    Luminary("Andrew Rawnsley", "andrewrawnsley", 78433570),
    Luminary("Steve Richards", "steverichards14", 159454866),
    Luminary("Polly Toynbee", "pollytoynbee", 226195230),
    Luminary("Norman Smith", "BBCNormanS", 106118793),
    Luminary("Tim Montgomerie", "montie", 16139649),
    Luminary("Jim Pickard", "PickardJE", 69080476),
    Luminary("Robert Peston", "Peston", 14157134))
}

case class Luminary(name: String, twitterHandle: String, userId: Long) {
}
