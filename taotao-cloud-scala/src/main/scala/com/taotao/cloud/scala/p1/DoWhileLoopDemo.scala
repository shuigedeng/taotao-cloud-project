package com.taotao.cloud.scala.p1

/*
   int sum = 0;
   int n = 1;
   do {
      sum += n;
      n++;
   } while(n <= 10)
   System.out.println(“sum=” + sum);
	使用scala来完成1+。。。+10的操作
 */
object DoWhileLoopDemo {
	def main(args: Array[String]): Unit = {
		var sum = 0
		var n = 1
		do {
			sum += n
			n += 1
		} while(n <= 10)
		println("sum=" + sum)
	}
}
