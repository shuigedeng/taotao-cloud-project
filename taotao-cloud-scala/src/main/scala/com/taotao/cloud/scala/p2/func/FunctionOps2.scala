package com.taotao.cloud.scala.p2.func

/*
	scala的几个基本函数
		没有返回值的函数
		空参函数
			1、如果一个函数在定义的时候没有参数，在调用的时候，可以省略掉()
			2、如果在定义一个空参函数的时候，省略了()，那么在调用的时候就必须也要省略掉()
		单行函数
			必须要使用"="将函数体和函数签名连接
		递归函数

 */
object FunctionOps2 {
	def main(args: Array[String]): Unit = {
		printMsg("old李")
		//空参函数
		showDate()
		showDate
//		showTime()
		showTime

		singleLineFunc("old李")

		val sum = factorial(5)
		println("5! = " + sum)

	}
	/*
		n! = n * (n - 1) * (n - 2) *...* 1
		n! = n * (n - 1)!
		(n - 1)! = (n - 1) * (n - 2)!
		....
		2! = 2 * 1!

		1! = 1
	 */
	def factorial(n:Int):Int = {
		if(n <= 1) {
			1
		} else {
			n * factorial(n - 1)
		}
	}

	def singleLineFunc(name:String) = println(name + ", come on~")

	/******************空参函数***************************/
	def showDate(): Unit ={
		println(System.currentTimeMillis())
	}
	def showTime {
		println(System.currentTimeMillis())
	}

	//没有返回值的函数
	def printMsg(name:String) {
		println("hello " + name)
	}
}
