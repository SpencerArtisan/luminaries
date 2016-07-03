package com.artisansoftware.luminaries.core

import com.artisansoftware.luminaries.core.RichTextImplicits._

object LuminaryFormatter {
  def format(luminaries: List[Luminary]): RichText =
    (luminaries map format).foldLeft(new RichText())(_ + "\n" + _)

  private def format(luminary: Luminary): RichText =
    luminary.name.bold.padRight(28) + luminary.twitterHandle
}
