package com.taotao.cloud.scala.p5.implicits

import java.io.{File, FileWriter}

import scala.io.Source

/*
	scala隐式转换功能之丰富现有api的类库
		让java.io.File类具备读写文件的能力

 */
object ImplicitOps2 {
	def main(args: Array[String]): Unit = {
		val file: File = new File("data/hello.txt")
		val filename = file.getName
		println("filename: " + filename)
		println("---------------file.read-----------------------")
		val lines = file.read()
		for(line <- lines) {
			println(line)
		}
		println("---------------file.write-----------------------")

		file.write("\r\nhello spark")

	}
	implicit def file2RichFile(file: File): RichFile = {
		new RichFile(file)
	}

	class RichFile(file:File) {
		def read() = Source.fromFile(file).getLines().toList

		def write(str:String): Unit = {
			val bw = new FileWriter(file, true)
			bw.write(str)
			bw.flush()
			bw.close()
		}
	}
}
