package com.taotao.cloud.seckill.biz.common.algorithm;

/**
 * 排序 VS 参考 ：http://www.cnblogs.com/mengdd/archive/2012/11/24/2786346.html
 */
public class AlgorithmVs {

	public static void main(String[] args) {
		testQuick();//快速排序
		testShell();//希尔排序
		testHeap();//堆排序
		testMerge();//归并排序
		testSelection();//直接排序
		testInsert();//直接插入排序
		testBubble();//冒泡排序
	}

	/**
	 * 测试快速排序耗费的时间
	 */
	public static void testQuick() {
		int[] list = new int[10000];
		for (int i = 0; i < 10000; i++) {
			list[i] = (int) (Math.random() * 100000);
		}

		// 快速排序
		long start = System.currentTimeMillis();
		QuickSort.quickSort(list, 0, list.length - 1);
		long end = System.currentTimeMillis();
		System.out.println("快速排序耗费的时间：" + (end - start));
		display(list);
	}

	/**
	 * 测试冒泡排序耗费的时间
	 */
	public static void testBubble() {
		int[] list = new int[10000];
		for (int i = 0; i < 10000; i++) {
			list[i] = (int) (Math.random() * 100000);
		}

		// 冒泡排序
		long start = System.currentTimeMillis();
		BubbleSort.bubbleSort(list);
		long end = System.currentTimeMillis();
		System.out.println("冒泡排序耗费的时间：" + (end - start));
		display(list);
	}

	/**
	 * 测试直接插入排序耗费的时间
	 */
	public static void testInsert() {
		int[] list = new int[10000];
		for (int i = 0; i < 10000; i++) {
			list[i] = (int) (Math.random() * 100000);
		}

		// 直接插入排序
		long start = System.currentTimeMillis();
		InsertSort.insertSort(list);
		long end = System.currentTimeMillis();
		System.out.println("直接插入排序耗费的时间：" + (end - start));
		display(list);
	}

	/**
	 * 测试堆排序排序耗费的时间
	 */
	public static void testHeap() {
		int[] list = new int[10000];
		for (int i = 0; i < 10000; i++) {
			list[i] = (int) (Math.random() * 100000);
		}
		long start = System.currentTimeMillis();
		HeapSort.heapSort(list);
		long end = System.currentTimeMillis();
		System.out.println("堆排序排序耗费的时间：" + (end - start));
		display(list);
	}

	/**
	 * 测试归并排序排序排序耗费的时间
	 */
	public static void testMerge() {
		int[] list = new int[10000];
		for (int i = 0; i < 10000; i++) {
			list[i] = (int) (Math.random() * 100000);
		}
		long start = System.currentTimeMillis();
		MergeSort.mergeSort(list, new int[list.length], 0, list.length - 1);
		long end = System.currentTimeMillis();
		System.out.println("归并排序排序耗费的时间：" + (end - start));
		display(list);
	}

	/**
	 * 测试希尔排序耗费的时间
	 */
	public static void testShell() {
		int[] list = new int[10000];
		for (int i = 0; i < 10000; i++) {
			list[i] = (int) (Math.random() * 100000);
		}

		// 希尔排序
		long start = System.currentTimeMillis();
		ShellSort.shellSort(list);
		long end = System.currentTimeMillis();
		System.out.println("希尔排序耗费的时间：" + (end - start));
		display(list);
	}

	/**
	 * 快速排序耗费的时间
	 */
	public static void quickSort() {
		int[] list = new int[10000];
		for (int i = 0; i < 10000; i++) {
			list[i] = (int) (Math.random() * 100000);
		}

		// 希尔排序
		long start = System.currentTimeMillis();
		QuickSort.quickSort(list, 0, list.length - 1);
		long end = System.currentTimeMillis();
		System.out.println("快速排序耗费的时间：" + (end - start));
		display(list);
	}

	/**
	 * 直接选择排序耗费的时间
	 */
	public static void testSelection() {
		int[] list = new int[10000];
		for (int i = 0; i < 10000; i++) {
			list[i] = (int) (Math.random() * 100000);
		}
		long start = System.currentTimeMillis();
		SelectionSort.selectionSort(list);
		long end = System.currentTimeMillis();
		System.out.println("直接排序耗费的时间：" + (end - start));
		display(list);
	}

	/**
	 * 遍历打印前10个数
	 */
	public static void display(int[] list) {
		System.out.println("********排序之后的前10个数start********");
		if (list != null && list.length > 0) {
			for (int i = 0; i < 10; i++) {
				System.out.print(list[i] + " ");
			}
			System.out.println("");
		}
		System.out.println("********排序之后的前10个数end**********");
		System.out.println("");
	}
}
