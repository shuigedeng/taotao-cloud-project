package com.taotao.cloud.java.javase.day14.chatper14_2;

public class TestTicket {
	public static void main(String[] args) {
		//1创建票对象
		Ticket ticket=new Ticket();
		//2创建线程对象
		Thread w1=new Thread(ticket, "窗口1");
		Thread w2=new Thread(ticket, "窗口2");
		Thread w3=new Thread(ticket, "窗口3");
		Thread w4=new Thread(ticket, "窗口4");
		//3启动线程
		w1.start();
		w2.start();
		w3.start();
		w4.start();
	}
}
