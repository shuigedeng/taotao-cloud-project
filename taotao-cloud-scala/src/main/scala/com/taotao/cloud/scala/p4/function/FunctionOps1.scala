package com.taotao.cloud.scala.p4.function

/*
	scala函数式编程
 */
object FunctionOps1 {
	def main(args: Array[String]): Unit = {

		//作为值的函数
//		funcOps1
		//匿名函数
//		funcOps2
		//高阶函数
		funcOps3
	}

	/**
	  * 所谓高阶函数：
	  *     带函数参数的函数，一个函数的参数是函数，把这种函数称之为高阶函数(high level function)
	  */
	def funcOps3: Unit = {
		//定义一个高阶函数
		def sayBye(name:String, func: (String) => Unit) = {
			func(name)
		}
		//调用函数sayBye
		sayBye("old李", (name:String) => method(name))
		//传递匿名函数
		sayBye("赵六", (name:String) => println(name))
		//简化书写
		sayBye("赵六", (name) => println(name))
		//如果匿名函数中只有一个变量，可以省略掉()
		sayBye("周七", name => println(name))
		//可以使用通配符"_"去代替这些变量
		sayBye("魏八",println(_))
		//最简可以连通配符都可以省略掉
		sayBye("蒋九",println)
		//重新在调用上述的函数
		sayBye("韩十", method)
	}

	def method(str:String): Unit = {
		println("一日之计在于晨")
		println(str)
		println("一年之计在于春")
	}

	/*
		所谓匿名函数，其实就是没有名字的函数
		scala中定义一个完成的函数：
			def funcName(params): returnType = {body}
		匿名函数：
			(params) => returnType //匿名函数的定义
			(params) => {body}  //匿名函数的实现
		问题是？
			该匿名函数如何被调用？
			所以，就只能将匿名函数复制给另外一个变量或者函数，来被调用。
	 */
	def funcOps2: Unit = {
		val sayBye = (name:String) => {
			println("good bye to " + name)
		}
		sayBye("张三")

		def sayByeBye = (name:String) => {
			println("good bye to " + name)
		}
		sayBye("李四")
	}
	/*
		函数可以作为值，传递给另外一个变量，或者另外一个函数，
		语法特点：必须要在函数后面加上空格或者下划线
	 */
	def funcOps1(): Unit = {
		def sayGoodBye(name:String): Unit = {
			println("good bye to " + name)
		}
		//传递给另外一个变量
		val sayBye = sayGoodBye _
		//传递给另外一个函数
		def sayByeBye = sayGoodBye _
		sayGoodBye("张三")
		sayBye("李四")
		sayByeBye("王五")
	}
}
