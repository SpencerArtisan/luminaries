package com.artisansoftware.luminaries.core

class TextPart(text: String, val styles: List[Int] = List()) {
  private val Marker = "\u001B[%sm"

  def apply(styleCode: Int) = new TextPart(text, styleCode :: styles)
  def length = text.length
  private def style(codes: Int*): String = codes.map(Marker.format(_)) mkString
  override def toString: String = style(styles:_*) + text + style(0)
}

class RichText(protected val parts: List[TextPart] = List()) {
  def this(text: String) = this(List(new TextPart(text)))

  def + (other: RichText): RichText = new RichText(parts ++ other.parts)

  def apply(styleCode: Int) = new RichText(parts.map(_.apply(styleCode)))
  def blue = apply(34)
  def red = apply(31)
  def greyBackground = apply(47)
  def bold = apply(1)
  def underline = apply(4)
  def padLeft(width: Int) = new RichText(new TextPart(padding(width), parts.head.styles) :: parts)
  def padRight(width: Int) = new RichText(parts :+ new TextPart(padding(width), parts.last.styles))
  def length = parts.foldLeft(0)(_ + _.length)

  override def toString: String = parts.foldLeft("")(_ + _)
  private def padding(width: Int): String = " " * Math.max(0, width - length)
}

object RichTextImplicits {
  implicit def wrap(text: String): RichText = new RichText(text)
}


