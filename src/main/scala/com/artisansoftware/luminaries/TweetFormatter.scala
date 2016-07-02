package com.artisansoftware.luminaries

import com.artisansoftware.luminaries.Style._
import com.artisansoftware.luminaries.Twitter._

object TweetFormatter {
  def format(tweet: Tweet): String =
    f"$Bold${tweet.getCreatedAt}%ta %<tb %<td %<tR$EndStyle   ${body(tweet)}%s"

  def body(tweet: Tweet): String =
    "(http[^\\s]*)".r.replaceAllIn(tweet.getText, m => highlight(m.group(0)))

  private def highlight(text: String): String =
    s"$Blue$Underline$text$EndStyle$EndColour"
}
