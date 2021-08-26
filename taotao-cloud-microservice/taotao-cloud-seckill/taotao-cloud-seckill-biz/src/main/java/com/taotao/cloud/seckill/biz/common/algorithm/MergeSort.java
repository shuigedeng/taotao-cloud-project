package com.taotao.cloud.seckill.biz.common.algorithm;

/**
 * 归并排序
 */
public class MergeSort {

	/**
	 * 归并排序（Merge Sort）与快速排序思想类似：将待排序数据分成两部分，继续将两个子部分进行递归的归并排序；然后将已经有序的两个子部分进行合并，最终完成排序。
	 * 其时间复杂度与快速排序均为O(nlogn)，但是归并排序除了递归调用间接使用了辅助空间栈，还需要额外的O(n)空间进行临时存储。从此角度归并排序略逊于快速排序，但是归并排序是一种稳定的排序算法，快速排序则不然。
	 * 所谓稳定排序，表示对于具有相同值的多个元素，其间的先后顺序保持不变。对于基本数据类型而言，一个排序算法是否稳定，影响很小，但是对于结构体数组，稳定排序就十分重要。例如对于student结构体按照关键字score进行非降序排序：
	 */
	public static void main(String[] args) {
		int[] list = {50, 10, 90, 30, 70};
		System.out.println("************归并排序************");
		System.out.println("排序前：");
		display(list);
		System.out.println("排序后：");
		mergeSort(list, new int[list.length], 0, list.length - 1);
		display(list);
	}

	/**
	 * 归并排序算法
	 *
	 * @param list     待排序的列表
	 * @param tempList 临时列表
	 * @param head     列表开始位置
	 * @param rear     列表结束位置
	 */
	public static void mergeSort(int[] list, int[] tempList, int head, int rear) {
		if (head < rear) {
			// 取分割位置
			int middle = (head + rear) / 2;
			// 递归划分列表的左序列
			mergeSort(list, tempList, head, middle);
			// 递归划分列表的右序列
			mergeSort(list, tempList, middle + 1, rear);
			// 列表的合并操作
			merge(list, tempList, head, middle + 1, rear);
		}
	}

	/**
	 * 合并操作(列表的两两合并)
	 *
	 * @param list
	 * @param tempList
	 * @param head
	 * @param middle
	 * @param rear
	 */
	public static void merge(int[] list, int[] tempList, int head, int middle, int rear) {
		// 左指针尾
		int headEnd = middle - 1;
		// 右指针头
		int rearStart = middle;
		// 临时列表的下标
		int tempIndex = head;
		// 列表合并后的长度
		int tempLength = rear - head + 1;

		// 先循环两个区间段都没有结束的情况
		while ((headEnd >= head) && (rearStart <= rear)) {
			// 如果发现右序列大，则将此数放入临时列表
			if (list[head] < list[rearStart]) {
				tempList[tempIndex++] = list[head++];
			} else {
				tempList[tempIndex++] = list[rearStart++];
			}
		}

		// 判断左序列是否结束
		while (head <= headEnd) {
			tempList[tempIndex++] = list[head++];
		}

		// 判断右序列是否结束
		while (rearStart <= rear) {
			tempList[tempIndex++] = list[rearStart++];
		}

		// 交换数据
		for (int i = 0; i < tempLength; i++) {
			list[rear] = tempList[rear];
			rear--;
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
