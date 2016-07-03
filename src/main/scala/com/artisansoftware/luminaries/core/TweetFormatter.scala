package com.artisansoftware.luminaries.core

import com.artisansoftware.luminaries.core.RichTextImplicits._
import com.artisansoftware.luminaries.io.Twitter.Tweet

object TweetFormatter {
  def format(tweets: List[Tweet], luminaries: List[Luminary]): List[Line] = {
    val tweetsByLuminary = tweets.groupBy(tweet => matchLuminaryStrict(tweet, luminaries))
    (for ((luminary, tweets) <- tweetsByLuminary) yield format(luminary, tweets)).flatten.toList
  }

  private def format(luminary: Luminary, tweets: List[Tweet]): List[Line] =
    new Line() :: header(luminary) :: new Line() :: tweets.map(format)

  private def header(luminary: Luminary): Line =
    new Line(luminary.name.toUpperCase.greyBackground.padLeft(28), "  ".greyBackground)

  private def format(tweet: Tweet): Line = {
    val date = f"${tweet.getCreatedAt}%ta %<tb %<td %<tR"
    val body = "(http[^\\s]*)".r.replaceAllIn(tweet.getText, _.group(0).blue.underline.toString)
    new Line(date.bold, "   ", body)
  }

  private def matchLuminaryStrict(tweet: Tweet, luminaries: List[Luminary]): Luminary = {
    val tweeter: Option[Luminary] = matchLuminary(tweet, luminaries)
    if (tweeter.isEmpty)
      throw new Exception("Could not find a luminary with handle " + tweet.getUser.getScreenName)

    tweeter.get
  }

  private def matchLuminary(tweet: Tweet, luminaries: List[Luminary]): Option[Luminary] =
    luminaries.find(tweeter => tweeter.twitterHandle.equals(tweet.getUser.getScreenName))

}
