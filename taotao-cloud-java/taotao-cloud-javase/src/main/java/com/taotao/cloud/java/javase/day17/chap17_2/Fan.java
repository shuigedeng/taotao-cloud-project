package com.taotao.cloud.java.javase.day17.chap17_2;

public class Fan implements Usb{

	@Override
	public void service() {
		System.out.println("风扇开始工作了...");
	}

}
