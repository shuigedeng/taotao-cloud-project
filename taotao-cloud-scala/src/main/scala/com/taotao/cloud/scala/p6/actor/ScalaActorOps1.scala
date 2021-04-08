package com.taotao.cloud.scala.p6.actor

import akka.actor.Actor


/*
	scala的actor通信
 */
object ScalaActorOps1 {
	def main(args: Array[String]): Unit = {
		val helloActor = new HelloActor
		//启动当前的actor线程
		helloActor.start()
		helloActor ! "jack"
		helloActor ! 123456
	}
}

class HelloActor extends Actor {
	override def act(): Unit = {
		while(true) {
			receive {
				case name:String => {
					println("Hello， " + name + "!How are you?")
				}
				case num:Int => {
					println("integer: " + num)
				}
			}
		}
	}
}
