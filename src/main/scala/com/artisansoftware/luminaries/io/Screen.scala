package com.artisansoftware.luminaries.io

import com.artisansoftware.luminaries.core.RichText

object Screen {
  def write(text: RichText): Unit = println(text.toString)
}
