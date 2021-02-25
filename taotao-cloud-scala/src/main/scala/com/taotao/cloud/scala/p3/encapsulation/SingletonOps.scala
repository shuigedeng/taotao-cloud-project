package com.taotao.cloud.scala.p3.encapsulation

object SingletonOps {
	def main(args: Array[String]): Unit = {
		val s1 = Singleton.getInstance()
		s1.x = 6
		val s2 = Singleton.getInstance()

		println(s1 == s2)
		println(s1.x)
		println(s2.x)
	}
}

class Singleton private() {
	var x = 5
}

object Singleton {
	private var instance:Singleton = _

	def getInstance():Singleton = {
		if(instance == null) {
			instance = new Singleton
		}
		instance//本类实例
	}
}
