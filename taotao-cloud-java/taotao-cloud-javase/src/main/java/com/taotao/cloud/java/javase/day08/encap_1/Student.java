package com.taotao.cloud.java.javase.day08.encap_1;

public class Student {
	private String name;
	private int age;
	private String sex;
	private double score;
	
	public void sayHi() {
		System.out.println("姓名:"+this.name+" 年龄:"+this.age+" 性别:"+this.sex+" 成绩:"+this.score);
	}
	
	//setAge()
	public void setAge(int age) {
		//判断
		if(age>0&&age<=120) {
			this.age=age;
		}else {
			this.age=18;
		}
	}
	//getAge();
	public int getAge() {
		return this.age;
	}
	
	//setSex
	public void setSex(String sex) {
		if(sex.equals("男")||sex.equals("女")) {
			this.sex=sex;
		}else {
			this.sex="男";
		}
	}
	//getSex
	public String getSex() {
		return this.sex;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getScore() {
		return score;
	}

	public void setScore(double score) {
		
		this.score = score;
	}
	
	
	
	
}
