package com.artisansoftware.luminaries

import java.io.{File, FileReader, FileWriter}

import com.lambdaworks.jacks.JacksMapper

object Repository {
  val filename: String = System.getProperty("com.artisansoftware.luminaries.file", "luminaries")
  private val file = new File(f"${System.getProperty("user.home")}/$filename.json")
  if (!file.exists()) file.createNewFile()
  private val reader: FileReader = new FileReader(file)
  var luminaries: List[Luminary] = List()

  try {
    luminaries = JacksMapper.readValue[List[Luminary]](reader)
  } catch {
    case _: Throwable => // Do nothing
  } finally {
    reader.close()
  }

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
