package com.taotao.cloud.java.javase.day12.chapter12_3;

import java.util.Comparator;
import java.util.TreeSet;

/**
 * 要求：使用TreeSet集合实现字符串按照长度进行排序
 * helloworld  zhang  lisi   wangwu  beijing  xian  nanjing
 * Comparator接口实现定制比较
 * @author wgy
 *
 */
public class Demo7 {
	public static void main(String[] args) {
		//创建集合，并指定比较规则
		TreeSet<String> treeSet=new TreeSet<>(new Comparator<String>() {
			@Override
			public int compare(String o1, String o2) {
				int n1=o1.length()-o2.length();
				int n2=o1.compareTo(o2);
				return n1==0?n2:n1;
			}
		});
		//添加数据
		treeSet.add("helloworld");
		treeSet.add("pingguo");
		treeSet.add("lisi");
		treeSet.add("zhangsan");
		treeSet.add("beijing");
		treeSet.add("cat");
		treeSet.add("nanjing");
		treeSet.add("xian");
		
		System.out.println(treeSet.toString());
		
		
	}
}
