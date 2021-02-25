package com.taotao.cloud.java.javase.day17.chap17_3;
/**
 * 单例第三种写法：静态内部类写法
 * @author wgy
 *
 */
public class SingleTon3 {
	private SingleTon3() {}
	
	private static class Holder{
		static SingleTon3 instance=new SingleTon3();
	}
	
	public static SingleTon3 getInstance() {
		return Holder.instance;
	}
}
