package com.taotao.cloud.seckill.biz.common.algorithm;

/**
 * 直接选择排序
 */
public class SelectionSort {

	/**
	 * 所谓选择排序，就是每次找到未排序中最小的（最大的也行）元素的位置，找到后与该位置与未排序序列的第一个元素交换值，直到该序列成为有序序列。
	 * 初始状态整个序列为无序序列，每次交换都使有序序列的长度加一，无序序列的起始位置后移一位。选择排序的平均时间复杂度为O(n^2)，且选择排序相对不稳定。
	 */
	public static void main(String[] args) {
		int[] list = {27, 76, 47, 23, 7, 32, 19, 86};
		System.out.println("************直接选择排序************");
		System.out.println("排序前：");
		display(list);
		System.out.println("排序后：");
		selectionSort(list);
		display(list);
	}

	/**
	 * 直接选择排序算法
	 */
	public static void selectionSort(int[] list) {
		int len = list.length;
		// 要遍历的次数（length-1次）
		for (int i = 0; i < len - 1; i++) {
			// 将当前下标定义为最小值下标
			int min = i;

			// 遍历min后面的数据
			for (int j = i + 1; j <= len - 1; j++) {
				// 如果有小于当前最小值的元素，将它的下标赋值给min
				if (list[j] < list[min]) {
					min = j;
				}
			}
			// 如果min不等于i，说明找到真正的最小值
			if (min != i) {
				swap(list, min, i);
			}
		}
	}

	/**
	 * 交换数组中两个位置的元素
	 */
	public static void swap(int[] list, int min, int i) {
		int temp = list[min];
		list[min] = list[i];
		list[i] = temp;
	}

	/**
	 * 遍历打印
	 */
	public static void display(int[] list) {
		if (list != null && list.length > 0) {
			for (int num : list) {
				System.out.print(num + " ");
			}
			System.out.println("");
		}
	}
}
