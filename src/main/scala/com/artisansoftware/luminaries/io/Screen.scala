package com.artisansoftware.luminaries.io

import com.artisansoftware.luminaries.core.Line

object Screen {
  def write(lines: List[Line]): Unit = println(lines.map(_.toString) mkString "\n")
  def write(lines: Line*): Unit = write(List(lines:_*))
}
