package com.taotao.cloud.java.javase.day08.poly_1;
/**
 * 主人类
 * @author shuigedeng
 *
 */
public class Master {
	String name;
	
	/**
	 * 喂狗狗
	 */
//	public void feed(Dog dog) {
//		System.out.println(this.name+"喂食");
//		dog.eat();
//	}
//	
	/**
	 * 喂鸟
	 */
//	public void feed(Bird bird) {
//		System.out.println(this.name+"喂食");
//		bird.eat();
//	}
	//使用多态优化
	public void feed(Animal animal) {
		System.out.println(this.name+"喂食");
		animal.eat();
	}
	
	//购买动物
	public Animal buy(int type) {
		Animal animal=null;
		if(type==1) {
			animal=new Dog();
		}else if(type==2) {
			animal=new Bird();
		}
		return animal;
	}
	
}
