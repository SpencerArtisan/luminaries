package com.artisansoftware.luminaries.core

case class TextPart(text: String, styles: List[Int] = List()) {
  private val Marker = "\u001B[%sm"

  def apply(styleCode: Int) = new TextPart(text, styleCode :: styles)
  def length = text.length
  private def style(codes: Int*): String = codes.map(Marker.format(_)) mkString
  override def toString: String = style(styles:_*) + text + (if (styles.nonEmpty) style(0) else "")
}

case class RichText(protected val parts: List[TextPart] = List()) {
  def + (other: RichText): RichText = RichText(parts ++ other.parts)

  def apply(styleCode: Int) = RichText(parts.map(_.apply(styleCode)))
  def blue = apply(34)
  def red = apply(31)
  def greyBackground = apply(47)
  def bold = apply(1)
  def underline = apply(4)
  def padLeft(width: Int) = RichText(new TextPart(padding(width), parts.head.styles) :: parts)
  def padRight(width: Int) = RichText(parts :+ new TextPart(padding(width), parts.last.styles))
  def length = parts.foldLeft(0)(_ + _.length)

  override def toString: String = parts.foldLeft("")(_ + _)
  private def padding(width: Int): String = " " * Math.max(0, width - length)
}

object RichTextImplicits {
  implicit def wrap(text: String): RichText = new RichText(List(new TextPart(text)))
}


