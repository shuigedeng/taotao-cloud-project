package com.taotao.cloud.java.javase.day10.interface_5;

public class Mouse implements Usb{

	@Override
	public void service() {
		System.out.println("鼠标连接电脑成功开始工作了...");
	}

}
