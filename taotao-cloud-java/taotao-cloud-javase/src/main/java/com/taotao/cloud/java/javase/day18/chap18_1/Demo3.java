package com.taotao.cloud.java.javase.day18.chap18_1;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;



public class Demo3 {
	public static void main(String[] args) {
		//匿名内部类
//		Consumer<Double> consumer=new Consumer<Double>() {
//
//			@Override
//			public void accept(Double t) {
//				System.out.println("聚餐消费:"+t);
//			}
//		};
		//Lambda表达式
		//Consumer<Double> consumer= t->System.out.println("聚餐消费:"+t);
		
//		happy(t->System.out.println("聚餐消费:"+t), 1000);
//		happy(t->System.out.println("唱歌消费:"+t), 2000);
//		happy(t->System.out.println("足疗消费:"+t), 3000);
		
		
//		int[] arr=getNums(()->new Random().nextInt(100), 5);
//		System.out.println(Arrays.toString(arr));
//		int[] arr2=getNums(()->new Random().nextInt(1000), 10);
//		System.out.println(Arrays.toString(arr2));
//		
//		String result=handlerString(s->s.toUpperCase(), "hello");
//		System.out.println(result);
//		String result2=handlerString(s->s.trim(), "   zhangsan        ");
//		System.out.println(result2);
		
		List<String> list=new ArrayList<>();
		list.add("zhangsan");
		list.add("zhangwuji");
		list.add("lisi");
		list.add("wangwu");
		list.add("zhaoliu");
		List<String> result=filterNames(s->s.startsWith("zhang"), list);
		System.out.println(result.toString());
		
		List<String> result2=filterNames(s->s.length()>5, list);
		System.out.println(result2);
	}
	//Consumer 消费型接口
	public static void happy(Consumer<Double> consumer,double money) {
		consumer.accept(money);
	}
	
	//Supplier 供给型接口
	public static int[] getNums(Supplier<Integer> supplier,int count) {
		int[] arr=new int[count];
		for(int i=0;i<count;i++) {
			arr[i]=supplier.get();
		}
		return arr;
	}
	
	//Function函数型接口
	public static String handlerString(Function<String, String> function,String str) {
		return function.apply(str);
	}
	
	//Predicate 断言型接口
	
	public static List<String> filterNames(Predicate<String> predicate,List<String> list){
		List<String> resultList=new ArrayList<String>();
		for (String string : list) {
			if(predicate.test(string)) {
				resultList.add(string);
			}
		}
		return resultList;
	}
	
}
