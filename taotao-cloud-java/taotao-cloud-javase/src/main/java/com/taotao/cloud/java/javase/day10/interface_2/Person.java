package com.taotao.cloud.java.javase.day10.interface_2;

public class Person implements Flyable,Fireable{
	String name;
	int age;
	public Person() {
		// TODO Auto-generated constructor stub
	}
	
	public Person(String name, int age) {
		super();
		this.name = name;
		this.age = age;
	}

	public void eat() {
		System.out.println(name+"吃东西...");
	}
	public void sleep() {
		System.out.println(name+"睡了...");
	}
	@Override
	public void fly() {
		System.out.println(name+"开始飞了...");
	}

	@Override
	public void fire() {
		System.out.println(name+"可以喷火了...");
	}
}
