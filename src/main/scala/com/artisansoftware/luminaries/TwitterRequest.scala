package com.artisansoftware.luminaries

import com.artisansoftware.luminaries.Twitter.Tweet

class TwitterRequest(val luminaries: List[Luminary], val hours: Int, filter: Tweet => Boolean) {
  def accept(tweet: Tweet): Boolean = filter(tweet)
}

