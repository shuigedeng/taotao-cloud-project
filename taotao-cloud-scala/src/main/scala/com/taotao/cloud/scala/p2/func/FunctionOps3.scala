package com.taotao.cloud.scala.p2.func

/*
	scala的默认参数和带名参数
 */
object FunctionOps3 {
	def main(args: Array[String]): Unit = {
		showAddr("old李", 13838383338L, "甘肃")
		showAddr("old李", 13838383338L)//这里使用的scala函数的默认参数的值
		//带名参数
		showAddr(name = "old王", 1393939339L, province = "火星")

		showAddr(name = "old王", province = "冥王星", phone = 1393939339L)
	}

	/**
	  *
	  * @param name
	  * @param phone
	  * @param province 该参数具有默认值
	  */
	def showAddr(name:String, phone:Long, province:String = "北京"): Unit = {
//		println("name: " + name + ", phone: " + phone + ", province: " + province) // 传统的String拼接
		println(s"name: ${name}, phone: ${phone}, province: ${province}")
	}
}
