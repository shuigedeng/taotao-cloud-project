package com.taotao.cloud.java.javase.day18.chap18_1;

import java.util.ArrayList;

/**
 * Stream的使用
 * (1)中间操作
 * @author wgy
 *
 */
public class Demo6 {
	public static void main(String[] args) {
		ArrayList<Employee> list=new ArrayList<>();
		list.add(new Employee("小王", 15000));
		list.add(new Employee("小张", 12000));
		list.add(new Employee("小李", 18000));
		list.add(new Employee("小孙", 20000));
		list.add(new Employee("小刘", 25000));
		//list.add(new Employee("小刘", 25000));
		//中间操作1 filter过滤   2 limit 限制  3 skip 跳过  4 distinct 去掉重复  5 sorted排序
		//filter过滤
		System.out.println("------filter-------");
		list.stream()
			.filter(e->e.getMoney()>15000)
			.forEach(System.out::println);
		//limit限制
		System.out.println("----limit------");
		list.stream()
			.limit(2)
			.forEach(System.out::println);
		//skip跳过
		System.out.println("-----skip------");
		list.stream()
			.skip(2)
			.forEach(System.out::println);
		System.out.println("------distinct--------");
		//distinct去重复
		list.stream()
			.distinct()
			.forEach(System.out::println);
		
		System.out.println("---------sorted---------");
		//sorted排序
		list.stream()
			.sorted((e1,e2)->Double.compare(e1.getMoney(), e2.getMoney()))
			.forEach(System.out::println);
				
		//中间操作2 map
		System.out.println("---------map--------");
		list.stream()
			.map(e->e.getName())
			.forEach(System.out::println);
		//中间操作3 parallel 采用多线程 效率高
		System.out.println("---------map--------");
		list.parallelStream()
			.forEach(System.out::println);
			
		
	}
}
