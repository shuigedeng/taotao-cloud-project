package com.taotao.cloud.scala.p4.function

/*
	scala中常见的高阶函数
	这些高阶函数，都是作用在集合上面
	filter
	map
	flatMap
	foreach
	reduce
	dropWhile
	sortWith
	groupBy
	partitionBy
 */
object FunctionOps2 {
	def main(args: Array[String]): Unit = {
//		filterOps
//		mapOps
//		flatMap
//		reduceOps
//		dropWhileOps
//		sortWithOps
//		groupByOps
		partitionOps
	}

	/*
		将集合中的元素，按照一定的提交进行分组，最后分成两个组
		partition(p: A => Boolean): (集合A， 集合B)
			在每一个元素A上进行作用该匿名函数，满足条件，共同构成返回值集合A，不满足条件的公共构成返回值集合B
			最后达到了分区的效果
	 */
	def partitionOps: Unit = {
		val array = Array(3, -6, 7, 1, 0, 9, 4, 2)
		//将集合array中的元素，按照4进行分区，小于4的再一个集合中，大于等于4的在另一个集合中
		val (left, right) = array.partition((num:Int) => num < 4)
		println(left.mkString("[", ", ", "]"))
		println(right.mkString("[", ", ", "]"))
	}

	/*
		groupBy ---就是分组，就是sql中的group by
	 */
	def groupByOps: Unit = {
		val array = Array(
			"甘肃,兰州",
			"甘肃,岷县",
			"甘肃,张掖",
			"北京,朝阳",
			"北京,昌平",
			"湖北,武汉",
			"湖北,黄石",
			"湖北,荆州"
		)
		/* 按照省份，对每一个城市进行分组
			select
				province,
				aggrFunc
			from xx
			group by province
		 */
		val province2Info:Map[String, Array[String]] = array.groupBy(line => line.substring(0, line.indexOf(",")))

		for((province, cities) <- province2Info) {
			println(s"province: ${province}--->${cities.mkString("[", ", ", "]")}")
		}
		println("-----------grouped---------------")
		val arrays = array.grouped(3)//将原来的集合进行分组，每个组内的元素有3个，或者可以理解为均分集合，每一份都是一个组
		for(arr <- arrays) {
			println(arr.mkString("[", ", ", "]"))
		}
	}

	/*
		sortWith: 排序操作
		sortWith((A, A) => Boolean)
			返回值是一个额Boolean类型，同时sortWith是排序，显示就是要对这两个元素A进行大小比较，比较的结果就是Boolean。
	 */
	def sortWithOps: Unit = {
		val array = Array(3, -6, 7, 1, 0, 9, 4, 2)
		//对集合中的元素进行升序排序
		var newArray = array.sortWith((v1, v2) => v1 < v2)
		println(newArray.mkString("[", ", ", "]"))

		newArray = array.sortWith(_ > _)
		println(newArray.mkString("[", ", ", "]"))
	}
	/*
		dropWhile(p:A => Boolean)
		该函数和filter一样，作用在集合的每一个元素，返回值为Boolean类型，
		dropWhile删除元素，直到不满足条件位置
	 */
	def dropWhileOps: Unit = {
		val array = Array(3, -6, 7, 1, 0, 9, 4, 2)

		//循环删除其中的偶数，直到条件不满足
		val newArray = array.dropWhile((num: Int) => num % 2 != 0)
		println(newArray.mkString("[", ", ", "]"))

	}

	/*
		reduce(p: (A, A) => A)
		reduce是一个聚合函数，将2个A，转化为1个A（其作用就是，作用在集合中的元素，进行聚合操作，得到一个新的值）
		那么这3个A本地代表什么意思呢？
		为了给大家说明，我将这3个A分别使用A1，A2，A3来进行代替
		大家肯定不陌生下面的求和的操作
		var sum = 0
		for(i <- 1 to 5) {
			sum = sum + i
		}
		此时，在每一次计算过程中的sum就是这里的A1，而i就是这里的A2，sum和i聚合结束的结果就是A3，
		此时的A3又作为下一次聚合操作中的sum，也就是A1.
	 */
	def reduceOps: Unit = {
		val array = 1 to 6
		var sum = 0
		for(i <- array) {
			println(s"sum= ${sum}, i = ${i}")
			sum = sum + i
		}
		println("final sum is : " + sum)
		println("------------------------------------")
		sum = array.reduce((A1: Int, A2: Int) => {
			println(s"A1= ${A1}, A2 = ${A2}")
			A1 + A2
		})
		println("final reduce sum is : " + sum)
		println("------------------------------------")
		sum = array.reduce((A1, A2) => {
			A1 + A2
		})
		println("final reduce sum is : " + sum)
		println("------------------------------------")
		sum = array.reduce(_ + _)//最简实现
		println("final reduce sum is : " + sum)
	}

	/*
		flatMap ---> (f: A => Traversable[B])
		flatMap和map的操作，比较相似，不同之处在于作用于匿名函数只用的返回值类型不一样。
		map操作，是one-2-one的操作
		flatMap操作，是one-2-many的操作，类似sql中的列转行的操作
		--------------
		foreach(p: A => Unit)
			遍历集合中的每一个元素A，进行操作，返回值类型为Unit
	 */
	def flatMap: Unit = {
		val array = Array(
			"i think there i am",
			"you are a lucky dog",
			"you are a green eye guy"
		)
		//提取集合中的每一个单词
		var words = array.flatMap((line:String) => {
			line.split("\\s+")
		})
		//查看words中的数据，遍历
		for(word <- words) {
			print(word + ",")
		}
		println("\r\n------------------------------------")
		words = array.flatMap((line) => line.split("\\s+"))
		words.foreach((word:String) => print(word + ","))
		println("\r\n------------------------------------")
		words = array.flatMap(line => line.split("\\s+"))
		words.foreach(word => print(word + ","))
		println("\r\n------------------------------------")
		words = array.flatMap(_.split("\\s+"))
		words.foreach(println)
	}
	/*
		map: (p: A => B)
			将一个结合中的所有元素A，都要作用在该匿名函数p上，每一个元素调用一次该匿名函数，
			将元素A，最终转化为元素B。
			需要注意的是，A和B都是数据类型可以一直，也可不一致，
			map的操作，其实就是一个one-2-one的映射操作
	 */
	def mapOps(): Unit = {
		val array = 1 to 5
		//将集合中的每一个元素，扩大原来的1.5倍
		var newArr:IndexedSeq[Double] = array.map((num: Int) => num *1.5)
		println(newArr.mkString("[", ", ", "]"))
		newArr = array.map((num) => num *1.5)
		println(newArr.mkString("[", ", ", "]"))
		newArr = array.map(num => num *1.5)
		println(newArr.mkString("[", ", ", "]"))
		newArr = array.map(_ * 1.5)
		println(newArr.mkString("[", ", ", "]"))
	}
	/*
		filter: 过滤
		filter: (A => Boolean)
		过滤掉集合中的元素A，经过函数惭怍，返回值为false的元素
	 */
	def filterOps(): Unit = {
		//过滤掉集合中的偶数，留下集合中的奇数
		val array = 1 to 10
		var filtered = array.filter((num:Int) => num % 2 != 0)
		println(filtered.mkString("[", ", ", "]"))
		filtered = array.filter((num) => num % 2 != 0)
		println(filtered.mkString("[", ", ", "]"))
		filtered = array.filter(num => num % 2 != 0)
		println(filtered.mkString("[", ", ", "]"))
		filtered = array.filter(_ % 2 != 0)
		println(filtered.mkString("[", ", ", "]"))
	}
}
