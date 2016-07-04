package com.artisansoftware.luminaries.core

import org.mockito.Mockito._
import org.scalatest._
import org.scalatest.mockito.MockitoSugar

class CommandLineTest extends FunSpec with Inside with Matchers with MockitoSugar {
  describe("empty command line") {
    val commandLine = CommandLine(Array())

    it("should have no switches") {
      for (c <- 'a' to 'z') {
        commandLine.hasSwitch(c) should be (false)
      }
    }

    it("should have no numeric args") {
      commandLine.numericArgs should be (empty)
    }

    it("should have no text args") {
      commandLine.textArgs should be (empty)
    }
  }

  describe("single number command line") {
    val commandLine = CommandLine(Array("42"))

    it("should have no switches") {
      for (c <- 'a' to 'z') {
        commandLine.hasSwitch(c) should be (false)
      }
    }

    it("should have one numeric arg") {
      commandLine.numericArgs should contain only 42
    }

    it("should have no text args") {
      commandLine.textArgs should be (empty)
    }
  }

  describe("single text command line") {
    val commandLine = CommandLine(Array("arg"))

    it("should have no switches") {
      for (c <- 'a' to 'z') {
        commandLine.hasSwitch(c) should be (false)
      }
    }

    it("should have no numeric args") {
      commandLine.numericArgs should be (empty)
    }

    it("should have one text arg") {
      commandLine.textArgs should contain only "arg"
    }
  }

  describe("single switch command line") {
    val commandLine = CommandLine(Array("-a"))

    it("should have one switch") {
      commandLine.hasSwitch('a') should be (true)
    }

    it("should have no numeric args") {
      commandLine.numericArgs should be (empty)
    }

    it("should have no text args") {
      commandLine.textArgs should be (empty)
    }
  }

  describe("complex command line") {
    val commandLine = CommandLine(Array("1", "-a", "arg1", "arg2", "-b", "2"))

    it("should have two switches") {
      commandLine.hasSwitch('a') should be (true)
      commandLine.hasSwitch('b') should be (true)
    }

    it("should have two numeric args") {
      commandLine.numericArgs should contain only (1, 2)
    }

    it("should have two text args") {
      commandLine.textArgs should contain only ("arg1", "arg2")
    }
  }
}
