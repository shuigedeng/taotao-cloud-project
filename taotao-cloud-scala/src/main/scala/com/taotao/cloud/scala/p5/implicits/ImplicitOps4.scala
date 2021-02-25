package com.taotao.cloud.scala.p5.implicits

object ImplicitOps4 {
	def main(args: Array[String]): Unit = {

		implicit val stuentSign = new StudentSubmitReport

		signForReport ("jack")

	}

	def signForReport(name: String) (implicit stuSReport: StudentSubmitReport) {
		stuSReport.writeReport(name + " come to here")
	}
}

class StudentSubmitReport {
	def writeReport(ctent: String) = println(ctent)
}
