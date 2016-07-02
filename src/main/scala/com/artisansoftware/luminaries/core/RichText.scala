package com.artisansoftware.luminaries.core


class RichText(text: String, styleCodes: List[Int] = List()) {
  private val Marker = "\u001B[%sm"

  def apply(styleCode: Int) = new RichText(text, styleCode :: styleCodes)
  def blue = apply(34)
  def red = apply(31)
  def greyBackground = apply(47)
  def bold = apply(1)
  def underline = apply(4)
  def padLeft(width: Int) = new RichText(padding(width) + text, styleCodes)
  def padRight(width: Int) = new RichText(text + padding(width), styleCodes)

  private def padding(width: Int): String = " " * Math.max(0, width - text.length)
  private def style(codes: Int*): String = codes.map(Marker.format(_)) mkString

  override def toString: String = style(styleCodes:_*) + text + style(0)
}

object RichTextImplicits {
  implicit def wrap(s: String): RichText = new RichText(s)
}


