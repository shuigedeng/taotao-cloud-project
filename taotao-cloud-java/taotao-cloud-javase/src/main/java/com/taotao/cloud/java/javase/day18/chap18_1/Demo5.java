package com.taotao.cloud.java.javase.day18.chap18_1;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Stream的使用
 * (1)创建流
 * @author wgy
 *
 */
public class Demo5 {
	public static void main(String[] args) {
		//(1)Collection对象中的stream()和parallelStream()方法
		ArrayList<String> arrayList=new ArrayList<>();
		arrayList.add("apple");
		arrayList.add("huawei");
		arrayList.add("xiaomi");
		Stream<String> stream = arrayList.parallelStream();
		//遍历
//		stream.forEach(s->System.out.println(s));
		stream.forEach(System.out::println);
		//(2)Arrays工具类的stream方法
		String[] arr= {"aaa","bbb","ccc"};
		Stream<String> stream2=Arrays.stream(arr);
		stream2.forEach(System.out::println);
		
		//(3)Stream接口中的of iterate 、generate 方法
		
		Stream<Integer> stream3 = Stream.of(10,20,30,40,50);
		stream3.forEach(System.out::println);
		//迭代流
		System.out.println("-----迭代流------");
		Stream<Integer> iterate = Stream.iterate(0, x->x+2);
		iterate.limit(5).forEach(System.out::println);
		System.out.println("--------生成流----------");
		//生成流
		Stream<Integer> generate = Stream.generate(()->new Random().nextInt(100));
		generate.limit(10).forEach(System.out::println);
		
		//(4)IntStream,LongStream,DoubleStream  的of  、range、rangeClosed
		IntStream stream4 = IntStream.of(100,200,300);
		stream4.forEach(System.out::println);
		IntStream range = IntStream.rangeClosed(0, 50);
		range.forEach(System.out::println);
	}
}
