package com.artisansoftware.luminaries

import java.io.{File, FileReader, FileWriter}

import com.lambdaworks.jacks.JacksMapper

object Repository {
  private val file = new File(f"${System.getProperty("user.home")}/luminaries.json")
  if (!file.exists()) file.createNewFile()
  var luminaries = JacksMapper.readValue[List[Luminary]](new FileReader(file))

  def +(luminary: Luminary): Unit = {
    luminaries = luminary :: luminaries
    write()
  }

  def -(luminary: Luminary): Unit = {
    luminaries = luminaries.filterNot(_.twitterHandle == luminary.twitterHandle)
    write()
  }

  def contains(luminary: Luminary): Boolean =
    luminaries.exists(_.twitterHandle == luminary.twitterHandle)

  private def write(): Unit = {
    val json = JacksMapper.writeValueAsString(luminaries)
    val writer: FileWriter = new FileWriter(file)
    writer.write(json)
    writer.close()
  }
}
