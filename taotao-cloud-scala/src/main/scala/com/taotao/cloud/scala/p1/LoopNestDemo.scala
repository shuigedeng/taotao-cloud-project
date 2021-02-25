package com.taotao.cloud.scala.p1

/*
	scala中循环的嵌套
******
******
******
******
******
 */
object LoopNestDemo {
	def main(args: Array[String]): Unit = {
		println("=================打印一个5 * 5的矩阵=================")
		for(i <- 0 until 5) {//使用到了循环的嵌套 外循环控制行数，内循环控制每一行的列数
			for(j <- 1 to 5) {
				print("*")
			}
			println
		}
		println("=================打印下三角形=================")
		/*
			*
			**
			***
			****
			*****
		 */
		for(i <- 1 to 5) {
			for(j <- 1 to i) {
				print("*")
			}
			println
		}
		println("=================打印上三角形=================")
		/*
			*****
			 ****
			  ***
			   **
			    *
		 */
		for(i <- 1 to 5) {
			for(j <- 1 to 5) {
				if(j >= i) {
					print("*")
				} else {
					print(" ")
				}
			}
			println
		}
		println("=================基于循环嵌套的99乘法表=================")
		for(i <- 1 to 9) {
			for(j <- 1 to i) {
				print(j + "*" + i + "=" + (i * j) + "\t")
			}
			println
		}
		println("=================for循环嵌套的另外一种方式=================")
		for(i <- 1 to 9; j <- 1 to i) {
			print(j + "*" + i + "=" + (i * j) + "\t")
			if(i == j) {
				println
			}
		}
		println("=================for循环嵌套的另外一种方式=================")
		for(i <- 1 to 9; j <- 1 to 9 if j <= i) {
			print(j + "*" + i + "=" + (i * j) + "\t")
			if(i == j) {
				println
			}
		}
	}
}
