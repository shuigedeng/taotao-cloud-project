package com.taotao.cloud.scala.p6.akka.second

import akka.actor.{Actor, ActorLogging, ActorRef, ActorSystem, Props}
import com.taotao.cloud.scala.p6.akka.{Request, Response, Signal}

/*
	akka actor的本地互相通信
 */
object AkkaActorOps2 {
	def main(args: Array[String]): Unit = {
		val driverActorSystem = ActorSystem.create("stuTeacherQASystem")
		//老师的代理对象
		val teacherActorRef = driverActorSystem.actorOf(Props(new TeacherActor()), "teacherActor")
		//学生的代理对象
		val stuActorRef = driverActorSystem.actorOf(Props(classOf[StudentActor], teacherActorRef), "stuActor")

		stuActorRef ! Signal


		driverActorSystem.terminate()
	}
}
class TeacherActor extends Actor with ActorLogging {

	override def receive = {
		case Request(request) => {
			log.info("学生请求说：" + request)
			//接收到学生的请求之后，变相学生最反馈
			sender ! Response("当属魏蜀吴！")
		}
	}
}
class StudentActor(teacherActor: ActorRef) extends Actor with ActorLogging {
	override def receive = {
		case Signal => {
			//学生向老师提出问题
			teacherActor ! Request("请问老师：三国都是哪三个国家？")
		}
		case Response(response) => {
			log.info("老师回复说：" + response)
		}
	}
}
