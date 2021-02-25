package com.taotao.cloud.scala.p4.mode

/*
	模式匹配你匹配case class样例类
 */
object MatchOps2 {
	def main(args: Array[String]): Unit = {
		caseOps
	}

	def caseOps: Unit = {
		abstract class Expr //抽象类，代表了表达式
		case class Var(name:String) extends Expr
		case class UnOp(expr:Expr, operator:String) extends Expr
		case class Number(num:Double) extends Expr
		//3 + 4
		case class BinOp(val left:Expr, operator:String, var right:Expr) extends Expr

		def test(expr: Expr): Unit = {
			expr match {
				case Var(name) => println("var: " + name)
				case Number(num) => println("number: " + num)
				case UnOp(Var(name), "+") => println(name + "+")
				case BinOp(Number(num1), "+", Number(num2)) => println(num1 + num2)
				case BinOp(Number(num1), "-", Number(num2)) => println(num1 - num2)
				case BinOp(Number(num1), "*", Number(num2)) => println(num1 * num2)
				case BinOp(Number(num1), "/", Number(num2)) => println(num1 / num2)
				case _ => println(expr)
			}
		}

		val binOp = BinOp(Number(3.0), "*", Number(4.2))
		binOp.right = Number(3.2)
		test(binOp)
	}
}
