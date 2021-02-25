package com.taotao.cloud.scala.p4.mode

//模式匹配之option操作
object MatchOps3 {
	def main(args: Array[String]): Unit = {

		val map = Map[String, String](
			"China" -> "BJ",
			"India" -> "XDL",
			"Japan" -> "Tokyo"
		)

		val capitalOption:Option[String] = map.get("Chinese")

		capitalOption match  {
			case Some(capital) => println("Capital is " + capital)
			case None => println("所查国家不存在~")
		}

	}

}
