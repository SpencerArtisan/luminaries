package com.artisansoftware.luminaries

import java.util.Date

import com.artisansoftware.luminaries.Twitter._
import org.junit.runner.RunWith
import org.mockito.Mockito._
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner
import org.scalatest.mock.MockitoSugar

@RunWith(classOf[JUnitRunner])
class TweetFormatterTest extends FunSuite with MockitoSugar {
  test("plain tweet") {
    val tweet = mock[Tweet]
    when(tweet.getText).thenReturn("a tweet")
    when(tweet.getCreatedAt).thenReturn(new Date(2016, 1, 2, 3, 4, 5))
    assert(TweetFormatter.format(tweet) === "\u001B[1mWed Feb 02 03:04\u001B[0m   a tweet")
  }

  test("tweet with link at end") {
    val tweet = mock[Tweet]
    when(tweet.getText).thenReturn("a link https://t.co/NFO0lamzvz")
    when(tweet.getCreatedAt).thenReturn(new Date(2016, 1, 2, 3, 4, 5))
    assert(TweetFormatter.format(tweet) === "\u001B[1mWed Feb 02 03:04\u001B[0m   a link \u001B[34m\u001B[4mhttps://t.co/NFO0lamzvz\u001B[0m\u001B[39m")
  }

  test("tweet with link in middle") {
    val tweet = mock[Tweet]
    when(tweet.getText).thenReturn("a link https://t.co/NFO0lamzvz in middle")
    when(tweet.getCreatedAt).thenReturn(new Date(2016, 1, 2, 3, 4, 5))
    assert(TweetFormatter.format(tweet) === "\u001B[1mWed Feb 02 03:04\u001B[0m   a link \u001B[34m\u001B[4mhttps://t.co/NFO0lamzvz\u001B[0m\u001B[39m in middle")
  }
}