package com.taotao.cloud.scala.p6.actor

import scala.actors.Actor

/*
	actor之间的通信
	基于学生向老师问问题，并得到老师的回复为例
 */
object ScalaActorOps3 {
	def main(args: Array[String]): Unit = {
		val teacherActor = new TeacherActor

		val stuActor = new StudentActor(teacherActor)

		teacherActor.start()
		stuActor.start()

		stuActor ! Signal
	}
}

case object Signal
case class Request(question:String)
case class Response(answer:String)

class StudentActor(teacherActor: TeacherActor) extends Actor {
	override def act(): Unit = {
		while (true) {
			receive {
				case Signal => {
					//学生要向老师问问题
					teacherActor ! Request("老师，请问树叶为什么是绿的？")
				}
				case Response(answer) => {
					println("老师回复说：" + answer)
				}
			}
		}
	}
}

class TeacherActor extends Actor {
	override def act(): Unit = {
		while (true) {
			receive {
				case Request(question) => {
					println("学生请教的问题是：" + question)
					//此处的sender便代表的是消息的发送者，在此便是studentActor
					sender ! Response("那是因为植物体内有叶绿素的原因~")
				}
			}
		}
	}
}
