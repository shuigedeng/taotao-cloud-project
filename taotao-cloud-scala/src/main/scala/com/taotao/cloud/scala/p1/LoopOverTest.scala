package com.taotao.cloud.scala.p1

import scala.io.StdIn
import scala.util.control.Breaks._

/*
    登录用户名密码的游戏：三次机会，从控制台输入用户名密码，如果成功登录，返回登录成功，失败，则反馈错误信息！
    本案例来研究scala中三种循环终止的方式
    1、return
    2、控制循环结束条件
    3、使用breakable的结构来完成循环终止
 */
object LoopOverTest {
	def main(args: Array[String]): Unit = {
		val dbUser = "wemz"
		val dbPwd = "sorry"

		breakable {
			var count = 3 //输入的机会次数
			while(count > 0) {
				val user = StdIn.readLine("请输入您的用户名：")
				val pwd = StdIn.readLine("请输入您的密码：")
				if (dbUser == user && dbPwd == pwd) {
					println("欢迎您，" + user + ", 光临寒舍，顿时寒舍蓬荜生辉~~~")
					//count = 0
					//return
					break
				} else {
					//用户名或密码错误
					count -= 1
					println("用户名或密码错误, 您还有(" + count + ")次机会重输！")
				}
			}
		}
	}
}
