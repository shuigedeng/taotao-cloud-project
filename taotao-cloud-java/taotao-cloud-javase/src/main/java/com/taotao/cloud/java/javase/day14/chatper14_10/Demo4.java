package com.taotao.cloud.java.javase.day14.chatper14_10;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Queue接口的使用
 * @author shuigedeng
 *
 */
public class Demo4 {
	public static void main(String[] args) {
		//1创建队列
		Queue<String> queue=new LinkedList<>();
		//2入队
		queue.offer("苹果");
		queue.offer("橘子");
		queue.offer("葡萄");
		queue.offer("西瓜");
		queue.offer("榴莲");
		//3出队
		System.out.println(queue.peek());
		System.out.println("----------------");
		System.out.println("元素个数:"+queue.size());
		int size=queue.size();
		for(int i=0;i<size;i++) {
			System.out.println(queue.poll());
		}
		System.out.println("出队完毕:"+queue.size());
		
		
	}
}
