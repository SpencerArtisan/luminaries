package com.artisansoftware.luminaries

import java.lang.Exception

import twitter4j.Status
import Twitter._
import Style._

object App {
  def main(args: Array[String]) =
    try {
      println(new CommandLineParser(args).execute)
    } catch {
      case _: Throwable => println(f"$Red  News unavailable.  Are you offline?  $EndColour")
    }

  //  for (i <- 1 to 80) {
  //    println("\u001B[" + i + "mhello world " + i + "!\u001B[39m")
  //  }
}
