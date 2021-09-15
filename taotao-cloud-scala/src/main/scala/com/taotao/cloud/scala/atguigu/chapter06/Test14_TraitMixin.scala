package com.taotao.cloud.scala.atguigu.chapter06

object Test14_TraitMixin {
  def main(args: Array[String]): Unit = {
    val student = new Student14
    student.study()
    student.increase()

    student.play()
    student.increase()

    student.dating()
    student.increase()

    println("===========================")
    // 动态混入
    val studentWithTalent: Student14 with Talent = new Student14 with Talent {
      override def dancing(): Unit = println("student is good at dancing")

      override def singing(): Unit = println("student is good at singing")
    }

    studentWithTalent.sayHello()
    studentWithTalent.play()
    studentWithTalent.study()
    studentWithTalent.dating()
    studentWithTalent.dancing()
    studentWithTalent.singing()
  }
}

// 再定义一个特质
trait Knowledge {
  var amount: Int = 0

  def increase(): Unit
}

trait Talent {
  def singing(): Unit

  def dancing(): Unit
}

class Student14 extends Person13 with Young with Knowledge {
  // 重写冲突的属性
  override val name: String = "student"

  // 实现抽象方法
  def dating(): Unit = println(s"student $name is dating")

  def study(): Unit = println(s"student $name is studying")

  // 重写父类方法
  override def sayHello(): Unit = {
    super.sayHello()
    println(s"hello from: student $name")
  }

  // 实现特质中的抽象方法
  override def increase(): Unit = {
    amount += 1
    println(s"student $name knowledge increased: $amount")
  }
}
