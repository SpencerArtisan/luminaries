package com.artisansoftware.luminaries

import com.artisansoftware.luminaries.Twitter.Tweet

class TwitterRequest(val luminaries: List[Luminary], val hours: Int, retweets: Boolean) {
  def accept(tweet: Tweet): Boolean = tweet.isRetweet == retweets
}

