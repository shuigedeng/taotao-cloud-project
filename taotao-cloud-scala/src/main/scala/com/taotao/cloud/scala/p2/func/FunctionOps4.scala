package com.taotao.cloud.scala.p2.func

object FunctionOps4 {
	def main(args: Array[String]): Unit = {
		println(add(3, 5))
		println(add(3, 5, 6, 7))
		println(add(Array(3, 5, 6, 7, 8): _*))
	}

	def add(arr: Int*) = {
		var sum = 0
		for(i <- arr) {
			sum += i
		}
		sum
	}

//	def add(arr:Array[Int]) = {
//		var sum = 0
//		for(i <- arr) {
//			sum += i
//		}
//		sum
//	}

	def add(a:Int, b:Int) = {
		a + b
	}
}
