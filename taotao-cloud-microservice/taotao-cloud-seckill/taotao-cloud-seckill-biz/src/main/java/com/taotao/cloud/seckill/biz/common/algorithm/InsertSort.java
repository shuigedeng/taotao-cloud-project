package com.taotao.cloud.seckill.biz.common.algorithm;

/**
 * 直接插入排序
 */
public class InsertSort {

	/**
	 * 直接插入排序是一种最简单的排序方法，它的基本操作是将一个记录插入到已排好的有序的表中，从而得到一个新的、记录数增1的有序表。 　　       *
	 * 当前元素的前面元素均为有序，要插入时，从当前元素的左边开始往前找（从后往前找），比当前元素大的元素均往右移一个位置，最后把当前元素放在它应该呆的位置就行了。
	 * 参考：https://www.cnblogs.com/mengdd/archive/2012/11/24/2786490.html
	 */
	public static void main(String[] args) {
		int[] list = {27, 76, 47, 23, 7, 32, 19, 86};
		System.out.println("************直接插入排序************");
		System.out.println("排序前：");
		display(list);
		System.out.println("排序后：");
		insertSort(list);
		display(list);
	}

	/**
	 * 直接插入排序算法
	 */
	public static void insertSort(int[] list) {
		int len = list.length;
		// 从无序序列中取出第一个元素 (注意无序序列是从第二个元素开始的)
		for (int i = 1; i < len; i++) {
			int temp = list[i];
			int j;
			// 遍历有序序列
			// 如果有序序列中的元素比临时元素大，则将有序序列中比临时元素大的元素依次后移
			for (j = i - 1; j >= 0 && list[j] > temp; j--) {
				list[j + 1] = list[j];
			}
			// 将临时元素插入到腾出的位置中
			list[j + 1] = temp;
		}
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
