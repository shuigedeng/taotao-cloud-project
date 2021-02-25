package com.taotao.cloud.java.javase.day14.chatper14_10;

import java.util.concurrent.CopyOnWriteArraySet;

/**
 * 演示CopyOnWriteArraySet
 * 重复依据：equals方法
 * @author wgy
 *
 */
public class Demo3 {
	public static void main(String[] args) {
		//1创建集合
		CopyOnWriteArraySet<String> set=new CopyOnWriteArraySet<>();
		//2添加元素
		set.add("pingguo");
		set.add("huawei");
		set.add("xiaomi");
		set.add("lianxiang");
		set.add("pingguo");
		//3打印
		System.out.println("元素个数:"+set.size());
		System.out.println(set.toString());
	}
}
