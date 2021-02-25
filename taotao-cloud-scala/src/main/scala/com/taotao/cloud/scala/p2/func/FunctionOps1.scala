package com.taotao.cloud.scala.p2.func

/*
	scala中函数的定义
	注意：
		1，如果定义的函数写了返回值类型，则必须要加"=",不然编译器通不过
		2，如果没有写返回值类型，但是加了"="，编译器会自动的进行类型推断
		3，如果没有写返回值类型，也没有加"="，编译器认为你该函数没有返回值类型，或者返回值类型unit
 */
object FunctionOps1 {
	def main(args: Array[String]): Unit = {
		val ret = printMsg1("王二麻子")
		println(ret)
	}

	def printMsg(name:String): String = {
		"hello " + name
	}

	def printMsg1(name:String) = {
		"hello " + name
	}

}
