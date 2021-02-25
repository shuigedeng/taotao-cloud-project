package com.taotao.cloud.java.javase.day12.chapter12_2;

public class TestGeneric {
	public static void main(String[] args) {
		//使用泛型类创建对象
		//注意：1泛型只能使用引用类型，2不同泛型类型对象之间不能相互赋值
		MyGeneric<String> myGeneric=new MyGeneric<String>();
		myGeneric.t="hello";
		myGeneric.show("大家好,加油");
		String string=myGeneric.getT();
		
		MyGeneric<Integer> myGeneric2=new MyGeneric<Integer>();
		myGeneric2.t=100;
		myGeneric2.show(200);
		Integer integer=myGeneric2.getT();
		
		
		//泛型接口
		MyInterfaceImpl impl=new MyInterfaceImpl();
		impl.server("xxxxxxx");
		
		
		MyInterfaceImpl2<Integer> impl2=new MyInterfaceImpl2<>();
		impl2.server(1000);
		
		//泛型方法
		
		MyGenericMethod myGenericMethod=new MyGenericMethod();
		myGenericMethod.show("中国加油");
		myGenericMethod.show(200);
		myGenericMethod.show(3.14);
		
	}
}
