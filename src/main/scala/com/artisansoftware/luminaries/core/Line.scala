package com.artisansoftware.luminaries.core

class Line(parts: List[RichText]= List()) {
  def this(parts: RichText*) = this(List(parts:_*))
  def this(text: String) = this(List(new RichText(text)))
  def + (other: RichText): Line = new Line(parts :+ other)
  override def toString: String = parts mkString
}
