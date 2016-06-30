package com.artisansoftware.luminaries

import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class ParallelTest extends FunSuite {
  test("dummy") {
    val result: Array[String] = new Parallel(Array("Hello", "World"), (word:String) => word.toUpperCase).go
    assert(result === List("HELLO", "WORLD"))
  }
}