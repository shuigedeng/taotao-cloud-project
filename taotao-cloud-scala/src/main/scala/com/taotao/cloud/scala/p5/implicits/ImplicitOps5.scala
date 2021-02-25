package com.taotao.cloud.scala.p5.implicits

/*
	隐式转换参数
	   也就是说一个函数的参数被implicit关键字修饰，把这些参数称之为隐式转换参数。
	 首先，该参数，不一定非得要(手动，显示)传递（赋值），因为有时候有默认值
	 其次，该参数，会自动的在其作用域范围内进行检索，找到相适应的变量，来完成赋值，
	    需要注意的是，该变量必须要被implicit关键字进行修饰
 */
object ImplicitOps5 {
	def main(args: Array[String]): Unit = {
		val array = Array(3, -6, 7, 1, 0, 9, 4, 2)
		println("排序前的数组" + array.mkString("[", ", ", "]"))
		//排序
		var newArray = array.sortWith(_ < _)
		println("排序后的数组" + newArray.mkString("[", ", ", "]"))
		println("======================sorted================================")

		implicit val ord = new Ordering[Int](){
			override def compare(x: Int, y: Int) = y.compareTo(x)
		}
		newArray = array.sortBy(num => num)

		println("排序后的数组" + newArray.mkString("[", ", ", "]"))

	}
}
