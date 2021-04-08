package com.taotao.cloud.scala.p6.akka.third

import akka.actor.{Actor, ActorLogging}
import  com.taotao.cloud.scala.p6.akka.{Header, Shutdown, Start, Stop}

class RemoteActor extends Actor with ActorLogging {
	def receive = {
		case Start => { // 处理Start消息
			log.info("Remote Server Start ==>RECV Start event : " + Start)
		}
		case Stop => { // 处理Stop消息
			log.info("Remote Server Stop ==>RECV Stop event: " + Stop)
		}
		case Shutdown(waitSecs) => { // 处理Shutdown消息
			log.info("Remote Server Shutdown ==>Wait to shutdown: waitSecs=" + waitSecs)
			Thread.sleep(waitSecs)
			log.info("Remote Server Shutdown ==>Shutdown this system.")
			context.system.shutdown // 停止当前ActorSystem系统
		}
		case Header(id, len, encrypted) => log.info("Remote Server => RECV header: " + (id, len, encrypted)) // 处理Header消息
		case _ =>
	}
}

