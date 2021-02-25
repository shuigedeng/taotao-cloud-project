package com.taotao.cloud.java.javase.day10.interface_5;

public class Fan implements Usb{

	@Override
	public void service() {
		System.out.println("风扇连接电脑成功，开始工作...");
	}

}
