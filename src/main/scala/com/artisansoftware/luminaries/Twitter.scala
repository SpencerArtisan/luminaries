package com.artisansoftware.luminaries

import java.util.Date

import twitter4j.conf.ConfigurationBuilder
import twitter4j.{TwitterStreamFactory, _}

import scala.collection.JavaConversions._

object Twitter {

  type Tweet = Status

  val Boring = List("soccer", "football", "sport")

  val luminaryIds = Luminary.luminaries.map(_.userId)

  val token = new TwitterFactory(
    new ConfigurationBuilder().
      setApplicationOnlyAuthEnabled(true).
      build()).getInstance().getOAuth2Token

  val twitter = new TwitterFactory(
    new ConfigurationBuilder().
      setApplicationOnlyAuthEnabled(true).
      setOAuth2TokenType(token.getTokenType).
      setOAuth2AccessToken(token.getAccessToken).
      build()).getInstance()

  val twitterStream = new TwitterStreamFactory(
    new ConfigurationBuilder().
      build()).getInstance()

  def tweets(request: TwitterRequest): Map[Luminary, List[Tweet]] = {
    var query: Query = buildQuery(request)
    var result: QueryResult = twitter.search().search(query)
    var allTweets = List[Tweet]()
    allTweets = allTweets ++ result.getTweets

    do {
      query = result.nextQuery()
      result = twitter.search().search(query)
      allTweets = allTweets ++ result.getTweets
    } while (result.getTweets.nonEmpty && withinHours(result.getTweets.last, request.hours))

    allTweets.
      filter(withinHours(_, request.hours)).
      filter(isInteresting(_, request)).
      groupBy(tweet => matchLuminaryStrict(tweet, request.luminaries))
  }

  def tweetStream(request: TwitterRequest, consumer: (Luminary, Tweet) => Unit): Unit = {
    twitterStream.addListener(new StatusAdapter() {
      override def onStatus(tweet: Tweet): Unit = {
        val luminary: Option[Luminary] = matchLuminary(tweet, request.luminaries)
        if (luminary.isDefined && isInteresting(tweet, request))
          consumer(luminary.get, tweet)
      }
    })
    twitterStream.filter(new FilterQuery().follow(luminaryIds:_*))
  }

  private def buildQuery(request: TwitterRequest): Query =
    new Query(request.luminaries.map("from:" + _.twitterHandle) mkString " OR ").count(100)

  private def matchLuminaryStrict(tweet: Tweet, luminaries: List[Luminary]): Luminary = {
    val tweeter: Option[Luminary] = matchLuminary(tweet, luminaries)
    if (tweeter.isEmpty)
      throw new Exception("Could not find a luminary with handle " + tweet.getUser.getScreenName)

    tweeter.get
  }

  def matchLuminary(tweet: Tweet, luminaries: List[Luminary]): Option[Luminary] =
    luminaries.find(tweeter => tweeter.twitterHandle.equals(tweet.getUser.getScreenName))

  private def withinHours(tweet: Tweet, hours: Int): Boolean =
    new Date().getTime - tweet.getCreatedAt.getTime < hours * (60 * 60 * 1000)

  private def isInteresting(tweet: Tweet, request: TwitterRequest) =
    tweet.getInReplyToStatusId == -1 && request.accept(tweet) && Boring.forall(!tweet.getText.contains(_))
}
