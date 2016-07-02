package com.artisansoftware.luminaries.io

import java.util.Date

import com.artisansoftware.luminaries.core.Luminary
import twitter4j.conf.ConfigurationBuilder
import twitter4j.{FilterQuery, _}

import scala.collection.JavaConversions._

/**
  * Created by spencerward on 02/07/2016.
  */
object Twitter {
  type Tweet = Status

  private val token = new TwitterFactory(
    new ConfigurationBuilder().
      setApplicationOnlyAuthEnabled(true).
      build()).getInstance().getOAuth2Token

  private val twitter = new TwitterFactory(
    new ConfigurationBuilder().
      setApplicationOnlyAuthEnabled(true).
      setOAuth2TokenType(token.getTokenType).
      setOAuth2AccessToken(token.getAccessToken).
      build()).getInstance()

  private val twitterStream = new TwitterStreamFactory(
    new ConfigurationBuilder().
      build()).getInstance()

  def read(luminaries: List[Luminary], hours: Int): List[Tweet] = {
    var query: Query = buildQuery(luminaries)
    var result: QueryResult = twitter.search().search(query)
    var allTweets: List[Tweet] = List[Tweet]()
    allTweets = allTweets ++ result.getTweets

    do {
      query = result.nextQuery()
      if (query != null) {
        result = twitter.search().search(query)
        allTweets = allTweets ++ result.getTweets
      }
    } while (result.getTweets.nonEmpty && withinHours(result.getTweets.last, hours))

    allTweets.filter(withinHours(_, hours))
  }

  def readIds(luminaries: List[Luminary]): List[Long] =
    twitter.
      users().
      lookupUsers(luminaries.map(_.twitterHandle):_*).
      map(_.getId).
      toList

  def tweetStream(luminaryIds: List[Long], consumer: Tweet => Unit): Unit = {
    twitterStream.addListener(new StatusAdapter() {
      override def onStatus(tweet: Tweet): Unit = consumer(tweet)
    })
    twitterStream.filter(new FilterQuery().follow(luminaryIds:_*))
  }

  private def buildQuery(luminaries: List[Luminary]): Query =
    new Query(luminaries.map("from:" + _.twitterHandle) mkString " OR ").count(100)

  private def withinHours(tweet: Tweet, hours: Int): Boolean =
    new Date().getTime - tweet.getCreatedAt.getTime < hours * (60 * 60 * 1000)
}
