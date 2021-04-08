package com.taotao.cloud.scala.p6.akka.third

import akka.actor.{ActorSystem, Props}
import com.taotao.cloud.scala.p6.akka.{Header, Start}
import com.typesafe.config.ConfigFactory

object AkkaClientApplication extends App {
	// 通过配置文件application.conf配置创建ActorSystem系统
	val system = ActorSystem("client-system", ConfigFactory.load().getConfig("MyRemoteClientSideActor"))
	val log = system.log
	// 获取到ClientActor的一个引用
	val clientActor = system.actorOf(Props[ClientActor], "clientActor")
	clientActor ! Start // 发送一个Start消息，第一次与远程Actor握手（通过本地ClientActor进行转发）
	Thread.sleep(2000)
	// 发送一个Header消息到远程Actor（通过本地ClientActor进行转发）
	clientActor ! Header("What's your name: Can you tell me ", 20, encrypted=false)
	Thread.sleep(2000)
}

