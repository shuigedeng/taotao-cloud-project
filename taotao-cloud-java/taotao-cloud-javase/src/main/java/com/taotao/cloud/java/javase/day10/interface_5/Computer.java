package com.taotao.cloud.java.javase.day10.interface_5;

public class Computer {
	Usb usb1;
	Usb usb2;
	Usb usb3;
	
	public void run() {
		System.out.println("电脑开始工作...");
		if(usb1!=null) {
			usb1.service();
		}
		if(usb2!=null) {
			usb2.service();
		}
		if(usb3!=null) {
			usb3.service();
		}
	}
}
