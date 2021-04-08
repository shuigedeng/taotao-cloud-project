package com.taotao.cloud.scala.p6.akka.third

import akka.actor.{Actor, ActorLogging}
import  com.taotao.cloud.scala.p6.akka.{Header, Start, Stop}

class ClientActor extends Actor with ActorLogging {

	// akka.<protocol>://<actor system>@<hostname>:<port>/<actor path>
	val path = "akka.tcp://remote-system@127.0.0.1:2552/user/remoteActor" // 远程Actor的路径，通过该路径能够获取到远程Actor的一个引用
	val remoteServerRef = context.actorSelection(path) // 获取到远程Actor的一个引用，通过该引用可以向远程Actor发送消息
	@volatile var connected = false
	@volatile var stop = false
	def receive = {
		case Start => { // 发送Start消息表示要与远程Actor进行后续业务逻辑处理的通信，可以指示远程Actor初始化一些满足业务处理的操作或数据
			send(Start)
			if(!connected) {
				connected = true
				log.info("ClientActor==> Actor connected: " + this)
			}
		}
		case Stop => {
			send(Stop)
			stop = true
			connected = false
			log.info("ClientActor=> Stopped")
		}
		case header: Header => {
			log.info("ClientActor=> Header")
			send(header)
		}
		case (seq, result) => log.info("RESULT: seq=" + seq + ", result=" + result) // 用于接收远程Actor处理一个Packet消息的结果
		case m => log.info("Unknown message: " + m)
	}
	private def send(cmd: Serializable): Unit = {
		log.info("Send command to server: " + cmd)
		try {
			remoteServerRef ! cmd // 发送一个消息到远程Actor，消息必须是可序列化的，因为消息对象要经过网络传输
		} catch {
			case e: Exception => {
				connected = false
				log.info("Try to connect by sending Start command...")
				send(Start)
			}
		}
	}
}

