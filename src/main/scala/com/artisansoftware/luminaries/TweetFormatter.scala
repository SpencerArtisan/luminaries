package com.artisansoftware.luminaries

import com.artisansoftware.luminaries.Style._
import com.artisansoftware.luminaries.Twitter._

object TweetFormatter {
  def format(tweet: Tweet): String =
    f"$Bold${tweet.getCreatedAt}%ta %<tb %<td %<tR$EndStyle   ${body(tweet)}%s"

  def body(tweet: Tweet): String =
    if (tweet.getText.contains("http")) highlightLink(tweet.getText) else tweet.getText

  private def highlightLink(text: String): String = {
    val start: Int = text.indexOf("http")
    val end: Int = text.indexOf(' ', start)
    val realEnd = if (end == -1) text.length else end
    s"${text.substring(0, start)}$Blue$Underline${text.substring(start, realEnd)}$EndStyle$EndColour${text.substring(realEnd)}"
  }
}
