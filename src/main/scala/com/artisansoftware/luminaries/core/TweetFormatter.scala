package com.artisansoftware.luminaries.core

import com.artisansoftware.luminaries.core.RichTextImplicits._
import com.artisansoftware.luminaries.io.Twitter.Tweet

object TweetFormatter {
  def format(tweets: List[Tweet], luminaries: List[Luminary]): RichText = {
    val tweetsByLuminary = tweets.groupBy(tweet => matchLuminaryStrict(tweet, luminaries))
    (for ((luminary, tweets) <- tweetsByLuminary) yield format(luminary, tweets)).foldLeft("")(_ + _)
  }

  private def format(luminary: Luminary, tweets: List[Tweet]): RichText =
    "\n\n" + header(luminary) + tweets.map(format).foldLeft("\n")(_ + "\n" + _)

  private def header(luminary: Luminary): RichText =
    luminary.name.toUpperCase.greyBackground.padLeft(28) + "  ".greyBackground

  private def format(tweet: Tweet): RichText = {
    val date = f"${tweet.getCreatedAt}%ta %<tb %<td %<tR"
    val body = "(http[^\\s]*)".r.replaceAllIn(tweet.getText, _.group(0).blue.underline.toString)
    date.bold + "   " + body
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
