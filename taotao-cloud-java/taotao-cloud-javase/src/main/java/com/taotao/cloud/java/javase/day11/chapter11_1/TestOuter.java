package com.taotao.cloud.java.javase.day11.chapter11_1;

import com.qf.chapter11_1.Outer.Inner;

public class TestOuter {
	public static void main(String[] args) {
		//1创建外部类对象
//		Outer outer=new Outer();
//		//2创建内部类对象
//		Inner inner=outer.new Inner();
		
		//一步到位
		Inner inner=new Outer().new Inner();
		
		inner.show();
		
		
	}
}
