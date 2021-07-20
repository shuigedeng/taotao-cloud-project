package com.taotao.cloud.java.javase.day12.chapter12_4;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Map接口的使用
 * 特点:(1)存储键值对 (2)键不能重复，值可以重复 (3)无序
 * @author shuigedeng
 *
 */
public class Demo1 {
	public static void main(String[] args) {
		//创建Map集合
		Map<String, String> map=new HashMap<>();
		//1添加元素
		map.put("cn", "中国");
		map.put("uk", "英国");
		map.put("usa", "美国");
		map.put("cn", "zhongguo");
		
		System.out.println("元素个数:"+map.size());
		System.out.println(map.toString());
		
		//2删除
//		map.remove("usa");
//		System.out.println("删除之后:"+map.size());
		//3遍历
		//3.1使用keySet();
		System.out.println("------keySet()--------");
		//Set<String> keyset=map.keySet();
		for (String key : map.keySet()) {
			System.out.println(key+"-----"+map.get(key));
		}
		//3.2使用entrySet()方法
		System.out.println("------entrySet()-----");
		//Set<Map.Entry<String, String>> entries=map.entrySet();
		for (Map.Entry<String, String> entry : map.entrySet()) {
			System.out.println(entry.getKey()+"---------"+entry.getValue());
		}
		//4判断
		System.out.println(map.containsKey("cn"));
		System.out.println(map.containsValue("泰国"));
		
	}
}
