package com.taotao.cloud.scala.p6.akka.first

import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import com.taotao.cloud.scala.p6.akka.Request


/*
	akka actor
 */
object AkkaActorOps {
  def main(args: Array[String]): Unit = {
    //akka actor的系统
    val actorSystem = ActorSystem.create("teacherActorSystem")

    //构建 akka actor的代理对象
    val teacherActorRef: ActorRef = actorSystem.actorOf(Props(new TeacherActor()))

    teacherActorRef ! Request("天空为什么是蓝的")

    actorSystem.terminate()
  }
}

class TeacherActor extends Actor {
  override def receive = {
    case Request(request) => {
      println("学生请求说：" + request)
    }
  }
}
