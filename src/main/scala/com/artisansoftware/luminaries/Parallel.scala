package com.artisansoftware.luminaries;

class Parallel[T, R: Manifest](data: Array[T], processor: T => R) {
  val MaxDepth = 2

  def go(): Array[R] = {
    var target = new Array[R](data.length)
    process(data, target, 0, data.length, 0)
    target
  }

  def process(source: Array[T], target: Array[R], start: Int, end: Int, depth: Int): Unit = {
    if (end - start <= 1 || depth == MaxDepth) {
      for(i <- start until end) {
        target(i) = processor(source(i))
      }
    } else {
      val mid = (end + start) / 2
      parallel(process(source, target, start, mid, depth + 1),
               process(source, target, mid, end, depth + 1))
    }
  }

  def parallel(taskA : => Unit, taskB : => Unit) = {
    val threadA = new Thread() {
      override def run(): Unit = {
        taskA
      }
    }
    val threadB = new Thread() {
      override def run(): Unit = {
        taskB
      }
    }
    threadA.start()
    threadB.start()
    threadA.join()
    threadB.join()
  }
}
