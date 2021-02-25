package com.taotao.cloud.java.javase.day17.chap17_2;

import java.io.FileInputStream;
import java.util.Properties;
import java.util.Scanner;

/**
 * 客户程序
 * @author wgy
 *
 */
public class Demo {
	public static void main(String[] args) throws Exception{
		System.out.println("=========请选择 1 鼠标  2风扇 3 u盘===========");
		Scanner input=new Scanner(System.in);
		String choice=input.next();
		//1 = com.qf.chap17_2.Mouse
		//2 = com.qf.chap17_2.Fan
		//3 = com.qf.chap17_2.Upan
		//4 = com.qf.chap17_2.KeyBoard
		Properties properties=new Properties();
		FileInputStream fis=new FileInputStream("src\\usb.properties");
		properties.load(fis);
		fis.close();
		
		Usb usb=UsbFactory.createUsb(properties.getProperty(choice));
		if(usb!=null) {
			System.out.println("购买成功");
			usb.service();
		}else {
			System.out.println("购买失败，您要购买的产品不存在");
		}
	}
}
