package com.taotao.cloud.scala.p6.actor

import akka.actor.Actor


/*
	使用case class样例类来完成scala actor的通信
	可以表达更加丰富的含义
	参加晨会
 */
object ScalaActorOps2 {
	def main(args: Array[String]): Unit = {
		val mm = new MorningMeeting
		mm.start()

		mm ! Greeting("tom", "how are you, gg~")

		mm ! WorkContent("scala")
	}
}

case class Greeting(name:String, greet:String)
case class WorkContent(content:String)

class MorningMeeting extends Actor {
	 def act(): Unit = {
		while (true) {
			receive {
				case Greeting(name, greet) => {
					println(s"Hi ${name}, ${greet}~")
				}
				case WorkContent(content) => {
					println(s"Let's talking about sth. with ${content}")
				}
			}
		}
	}

  override def receive: Receive = ???
}
