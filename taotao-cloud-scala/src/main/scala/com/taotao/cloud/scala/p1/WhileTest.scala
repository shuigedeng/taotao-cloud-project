package com.taotao.cloud.scala.p1

import scala.io.StdIn

/*
    登录用户名密码的游戏：三次机会，从控制台输入用户名密码，如果成功登录，返回登录成功，失败，则反馈错误信息！
 */
object WhileTest {
	def main(args: Array[String]): Unit = {
		val dbUser = "wemz"
		val dbPwd = "sorry"

		/*
			从控制台输入用户名和密码
			scala-api：
				StdIn.readInt() 从控制台输入一个int
				StdIn.readLine() 从控制台输入一行字符串
		 */
		var count = 3 //输入的机会次数
		while(count > 0) {
			val user = StdIn.readLine("请输入您的用户名：")
			val pwd = StdIn.readLine("请输入您的密码：")
			if (dbUser == user && dbPwd == pwd) {
				println("欢迎您，" + user + ", 光临寒舍，顿时寒舍蓬荜生辉~~~")
				count = 0
			} else {
				//用户名或密码错误
				count -= 1
				println("用户名或密码错误, 您还有(" + count + ")次机会重输！")
			}
		}
	}
}
