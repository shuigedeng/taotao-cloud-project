package com.taotao.cloud.scala.p1

/*
java版本的代码：
	int sum = 0;
	int n = 1;
	while(n <= 10) {
	    sum += n;
	    n++;
	}
	System.out.println(“sum=” + sum);

 */
object WhileLoopDemo {
	def main(args: Array[String]): Unit = {
		var sum:Int = 0
		var n = 1
		while(n <= 10) {
			sum += n
			//n++ //java中的自增和自减，但是scala中可没有这个自增或自减，++/--是scala中集合的一个函数
			n += 1
		}
		println("sum=" + sum)
	}
}
