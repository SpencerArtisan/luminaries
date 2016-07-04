package com.artisansoftware.luminaries.core

import org.scalatest._
import org.scalatest.mockito.MockitoSugar
import com.artisansoftware.luminaries.core.RichTextImplicits._

class RichTextTest extends FunSpec with Inside with Matchers with MockitoSugar {
  describe("empty rich text") {
    val text = RichText()

    it("should have no text") {
      text.toString should be ("")
    }
  }

  describe("Simple rich text") {
    val text: RichText = "hello world"

    it("should contain the same text") {
      text.toString should be ("hello world")
    }

    it("should support concatination") {
      (text + "!").toString should be ("hello world!")
    }

    it("should support left padding") {
      text.padLeft(15).toString should be ("    hello world")
    }

    it("should support left padding when text is longer than pad width") {
      text.padLeft(5).toString should be ("hello world")
    }

    it("should support right padding") {
      text.padRight(15).toString should be ("hello world    ")
    }

    it("should support right padding when text is longer than pad width") {
      text.padRight(5).toString should be ("hello world")
    }

    it("should support length") {
      text.length should be (11)
    }

    it("should support styling") {
      text.blue.toString should be ("\u001B[34mhello world\u001B[0m")
    }

    it("should support double styling") {
      text.blue.underline.toString should be ("\u001B[4\u001B[34mhello world\u001B[0m")
    }
  }
}
