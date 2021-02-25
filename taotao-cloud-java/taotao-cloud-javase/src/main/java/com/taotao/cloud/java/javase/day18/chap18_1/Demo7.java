package com.taotao.cloud.java.javase.day18.chap18_1;

import java.util.ArrayList;
import java.util.UUID;

public class Demo7 {
	public static void main(String[] args) {
		//串行流和并行流的区别
		ArrayList<String> list=new ArrayList<>();
		for(int i=0;i<5000000;i++) {
			list.add(UUID.randomUUID().toString());
		}
		//串行 10秒  并行 7秒
		long start=System.currentTimeMillis();
		long count=list.parallelStream().sorted().count();
		System.out.println(count);
		long end=System.currentTimeMillis();
		System.out.println("用时:"+(end-start));
		
		
	}
}
