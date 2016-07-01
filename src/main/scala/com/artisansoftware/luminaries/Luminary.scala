package com.artisansoftware.luminaries

import java.io.{File, FileReader}

import com.lambdaworks.jacks.JacksMapper

case class Luminary(name: String, twitterHandle: String) {
}

object Luminary {
  private val path: String = f"${System.getProperty("user.home")}/luminaries.json"
  private val file = new File(path)
  if (!file.exists()) file.createNewFile()
  val luminaries = JacksMapper.readValue[List[Luminary]](new FileReader(path))
}

