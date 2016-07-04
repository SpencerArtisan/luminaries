package com.artisansoftware.luminaries.core

import org.scalatest._
import org.scalatest.mockito.MockitoSugar

class LuminaryFormatterTest extends FunSpec with Inside with Matchers with MockitoSugar {
  describe("empty luminary list") {
    val text = LuminaryFormatter.format(List())

    it("should have no text") {
      text should be (RichText())
    }
  }

  describe("single luminary list") {
    val luminary: Luminary = Luminary("Robert Peston", "peston")
    val text = LuminaryFormatter.format(List(luminary))

    it("should include the luminary name") {
      text.toString should include ("Robert Peston")
    }

    it("should include the luminary twitter handle") {
      text.toString should include ("peston")
    }
  }

  describe("multiple luminary list") {
    val luminary1: Luminary = Luminary("Robert Peston", "peston")
    val luminary2: Luminary = Luminary("Jon Snow", "jonsnow")
    val text = LuminaryFormatter.format(List(luminary1, luminary2))

    it("should include the luminary names") {
      text.toString should include ("Robert Peston")
      text.toString should include ("Jon Snow")
    }

    it("should include the luminary twitter handles") {
      text.toString should include ("peston")
      text.toString should include ("jonsnow")
    }
  }

}
