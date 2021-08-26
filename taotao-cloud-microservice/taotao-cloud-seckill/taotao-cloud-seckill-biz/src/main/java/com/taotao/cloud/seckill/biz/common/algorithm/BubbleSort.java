package com.taotao.cloud.seckill.biz.common.algorithm;

/**
 * 冒泡排序
 */
public class BubbleSort {

	/**
	 * 冒泡排序，持续比较相邻元素，大的挪到后面，因此大的会逐步往后挪，故称之为冒泡。 复杂度分析：平均情况与最坏情况均为 O(n^2), 使用了 temp 作为临时交换变量，空间复杂度为
	 * O(1).
	 */
	public static void main(String[] args) {
		int[] list = {27, 76, 47, 23, 7, 32, 19, 86};
		System.out.println("************冒泡排序************");
		System.out.println("排序前：");
		display(list);
		System.out.println("排序后：");
		bubbleSort(list);
		display(list);
	}

	/**
	 * 遍历打印
	 */
	public static void display(int[] list) {
		if (list != null && list.length > 0) {
			for (int num :
				list) {
				System.out.print(num + " ");
			}
			System.out.println("");
		}
	}

	/**
	 * 冒泡排序算法
	 */
	public static void bubbleSort(int[] list) {
		int len = list.length;
		// 做多少轮排序（最多length-1轮）
		for (int i = 0; i < len - 1; i++) {
			// 每一轮比较多少个
			for (int j = 0; j < len - 1 - i; j++) {
				if (list[j] > list[j + 1]) {
					// 交换次序
					int temp = list[j];
					list[j] = list[j + 1];
					list[j + 1] = temp;
				}
			}
		}
	}
}
