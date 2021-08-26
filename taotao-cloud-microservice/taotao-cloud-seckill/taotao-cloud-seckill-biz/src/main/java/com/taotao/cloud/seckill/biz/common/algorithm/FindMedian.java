package com.taotao.cloud.seckill.biz.common.algorithm;

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Random;

/**
 * 百亿数据中取中位数 场景：股市上一个股票的价格从开市开始是不停的变化的，需要开发一个系统，给定一个股票，它能实时显示从开市到当前时间的这个股票的价格的中位数（中值）。 创建者 柒
 * 创建时间	2018年12月21日
 */
public class FindMedian {

	//maxHeap保存较小的半边数据，minHeap保存较大的半边数据。
	private static PriorityQueue<Integer> maxHeap, minHeap;

	public static void main(String[] args) {

		Comparator<Integer> revCmp = new Comparator<Integer>() {
			@Override
			public int compare(Integer left, Integer right) {
				return right.compareTo(left);
			}
		};

		maxHeap = new PriorityQueue<Integer>(100, revCmp);
		minHeap = new PriorityQueue<Integer>(100);
		Random ra = new Random();
		for (int i = 0; i <= 100; i++) {
			int number = ra.nextInt(200);
			addNumber(number);
		}
		System.out.println(minHeap);
		System.out.println(maxHeap);
		System.out.println(getMedian());
	}

	/*
	 * offer 新增元素
	 * peek 头部查询元素
	 * poll 队列中删除从头部
	 * 1)比较两个堆的大小，第一次肯定相同，此时两个堆都没有数据，把第一个数据放入大堆
	 * 2)比较两个堆的大小，第二次肯定不同，如果value值小于大堆头部的值，小堆加入大堆头部元素，大堆加入当前值
	 * 注意：
	 * 并不是线程安全的，多线程访问操作会有并发问题
	 */
	public static void addNumber(int value) {
		if (maxHeap.size() == minHeap.size()) {
			if (minHeap.peek() != null && value > minHeap.peek()) {
				maxHeap.offer(minHeap.poll());
				minHeap.offer(value);
			} else {
				maxHeap.offer(value);
			}
		} else {
			if (value < maxHeap.peek()) {
				minHeap.offer(maxHeap.poll());
				maxHeap.offer(value);
			} else {
				minHeap.offer(value);
			}
		}
	}

	/*
	 * If maxHeap and minHeap are of different sizes,
	 * then maxHeap must have one extra element.
	 */
	public static double getMedian() {
		if (maxHeap.isEmpty()) {
			return -1;
		}

		if (maxHeap.size() == minHeap.size()) {
			return (double) (minHeap.peek() + maxHeap.peek()) / 2;
		} else {
			return maxHeap.peek();
		}
	}
}
