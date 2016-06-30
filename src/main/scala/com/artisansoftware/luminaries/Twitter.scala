package com.artisansoftware.luminaries

import java.util.Date

import twitter4j.conf.ConfigurationBuilder
import twitter4j.{Query, QueryResult, Status, TwitterFactory}

import scala.collection.JavaConversions._

object Twitter {
  type Tweet = Status

  val Boring = List("soccer", "football", "sport")

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
      groupBy(tweet => matchLuminary(tweet, request.luminaries))
  }

  private def buildQuery(request: TwitterRequest): Query =
    new Query(request.luminaries.map("from:" + _.twitterHandle) mkString " OR ").count(100)

  private def matchLuminary(tweet: Tweet, luminaries: List[Luminary]): Luminary = {
    val tweeter = luminaries.find(tweeter => tweeter.twitterHandle.equals(tweet.getUser.getScreenName))
    if (tweeter.isEmpty)
      throw new Exception("Could not find a luminary with handle " + tweet.getUser.getScreenName)

    tweeter.get
  }

  private def withinHours(tweet: Tweet, hours: Int): Boolean =
    new Date().getTime - tweet.getCreatedAt.getTime < hours * (60 * 60 * 1000)

  private def isInteresting(tweet: Tweet, request: TwitterRequest) =
    tweet.getInReplyToStatusId == -1 && request.accept(tweet) && Boring.forall(!tweet.getText.contains(_))
}
