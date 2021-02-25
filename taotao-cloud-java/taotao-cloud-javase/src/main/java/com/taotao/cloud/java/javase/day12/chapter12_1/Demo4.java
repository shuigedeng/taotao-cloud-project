package com.taotao.cloud.java.javase.day12.chapter12_1;

import java.util.ArrayList;
import java.util.List;

/**
 * List的使用
 * @author wgy
 *
 */
public class Demo4 {
	public static void main(String[] args) {
		//创建集合
		List list=new ArrayList();
		//1添加数字数据(自动装箱)
		list.add(20);
		list.add(30);
		list.add(40);
		list.add(50);
		list.add(60);
		System.out.println("元素个数:"+list.size());
		System.out.println(list.toString());
		//2删除操作
		//list.remove(0);
//		list.remove(new Integer(20));
//		System.out.println("删除元素:"+list.size());
//		System.out.println(list.toString());
		
		//3补充方法subList：返回子集合,含头不含尾
		List subList=list.subList(1, 3);
		System.out.println(subList.toString());
		
	}
}
