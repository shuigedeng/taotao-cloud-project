package com.taotao.cloud.java.javase.day08.inheritance_4;
/**
 * super调用父类的构造方法
 * (1)super调用父类的无参构造
 * @author wgy
 *
 */
public class TestB {
	public static void main(String[] args) {
		B b=new B(100,200,300);
		b.m1();
		b.m2();
	}
}
