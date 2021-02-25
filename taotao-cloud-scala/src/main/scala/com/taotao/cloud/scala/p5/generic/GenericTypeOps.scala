package com.taotao.cloud.scala.p5.generic

/**
  * scala类型参数学习：
  *     scala的类型参数和java一样，既可以定义在类上面，也可以定义在方法上面。
  *     java中的泛型定义，使用<>，<>中指定具体的泛型，使用大写的标识符来制定，通常通常是大写字母，可以是一个字母，也可以是多个字母
  *     scala中的泛型定义使用[],[]中指定具体的泛型，使用大写的标识符来制定，通常通常是大写字母，可以是一个字母，也可以是多个字母
  * 泛型类：
  *     所谓泛型类，就是在类上面定义一个泛型，我们可以吧这个泛型当成是类的一个成员，在类的作用域范围内进行使用，
  *     可以在成员变量，成员方法上面来使用这个泛型。
  */
object GenericTypeOps {
	def main(args: Array[String]): Unit = {
		val userController = new UserController
		userController.register(User("张三", 13))
	}
}

//构建用户user，完成网络注册的业务逻辑
case class User(name:String, age:Int)
case class Product(pid:Long, name:String, price:Float)
trait BaseDao[T] {
	//通用的泛型保存方法
	def insert(item: T)
}

//具体的实现类---用户
class UserDao extends BaseDao[User] {
	override def insert(item: User): Unit = {
		println(s"向数据库插入用户信息为：${item.name}\t${item.age}")
	}
}
//具体的实现类---商品
class ProductDao extends BaseDao[Product] {
	override def insert(item: Product): Unit = {
		println(s"向数据库插入商品信息为：${item.pid}\t${item.name}\t${item.price}")
	}
}

//用户的业务层逻辑
class UserServiceImpl {
	val userDao = new UserDao
	def save(user: User): Unit = {
		userDao.insert(user)
	}
}

class UserController {
	val us = new UserServiceImpl
	def register(user: User): Unit = {
		us.save(user)
	}
}
