package com.taotao.cloud.java.javase.day14.chatper14_3;

public class TestPriority {
	public static void main(String[] args) {
		PriorityThread p1=new PriorityThread();
		p1.setName("p1");
		PriorityThread p2=new PriorityThread();
		p2.setName("p2");
		PriorityThread p3=new PriorityThread();
		p3.setName("p3");
		
		
		p1.setPriority(1);
		p3.setPriority(10);
		
		//启动
		p1.start();
		p2.start();
		p3.start();
	}
}
