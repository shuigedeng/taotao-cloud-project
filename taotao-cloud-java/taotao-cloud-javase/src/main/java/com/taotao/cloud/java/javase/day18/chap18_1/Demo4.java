package com.taotao.cloud.java.javase.day18.chap18_1;

import java.util.Comparator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * 方法引用的使用
 * 1 对象::实例方法
 * 2 类::静态方法
 * 3 类::实例方法
 * 4 类::new
 * @author wgy
 *
 */
public class Demo4 {
	public static void main(String[] args) {
		//1 对象::实例方法
		Consumer<String> consumer=s->System.out.println(s);
		consumer.accept("hello");
		Consumer<String> consumer2=System.out::println;
		consumer.accept("world");
		
		//2类::静态方法
		Comparator<Integer> com=(o1,o2)->Integer.compare(o1, o2);
		Comparator<Integer> com2=Integer::compare;
			
		//3类::实例方法
		Function<Employee, String> function=e->e.getName();
		Function<Employee, String> function2=Employee::getName;
		
		System.out.println(function2.apply(new Employee("小明", 50000)));
		
		//4类::new
		Supplier<Employee> supplier=()->new Employee();
		Supplier<Employee> supplier2=Employee::new;
		
		Employee employee=supplier.get();
		System.out.println(employee.toString());
		
	}
}
