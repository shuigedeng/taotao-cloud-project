package com.taotao.cloud.scala.p2.collection

/**
  * scala zip拉链操作
  */
object ZipOps {
	def main(args: Array[String]): Unit = {
		val province = Array("山东", "河南", "陕西", "福建", "广东", "甘肃")
		val capital = Array("济南", "郑州", "西安", "福州", "广州")

		val pcs:Array[(String, String)] = province.zip(capital)//会将两个集合裁剪之一样长
		for(pc <- pcs) {
			val p = pc._1
			val c = pc._2
			println(s"province: ${p}, capital: ${c}")
		}
		println("------------------zipAll-------------------")
		/**
		  * zipAll会使用后面的参数对没有匹配上的值自动的填充
		  * 其中第一个元素（A），会和第二个集合中的多余的元素进行配对
		  * 其中第二个元素(B)，会和第一个集合中的多余的元素进行配对
		  */
		val pcAll: Array[(String, String)] = province.zipAll(capital, "A", "B")
		for(pc <- pcAll) {
			val p = pc._1
			val c = pc._2
			println(s"province: ${p}, capital: ${c}")
		}
	}
}
