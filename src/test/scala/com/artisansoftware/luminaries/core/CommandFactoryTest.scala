package com.artisansoftware.luminaries.core

import org.mockito.Mockito._
import org.scalatest._
import org.scalatest.mockito.MockitoSugar

class CommandFactoryTest extends FunSpec with Inside with Matchers with MockitoSugar {

  describe("CommandFactory") {
    val standardTweet = mock[Tweet]
    val retweet = mock[Tweet]
    val replyTweet = mock[Tweet]

    when(standardTweet.isRetweet).thenReturn(false)
    when(standardTweet.getInReplyToUserId).thenReturn(-1)
    when(standardTweet.getText).thenReturn("tweet text")
    when(retweet.isRetweet).thenReturn(true)
    when(retweet.getInReplyToUserId).thenReturn(-1)
    when(retweet.getText).thenReturn("tweet text")
    when(replyTweet.isRetweet).thenReturn(false)
    when(replyTweet.getInReplyToUserId).thenReturn(42)
    when(replyTweet.getText).thenReturn("tweet text")

    it("should generate a help command") {
      val command = CommandFactory.build(CommandLine(Array("-h")))
      inside(command) { case SimpleCommand(text) =>
        text.toString should include ("help")
      }
    }

    it("should generate a read luminaries command") {
      val command = CommandFactory.build(CommandLine(Array("-l")))
      command should matchPattern { case ReadLuminariesCommand => }
    }

    it("should generate an add luminary command") {
      val command = CommandFactory.build(CommandLine(Array("-a", "handle", "full", "name")))
      command should matchPattern { case AddLuminaryCommand("handle", "full name") => }
    }

    it("should generate an delete luminary command") {
      val command = CommandFactory.build(CommandLine(Array("-d", "handle")))
      command should matchPattern { case DeleteLuminaryCommand("handle") => }
    }

    it("should generate a stream tweets command") {
      val command = CommandFactory.build(CommandLine(Array("-s")))
      inside(command) { case StreamTweetsCommand(hours, filter) =>
        hours should be > 0
        filter(standardTweet) should be (true)
        filter(retweet) should be (false)
        filter(replyTweet) should be (false)
      }
    }

    it("should generate a stream tweets command with replies and retweets included") {
      val command = CommandFactory.build(CommandLine(Array("-src", "42")))
      inside(command) { case StreamTweetsCommand(hours, filter) =>
        hours should be (42)
        filter(standardTweet) should be (true)
        filter(retweet) should be (true)
        filter(replyTweet) should be (true)
      }
    }

    it("should generate a read tweets command with default hours and filter") {
      val command = CommandFactory.build(CommandLine(Array()))
      inside(command) { case ReadTweetsCommand(hours, filter, _) =>
        hours should be > 0
        filter(standardTweet) should be (true)
        filter(retweet) should be (false)
        filter(replyTweet) should be (false)
      }
    }

    it("should generate a read tweets command with retweets included") {
      val command = CommandFactory.build(CommandLine(Array("-r")))
      inside(command) { case ReadTweetsCommand(_, filter, _) =>
        filter(standardTweet) should be (true)
        filter(retweet) should be (true)
        filter(replyTweet) should be (false)
      }
    }

    it("should generate a read tweets command with replies included") {
      val command = CommandFactory.build(CommandLine(Array("-c")))
      inside(command) { case ReadTweetsCommand(_, filter, _) =>
        filter(standardTweet) should be (true)
        filter(retweet) should be (false)
        filter(replyTweet) should be (true)
      }
    }

    it("should generate a read tweets command with replies and retweets included") {
      val command = CommandFactory.build(CommandLine(Array("-cr")))
      inside(command) { case ReadTweetsCommand(_, filter, _) =>
        filter(standardTweet) should be (true)
        filter(retweet) should be (true)
        filter(replyTweet) should be (true)
      }
    }

    it("should generate a read tweets command with keywords included") {
      val command = CommandFactory.build(CommandLine(Array("tweet")))
      inside(command) { case ReadTweetsCommand(_, filter, _) =>
        filter(standardTweet) should be (true)
      }
    }

    it("should generate a read tweets command with missing keywords excluded") {
      val command = CommandFactory.build(CommandLine(Array("wibble")))
      inside(command) { case ReadTweetsCommand(_, filter, _) =>
        filter(standardTweet) should be (false)
      }
    }

    it("should generate a read tweets command with boring tweets excluded") {
      val boringTweet = mock[Tweet]
      when(boringTweet.isRetweet).thenReturn(false)
      when(boringTweet.getInReplyToUserId).thenReturn(-1)
      when(boringTweet.getText).thenReturn("something about football...yawn")

      val command = CommandFactory.build(CommandLine(Array()))
      inside(command) { case ReadTweetsCommand(_, filter, _) =>
        filter(boringTweet) should be (false)
      }
    }

    it("should generate a read tweets command with specified hours") {
      val command = CommandFactory.build(CommandLine(Array("42")))
      command should matchPattern { case ReadTweetsCommand(42, _, false) => }
    }

    it("should generate a read tweets command in timeline format") {
      val command = CommandFactory.build(CommandLine(Array("-t")))
      command should matchPattern { case ReadTweetsCommand(_, _, true) => }
    }
  }
}
