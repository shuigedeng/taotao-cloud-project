package com.taotao.cloud.java.javase.day18.chap18_1;

public class Demo2 {
	public static void main(String[] args) {
		//匿名内部类
		Usb mouse=new Usb() {
			
			@Override
			public void service() {
				System.out.println("鼠标开始工作了..........");
			}
		};
		
		Usb fan=()->System.out.println("风扇开始工作了..........");
		
		
		run(mouse);
		run(fan);
	}
	public static void run(Usb usb) {
		usb.service();
	}
}
