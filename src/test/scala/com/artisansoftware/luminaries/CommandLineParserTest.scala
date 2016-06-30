package com.artisansoftware.luminaries

import java.util.Date

import com.artisansoftware.luminaries.Twitter._
import org.junit.runner.RunWith
import org.mockito.Mockito._
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner
import org.scalatest.mock.MockitoSugar

@RunWith(classOf[JUnitRunner])
class CommandLineParserTest extends FunSuite with MockitoSugar {
  test("luminaries") {
    val request: TwitterRequest = new CommandLineParser(Array()).toRequest
    assert(request.luminaries.nonEmpty)
  }

  test("default hours") {
    val request: TwitterRequest = new CommandLineParser(Array()).toRequest
    assert(request.hours == 2)
  }

  test("hours") {
    val request: TwitterRequest = new CommandLineParser(Array("3")).toRequest
    assert(request.hours == 3)
  }
}