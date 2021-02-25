package com.taotao.cloud.java.javase.day11.chapter11_5;

import java.util.Arrays;

public class Demo3 {
	public static void main(String[] args) {
		String name="hello";//"hello" 常量存储在字符串池中,
		name="zhangsan";//“张三”赋值给name变量，给字符串赋值时，并没有修改数据，而是重新开辟一个空间
		String name2="zhangsan";
		
		//演示字符串的另一种创建方式，new String();
		String str=new String("java");
		String str2=new String("java");
		System.out.println(str==str2);
		System.out.println(str.equals(str2));
		
		System.out.println("------------字符串方法的使用 1------------------");
		//字符串方法的使用
		//1、length();返回字符串的长度
		//2、charAt(int index);返回某个位置的字符
		//3、contains(String str);判断是否包含某个子字符串
		
		String content="java是世界上最好的java编程语言,java真香";
		System.out.println(content.length());
		System.out.println(content.charAt(content.length()-1));
		System.out.println(content.contains("java"));
		System.out.println(content.contains("php"));
		
		System.out.println("------------字符串方法的使用 2------------------");
		//字符串方法的使用
		//4、toCharArray();返回字符串对应的数组
		//5、indexOf();返回子字符串首次出现的位置
		//6、lastIndexOf();返回字符串最后一次出现的位置
		System.out.println(Arrays.toString(content.toCharArray()));
		System.out.println(content.indexOf("java"));
		System.out.println(content.indexOf("java", 4));
		System.out.println(content.lastIndexOf("java"));
		
		System.out.println("------------字符串方法的使用3------------------");
		
		//7、trim();去掉字符串前后的空格
		//8、toUpperCase();//把小写转成大写 toLowerCase();把大写转成小写
		//9、endWith(str);判断是否已str结尾,startWith(str);判断是否已str开头
		
		String content2="   hello World   ";
		System.out.println(content2.trim());
		System.out.println(content2.toUpperCase());
		System.out.println(content2.toLowerCase());
		String filename="hello.java";
		System.out.println(filename.endsWith(".java"));
		System.out.println(filename.startsWith("hello"));
		
		
		System.out.println("------------字符串方法的使用4------------------");
		
		//10、replace(char old,char new); 用新的字符或字符串替换旧的字符或字符串
		//11、split();对字符串进行拆分
		
		System.out.println(content.replace("java", "php"));
		
		String say="java is the best   programing language,java xiang";
		String[] arr=say.split("[ ,]+");
		System.out.println(arr.length);
		for (String string : arr) {
			System.out.println(string);
		}
		
		//补充两个方法equals 、compareTo();比较大小
		System.out.println("---------补充---------");
		String s1="hello";
		String s2="HELLO";
		System.out.println(s1.equalsIgnoreCase(s2));
		
		String s3="abc";//97
		String s4="ayzawe";//120
		System.out.println(s3.compareTo(s4));
		
		String s5="abc";
		String s6="abc";
		System.out.println(s5.compareTo(s6));
		
		
	}
}
