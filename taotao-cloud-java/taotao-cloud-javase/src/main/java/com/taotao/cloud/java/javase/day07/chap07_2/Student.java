package com.taotao.cloud.java.javase.day07.chap07_2;
/**
 * 学生类
 * @author wgy
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
	//默认的构造方法
	public Student() {
		//初始化工作
		System.out.println("默认构造方法执行了...");
//		name="xxxx";
//		age=10;
//		sex="男";
//		score=60;
	}
	
	public Student(String name,int age) {
		this.name=name;
		this.age=age;
	}
	
	public Student(String name,int age,String sex,double score) {
		//如果成员变量和局部变量名相同，由于局部变量的优先级高，怎么访问实例变量?使用this
//		this.name=name;
//		this.age=age;
		
		//this();//默认构造方法
		this(name,age);//调用带参构造方法 注意事项（1）this()必须是第一条语句  （2）只能调用一次
		this.sex=sex;
		this.score=score;
		
	}
	
	//方法
	public void sayHi() {
		System.out.println("大家好，我是"+this.name+" 年龄:"+this.age+" 性别："+this.sex+" 分数："+this.score);
		this.study();
		
	}
	public void study() {
		System.out.println(this.name+"好好学习");
	}
	
}
