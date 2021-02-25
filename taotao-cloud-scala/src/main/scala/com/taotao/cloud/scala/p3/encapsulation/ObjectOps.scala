package com.taotao.cloud.scala.p3.encapsulation

/**
  * scala中的object结构
  *     在java的一个类中，既可以拥有static静态，也可以拥有非static，同时java中的主函数
  *     必须是static。但是scala中的class只能提供非static的成员，所以要想scala也能提供static的城规院，
  *     就必须要使用object结构
  *  scala的object中定义的成员都是类似于java中的静态成员，也就是直接可以通过类名.调用，无需创建对象
  *
  * object的主要作用：
  *     1. 给scala类提供程序运行入口，静态的main函数
  *     2. 给scala类也能够提供静态成员---scala类的伴生对象来实现
  *
  */
object ObjectOps {
	def main(args: Array[String]): Unit = {
		println("hello world")
	}
}
