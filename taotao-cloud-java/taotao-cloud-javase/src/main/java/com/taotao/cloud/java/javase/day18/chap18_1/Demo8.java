package com.taotao.cloud.java.javase.day18.chap18_1;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Stream的使用 终止操作
 * 
 * @author shuigedeng
 *
 */
public class Demo8 {
	public static void main(String[] args) {
		ArrayList<Employee> list = new ArrayList<>();
		list.add(new Employee("小王", 15000));
		list.add(new Employee("小张", 12000));
		list.add(new Employee("小李", 18000));
		list.add(new Employee("小孙", 20000));
		list.add(new Employee("小刘", 25000));
		//终止操作 foreach
		list.stream()
			.filter(e->{
					System.out.println("过滤了....");
					return e.getMoney()>15000;
				})
			.forEach(System.out::println);
		//终止操作 min max count
		System.out.println("-----min-----");
		Optional<Employee> min = list.stream()
			.min((e1,e2)->Double.compare(e1.getMoney(), e2.getMoney()));
		System.out.println(min.get());
		System.out.println("-----max-----");
		Optional<Employee> max = list.stream()
			.max((e1,e2)->Double.compare(e1.getMoney(), e2.getMoney()));
		System.out.println(max.get());
		
		long count = list.stream().count();
		System.out.println("员工个数:"+count);
		
		//终止操作 reduce 规约
		//计算所有员工的工资和
		System.out.println("--------reduce---------");
		Optional<Double> sum = list.stream()
			.map(e->e.getMoney())
			.reduce((x,y)->x+y);
		System.out.println(sum.get());
		
		//终止方法 collect收集
		//获取所有的员工姓名，封装成一个list集合
		System.out.println("------collect------");
		List<String> names = list.stream()
			.map(e->e.getName())
			.collect(Collectors.toList());
		for (String string : names) {
			System.out.println(string);
		}
	}
}
