package com.taotao.cloud.scala.p4.mode

/**
  * scala中使用case class来模拟枚举操作
  */
object CaseClass2EnumerationOps {
	def main(args: Array[String]): Unit = {
		accrossRoad(TrafficLight.RED)
		println("---------使用case class来模拟枚举-----------")
		accrossRoad(RED("绿灯"))
	}

	def accrossRoad(light: Light): Unit = {
		light match {
			case RED(name) => println(name + "警示，行车不规范，亲人两行泪")
			case YELLOW(name) => println(name + "警示，快人5秒钟，快人一辈子")
			case GREEN(name) => println(name + "警示，开开心心开出去，平平安安开回来")
		}
	}

	def accrossRoad(light: TrafficLight.Value): Unit = {
		light match {
			case TrafficLight.RED => println("行车不规范，亲人两行泪")
			case TrafficLight.YELLOW => println("快人5秒钟，快人一辈子")
			case TrafficLight.GREEN => println("开开心心开出去，平平安安开回来")
		}
	}

	//使用scala传统的方式来定义一个枚举 得到RED其实TrafficLight.Value这种类
	object TrafficLight extends Enumeration {
		val RED, YELLOW, GREEN = Value
	}
}

/*
scala中对于如果说一个类的子类都是已知的，我们可以使用一个sealed关键字来进行修饰
表示该类是密封，刚好和枚举的含义相契合，枚举，可以一一列举
 */
sealed class Light(name:String)

case class RED(name:String) extends Light(name)
case class YELLOW(name:String) extends Light(name)
case class GREEN(name:String) extends Light(name)
