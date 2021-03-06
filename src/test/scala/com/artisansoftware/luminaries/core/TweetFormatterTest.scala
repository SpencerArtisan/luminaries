package com.artisansoftware.luminaries.core

import org.mockito.Mockito._
import org.scalatest._
import org.scalatest.mockito.MockitoSugar
import org.mockito.Mockito.withSettings
import org.mockito.Answers._
import org.mockito.internal.stubbing.defaultanswers.ReturnsDeepStubs

class TweetFormatterTest extends FunSpec with Inside with Matchers with MockitoSugar {
  describe("empty tweet list") {
    val text = TweetFormatter.format(List(), List())

    it("should have no text") {
      text should be (RichText())
    }
  }

  describe("compact single tweet format") {
    val tweet = mock[Tweet] (withSettings.defaultAnswer(new ReturnsDeepStubs()))
    when(tweet.getText).thenReturn("tweet text")
    when(tweet.getUser.getScreenName).thenReturn("peston")
    val luminary: Luminary = Luminary("Robert Peston", "peston")
    val text = TweetFormatter.formatCompact(tweet, List(luminary))

    it("should include the luminary name in upper case") {
      text.toString should include ("ROBERT PESTON")
    }

    it("should include the tweet") {
      text.toString should include ("tweet text")
    }

    it("should take up 1 lines") {
      "\n".r.findAllMatchIn(text.toString).length should be (1)
    }
  }

  describe("compact multiple tweet format") {
    val tweet1 = mock[Tweet] (withSettings.defaultAnswer(new ReturnsDeepStubs()))
    when(tweet1.getText).thenReturn("tweet1 text")
    when(tweet1.getUser.getScreenName).thenReturn("peston")
    val luminary1: Luminary = Luminary("Robert Peston", "peston")
    val tweet2 = mock[Tweet] (withSettings.defaultAnswer(new ReturnsDeepStubs()))
    when(tweet2.getText).thenReturn("tweet2 text")
    when(tweet2.getUser.getScreenName).thenReturn("crick")
    val luminary2: Luminary = Luminary("Michael Crick", "crick")

    val text = TweetFormatter.formatCompact(List(tweet1,tweet2), List(luminary1, luminary2))

    it("should include the luminary names in upper case") {
      text.toString should include ("ROBERT PESTON")
      text.toString should include ("MICHAEL CRICK")
    }

    it("should include the tweets") {
      text.toString should include ("tweet1 text")
      text.toString should include ("tweet2 text")
    }

    it("should take up 2 lines") {
      "\n".r.findAllMatchIn(text.toString).length should be (2)
    }
  }

  describe("single tweet list") {
    val tweet = mock[Tweet] (withSettings.defaultAnswer(new ReturnsDeepStubs()))
    when(tweet.getText).thenReturn("tweet text")
    when(tweet.getUser.getScreenName).thenReturn("peston")
    val luminary: Luminary = Luminary("Robert Peston", "peston")
    val text = TweetFormatter.format(List(tweet), List(luminary))

    it("should include the luminary name in upper case") {
      text.toString should include ("ROBERT PESTON")
    }

    it("should include the tweet") {
      text.toString should include ("tweet text")
    }

    it("should take up 4 lines") {
      "\n".r.findAllMatchIn(text.toString).length should be (4)
    }
  }

  describe("multiple tweet list") {
    val tweet1 = mock[Tweet](withSettings.defaultAnswer(new ReturnsDeepStubs()))
    when(tweet1.getText).thenReturn("the pound in sinking")
    when(tweet1.getUser.getScreenName).thenReturn("peston")
    val tweet2 = mock[Tweet](withSettings.defaultAnswer(new ReturnsDeepStubs()))
    when(tweet2.getText).thenReturn("the pesos falling")
    when(tweet2.getUser.getScreenName).thenReturn("jonsnow")
    val luminary1: Luminary = Luminary("Robert Peston", "peston")
    val luminary2: Luminary = Luminary("Jon Snow", "jonsnow")
    val text = TweetFormatter.format(List(tweet1, tweet2), List(luminary1, luminary2))

    it("should include the luminary names in upper case") {
      text.toString should include ("ROBERT PESTON")
      text.toString should include ("JON SNOW")
    }

    it("should include the tweets") {
      text.toString should include ("the pound in sinking")
      text.toString should include ("the pesos falling")
    }

    it("should take up 8 lines") {
      "\n".r.findAllMatchIn(text.toString).length should be (8)
    }
  }

}
