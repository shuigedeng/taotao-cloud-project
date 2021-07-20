package com.taotao.cloud.java.javase.day18.chap18_1;

import java.util.Comparator;
import java.util.TreeSet;
import java.util.function.Consumer;

/**
 * Lambda表达式的使用
 * @author shuigedeng
 *
 */
public class Demo1 {
	public static void main(String[] args) {
		//匿名内部类
//		Runnable runnable=new Runnable() {
//			
//			@Override
//			public void run() {
//				System.out.println("子线程执行了.........");
//			}
//		};
		
		//Lambda表达式
		Runnable runnable2=()->System.out.println("子线程执行了2.........");
		
		new Thread(runnable2).start();
		new Thread(()->System.out.println("子线程执行了3.........")).start();
		
		/////////////////////////////////
		//匿名内部类
//		Comparator<String> com=new Comparator<String>() {
//
//			@Override
//			public int compare(String o1, String o2) {
//				// TODO Auto-generated method stub
//				return o1.length()-o2.length();
//			}
//		};
		
		//Lambda表达式
		
		Comparator<String> com2=(String o1, String o2)-> {
			// TODO Auto-generated method stub
			return o1.length()-o2.length();
		};
		
		Comparator<String> com3=(o1,o2)->o1.length()-o2.length();
		
		TreeSet<String> treeSet=new TreeSet<>(com3);
		
		
		
	}
}
