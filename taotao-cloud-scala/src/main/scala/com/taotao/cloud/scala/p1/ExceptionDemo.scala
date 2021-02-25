package com.taotao.cloud.scala.p1

import java.io.FileNotFoundException

import scala.io.Source

/*
	scala中的异常控制和懒加载
	lazy :懒加载，被lazy所修饰的变量，之后当第一次被调用的时候才会进行初始化，之前只是做了语法结构的正确性判断。
 */
object ExceptionDemo {
	def main(args: Array[String]): Unit = {

		try {
			lazy val lines = Source.fromFile("E:/work/scala-videos/code/scala-study/data/hello1.txt").mkString
			println(lines)
			lazy val i = 1 / 0
			val num = Integer.valueOf("12345a")
			println("num: " + num)
		} catch {
			case fnfe: FileNotFoundException => {
				println(fnfe.getMessage)
			}
			case ame: ArithmeticException => {
				println(ame.getMessage)
			}
			case nfe: NumberFormatException => {
				println(nfe.getMessage)
			}
			case _ => {
				println("通用异常处理")
			}
		}
	}
}
