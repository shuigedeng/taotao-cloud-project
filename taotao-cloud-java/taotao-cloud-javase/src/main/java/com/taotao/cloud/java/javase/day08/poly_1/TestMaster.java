package com.taotao.cloud.java.javase.day08.poly_1;

public class TestMaster {
	public static void main(String[] args) {
		Master master=new Master();
		master.name="小明";
		Dog wangcai=new Dog();
		Bird bird=new Bird();
		//喂食
		master.feed(wangcai);
		master.feed(bird);
	}
}
