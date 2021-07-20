package com.taotao.cloud.java.javase.day12.chapter12_4;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.omg.PortableInterceptor.IORInterceptor;

/**
 * 演示Collections工具类的使用
 * @author shuigedeng
 *
 */
public class Demo4 {
	public static void main(String[] args) {
		List<Integer> list=new ArrayList<>();
		list.add(20);
		list.add(5);
		list.add(12);
		list.add(30);
		list.add(6);
		//sort排序
		System.out.println("排序之前:"+list.toString());
		Collections.sort(list);
		System.out.println("排序之后:"+list.toString());
		
		//binarySearch二分查找
		int i=Collections.binarySearch(list, 13);
		System.out.println(i);
		
		//copy复制
		List<Integer> dest=new ArrayList<>();
		for(int k=0;k<list.size();k++) {
			dest.add(0);
		}
		Collections.copy(dest, list);
		System.out.println(dest.toString());
		
		//reverse反转
		
		Collections.reverse(list);
		System.out.println("反转之后:"+list);
		
		//shuffle 打乱
		Collections.shuffle(list);
		System.out.println("打乱之后:"+list);
		
		
		//补充： list转成数组 
		System.out.println("-------list转成数组 -----");
		Integer[] arr=list.toArray(new Integer[10]);
		System.out.println(arr.length);
		System.out.println(Arrays.toString(arr));
		
		//数组转成集合
		System.out.println("-------数组转成集合 -----");
		String[] names= {"张三","李四","王五"};
		//集合是一个受限集合，不能添加和删除
		List<String> list2=Arrays.asList(names);
		//list2.add("赵六");
		//list2.remove(0);
		System.out.println(list2);
		//把基本类型数组转成集合时，需要修改为包装类型
		Integer[] nums= {100,200,300,400,500};
		List<Integer> list3=Arrays.asList(nums);
		System.out.println(list3);
		
		
		
	}
}
