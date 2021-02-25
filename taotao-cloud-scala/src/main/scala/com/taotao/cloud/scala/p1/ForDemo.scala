package com.taotao.cloud.scala.p1

/*
	使用for循环来完成前面while循环的案例
java版本：
	int sum = 0;
	for(int n = 1; n <= 10; n++) {
		sum += n;
	}
	sop(sum);
 */
object ForDemo {
	def main(args: Array[String]): Unit = {
		var sum = 0
		for(n <- 1 to 10) {
			sum += n
		}
		println("for sum: " + sum)
	}
}
