package com.taotao.cloud.java.javase.day10.interface_2;

public class TestPerson {
	public static void main(String[] args) {
		Person xiaoming=new Person("小明",20);
		xiaoming.fly();
		xiaoming.fire();
		System.out.println("=======多态========");
		Flyable flyable=new Person("小张",22);
		flyable.fly();
		
	
		Fireable fireable=new Person("小李", 15);
		fireable.fire();
	}
}
