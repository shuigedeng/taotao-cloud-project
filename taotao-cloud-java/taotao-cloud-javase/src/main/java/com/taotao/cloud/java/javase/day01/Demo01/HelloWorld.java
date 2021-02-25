package com.taotao.cloud.java.javase.day01.Demo01;

import java.util.Map;

public class HelloWorld {
	public static void main(String[] args) {
		System.out.print("Hello Everyone");
	}
}

class GoodByeWorld {
	public static void main(String[] args) {
		System.out.println("output environment var:");
		for(Map.Entry entry:System.getenv().entrySet()){
			System.out.println(entry.getKey()+":"+entry.getValue());
		}

	}


}
