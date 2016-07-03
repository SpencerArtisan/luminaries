package com.artisansoftware.luminaries.core

import com.artisansoftware.luminaries.core.RichTextImplicits._

object LuminaryFormatter {
  def format(luminaries: List[Luminary]): List[Line] =
    luminaries map format

  private def format(luminary: Luminary): Line =
    new Line(List[RichText](luminary.name.bold.padRight(28), luminary.twitterHandle))
}
