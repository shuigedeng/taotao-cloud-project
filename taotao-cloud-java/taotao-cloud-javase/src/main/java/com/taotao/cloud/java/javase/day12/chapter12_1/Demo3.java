package com.taotao.cloud.java.javase.day12.chapter12_1;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * List子接口的使用
 * 特点：1 有序 有下标  2 可以重复
 * @author shuigedeng
 *
 */
public class Demo3 {
	public static void main(String[] args) {
		//先创建集合对象
		List list=new ArrayList<>();
		//1添加元素
		list.add("苹果");
		list.add("小米");
		list.add(0, "华为");
		System.out.println("元素个数:"+list.size());
		System.out.println(list.toString());
		//2删除元素
		//list.remove("苹果");
//		list.remove(0);
//		System.out.println("删除之后:"+list.size());
//		System.out.println(list.toString());
		//3遍历
		//3.1使用for遍历
		System.out.println("-----3.1使用for遍历-----");
		for(int i=0;i<list.size();i++) {
			System.out.println(list.get(i));
		}
		//3.2使用增强for
		System.out.println("-----3.2使用增强for-----");
		for (Object object : list) {
			System.out.println(object);
		}
		//3.3使用迭代器
		Iterator it=list.iterator();
		System.out.println("-----3.3使用迭代器-----");
		while(it.hasNext()) {
			System.out.println(it.next());
		}
		//3.4使用列表迭代器 ,和Iterator的区别，ListIterator可以向前或向后遍历，添加、删除、修改元素
		ListIterator lit=list.listIterator();
		System.out.println("------3.4使用列表迭代器从前往后-------");
		while(lit.hasNext()) {
			System.out.println(lit.nextIndex()+":"+lit.next());
		}
		System.out.println("------3.4使用列表迭代器后往前-------");
		while(lit.hasPrevious()) {
			System.out.println(lit.previousIndex()+":"+lit.previous());
		}
		
		//4判断
		System.out.println(list.contains("苹果"));
		System.out.println(list.isEmpty());
		
		//5获取位置
		System.out.println(list.indexOf("华为"));
	}
}
