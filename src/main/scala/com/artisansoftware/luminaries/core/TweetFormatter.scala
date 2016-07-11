package com.artisansoftware.luminaries.core

import com.artisansoftware.luminaries.core.RichTextImplicits._

object TweetFormatter {
  val HeaderWidth = 24
  
  def format(tweets: List[Tweet], luminaries: List[Luminary]): RichText = {
    val tweetsByLuminary = tweets.groupBy(tweet => matchLuminaryStrict(tweet, luminaries))
    (for ((luminary, tweets) <- tweetsByLuminary) yield format(tweets, luminary)).foldLeft(RichText())(_ + "\n" + _)
  }

  def formatCompact(tweets: List[Tweet], luminaries: List[Luminary]): RichText =
    (for (tweet <- tweets.reverse) yield formatCompact(tweet, luminaries)).foldLeft(RichText())(_ + _)

  def formatCompact(tweet: Tweet, luminaries: List[Luminary]): RichText =
    header(matchLuminaryStrict(tweet, luminaries)) + "  " + format(tweet) + "\n"

  private def format(tweets: List[Tweet], luminary: Luminary): RichText =
    header(luminary) + tweets.reverse.map(format).foldLeft("\n")(_ + "\n" + _) + "\n"

  private def header(luminary: Luminary): RichText =
    luminary.name.toUpperCase.greyBackground.padLeft(HeaderWidth) + "  ".greyBackground

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
