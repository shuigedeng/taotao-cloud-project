package com.taotao.cloud.scala.p4.mode

import scala.io.StdIn

/**
  * 完成scala中模式匹配的学习
  */
object MatchCaseOps1 {
	def main(args: Array[String]): Unit = {
//		switchOps
//		yeildOps
//		typeOps
//		collectionOps
		//接收主函数的参数
		/* 通常在scala中的这种获取参数的方式不被建议
		val first = args(0)
		val second = args(1)
		val third = args(2)
		val fourth = args(3)
		println("first: " + first)
		println("second: " + second)
		println("third: " + third)
		println("fourth: " + fourth)
		//这里建议使用模式匹配，去匹配数组，来完成操作
		*/
		val Array(first, second, third, fourth) = args
		println("first: " + first)
		println("second: " + second)
		println("third: " + third)
		println("fourth: " + fourth)
	}

	def collectionOps: Unit = {
		println("-------------匹配字符串----------------")
		val str = "Hello World"
		for(ch <- str) {
			ch match {
				case ' ' => println(",")
				case _ => println(ch)
			}
		}
		println("-------------匹配其它集合----------------")
		val array = Array(1, 1, 2)

		array match {
			//匹配当前数组，如果只有两个元素，成功，就依次赋值给x和y
			case Array(x, y) => println(s"x=${x}, y=${y}")
			case Array(0, _ *) => println("匹配该数组，首元素为0")
			case _ => println("other.")
		}

	}

	def typeOps: Unit = {

		class Person(name:String, age:Int) {
		}
		class Worker(name:String, age:Int) extends Person(name, age) {
			def work(): Unit = {
				println(s"工人同志${name}, 年龄为${age}正在热火朝天地休息")
			}
		}
		class Student(name:String, age:Int) extends Person(name, age) {
			def study(): Unit = {
				println(s"学生${name}, 年龄为${age}正在紧锣密鼓地王者农药~")
			}
		}

		def doSth(person:Person): Unit = {
			//类型匹配 ----类型检查
//			person.isInstanceOf[Worker]
//			person.isInstanceOf[Student]
			person match  {
				case worker: Worker => worker.work()
				case stu: Student => stu.study()
				case _ => println("没有匹配到具体类型，请重新输入~")
			}
		}
		doSth(new Worker("old李", 32))
	}

	//模式匹配是一个表达式，因此是有返回值的
	def yeildOps: Unit = {
		println("请从控制台输入一个字符：")
		val ch:Char = StdIn.readChar()
		val sign = ch match  {
			case '+' => -1
			case '-' => 1
			case '*' => -2
			case '/' => 2
			case _ => 0 //java中的default
		}
		println("sign: " + sign)
	}
	/*
		学习模式匹配去模拟java中的switch语法
		char ch = '='
		switch(ch) {
			case '+':
				....
				break;
			case '-':
				...
				break;
			...
			default:
				...
				break;
		}
	 */
	def switchOps(): Unit = {
		println("请从控制台输入一个字符：")
		val ch:Char = StdIn.readChar()
		var sign = 0
		ch match  {
			case '+' => sign = -1
			case '-' => sign = 1
			case '*' => sign = -2
			case '/' => sign = 2
			case _ => sign = 0 //java中的default
		}
		println("sign: " + sign)
	}


}
