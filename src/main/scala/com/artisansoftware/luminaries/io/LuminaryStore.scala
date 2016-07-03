package com.artisansoftware.luminaries.io

import java.io.{File, FileReader, FileWriter}

import com.artisansoftware.luminaries.core.Luminary
import com.lambdaworks.jacks.JacksMapper

object LuminaryStore {
  val Filename: String = System.getProperty("com.artisansoftware.luminaries.file", "luminaries")

  def read(): List[Luminary] = {
    val reader: FileReader = new FileReader(file)
    try {
      JacksMapper.readValue[List[Luminary]](reader)
    } catch {
      case _: Throwable => List()
    } finally
      reader.close()
  }

  def write(luminaries: List[Luminary]): Unit = {
    val writer: FileWriter = new FileWriter(file)
    writer.write(JacksMapper.writeValueAsString(luminaries))
    writer.close()
  }

  def file: File = {
    val file = new File(f"${System.getProperty("user.home")}/$Filename.json")
    if (!file.exists()) file.createNewFile()
    file
  }
}
