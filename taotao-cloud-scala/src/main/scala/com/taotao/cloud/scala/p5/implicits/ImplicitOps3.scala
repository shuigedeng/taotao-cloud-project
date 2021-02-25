package com.taotao.cloud.scala.p5.implicits

import java.io.File

/*
	隐式转换的导入：
		其实，隐式转换的导入，有点类似导包，这是其一
		其二，只需要将我们该隐式转换引入到相关变量的作用域范围内即可
		其三，该变量就会自动在其作用域范围内去检索被implicit关键字修饰的函数
		其四，当找到implicit对应的函数有多个的时候，就会根据自己的类型和返回值的类型，进行匹配，
			一次来确定到底要是用哪一个隐式转换
		注意：只需要我们做导包，不需要进行强制的指定
 */
import ImplicitOps2._//不建议在这里导入隐式转换
object ImplicitOps3 {
	def main(args: Array[String]): Unit = {
		import ImplicitOps2.file2RichFile
		val file: File = new File("data/hello.txt")
		for(line <- file.read()) {
			println(line)
		}
	}
}
