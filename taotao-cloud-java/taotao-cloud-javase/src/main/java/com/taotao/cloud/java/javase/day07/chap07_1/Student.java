package com.taotao.cloud.java.javase.day07.chap07_1;
/**
 * 学生类
 * @author shuigedeng
 *
 */
public class Student {
	//属性
	//姓名
	String name;
	//年龄
	int age;
	//性别
	String sex;
	//分数
	double score;
	
	//方法
	public void sayHi() {
		System.out.println("大家好，我是"+name+" 年龄:"+age+" 性别："+sex+" 分数："+score);
	}
	
}
