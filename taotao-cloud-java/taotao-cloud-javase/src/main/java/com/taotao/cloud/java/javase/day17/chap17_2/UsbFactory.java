package com.taotao.cloud.java.javase.day17.chap17_2;
/**
 * 工厂类
 * @author wgy
 *
 */
public class UsbFactory {
	public static Usb createUsb(String type) {//类型的全名称 com.qf.
		Usb usb=null;
		Class<?> class1=null;
		try {
			class1 = Class.forName(type);
			usb=(Usb)class1.newInstance();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
		return usb;
	}
}
