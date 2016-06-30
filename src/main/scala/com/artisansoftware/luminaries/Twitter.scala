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
//      setOAuthConsumerKey("AggzH4Q1uR4SVsxMmRNqHQnwv").
//      setOAuthConsumerSecret("Nqy9nCe5F1TOkGVlJfE6aG9DiRqy80vS9AbVDZczX7FGffLOIz").
      build()).getInstance().getOAuth2Token

  val twitter = new TwitterFactory(
    new ConfigurationBuilder().
      setApplicationOnlyAuthEnabled(true).
//      setOAuthConsumerKey("AggzH4Q1uR4SVsxMmRNqHQnwv").
//      setOAuthConsumerSecret("Nqy9nCe5F1TOkGVlJfE6aG9DiRqy80vS9AbVDZczX7FGffLOIz").
      setOAuth2TokenType(token.getTokenType).
      setOAuth2AccessToken(token.getAccessToken).
      build()).getInstance()

  def tweets(request: TwitterRequest): Map[Luminary, List[Tweet]] = {
    var query: Query = buildQuery(request.luminaries, request.hours)
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
      filter(isInteresting).
      groupBy(tweet => matchLuminary(tweet, request.luminaries))
  }

  private def buildQuery(luminaries: List[Luminary], hours: Int): Query =
    new Query(luminaries.map("from:" + _.twitterHandle) mkString " OR ").count(100)

  private def matchLuminary(tweet: Tweet, luminaries: List[Luminary]): Luminary = {
    val tweeter = luminaries.find(tweeter => tweeter.twitterHandle.equals(tweet.getUser.getScreenName))
    if (tweeter.isEmpty)
      throw new Exception("Could not find a luminary with handle " + tweet.getUser.getScreenName)

    tweeter.get
  }

  private def withinHours(tweet: Tweet, hours: Int): Boolean =
    new Date().getTime - tweet.getCreatedAt.getTime < hours * (60 * 60 * 1000)

  private def isInteresting(tweet: Tweet) =
    tweet.getInReplyToStatusId == -1 && !tweet.isRetweet && Boring.forall(!tweet.getText.contains(_))
}
