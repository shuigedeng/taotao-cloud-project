package com.taotao.cloud.scala.p3.extendz


/**
  *    scala中的类型检查和类型转换
  *     类型检查：obj.isInstanceOf[类型]
  *     类型转换：obj.asInstanceOf[类型]
  */
object ObjectEqualsOps {
	def main(args: Array[String]): Unit = {
		val w1 = new Worker("zhangsan", 13)
		val w2 = new Worker("zhangsan", 13)
		println(w1.equals(w2))
	}
}

class Worker {
	var name: String = _
	var age: Int = _

	def this(name: String, age: Int) {
		this()
		this.name = name;
		this.age = age;
	}

	/*
	override def equals(obj: scala.Any): Boolean = {
		if(obj == null)
			false
		else if(!obj.isInstanceOf[Worker])
			false
		else {
			//将obj转化为Worker
			val that:Worker = obj.asInstanceOf[Worker]
			this.name.eq(that.name) && this.age == that.age
		}
	}
	*/
	/*
		使用scala中的模式匹配来完成类型检查和类型转换
		注意：在写这个模式匹配的时候，一定把默认的选项加上，避免所有的可能性都没有匹配成功，
		此时就会报错：scala.MatchError
	 */
	override def equals(obj: scala.Any): Boolean = {
		obj match {
			case that:Worker => {
				this.name.eq(that.name) && this.age == that.age
			}
			case _ => {//匹配不上前面的可能性，就使用这种默认的
				println("类型不匹配，不能进行比较")
				false
			}
		}
	}
}
