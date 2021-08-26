package com.taotao.cloud.seckill.biz.common.algorithm;
/**
 * 希尔排序
 */
public class ShellSort {
	/**
	 * Shellsort是最古老的排序算法之一，该算法以其发明者Donald L. Shell的名字命名（1959）。
	 * 在ShellSort排序算法之前的算法时间复杂度基本都是O(n2)O(n2)，该算法是突破这个时间复杂度的第一批算法之一。
	 * 另外 Shellsort 是快速、易于理解和易于实现的。 然而，其复杂度分析有点复杂。
	 */
    public static void main(String[] args) {
    	int[] list = {27, 76, 47, 23, 7, 32, 19, 86};
        System.out.println("************希尔排序************");
        System.out.println("排序前：");
        display(list);
        System.out.println("");

        System.out.println("排序后：");
        shellSort(list);
        display(list);
    }

    /**
     * 希尔排序算法
     */
    public static void shellSort(int[] list) {
    	int len = list.length ;
        // 取增量
        int gap = len / 2;

        while (gap >= 1) {
            // 无序序列
            for (int i = gap; i < len; i++) {
                int temp = list[i];
                int j;

                // 有序序列
                for (j = i - gap; j >= 0 && list[j] > temp; j = j - gap) {
                    list[j + gap] = list[j];
                }
                list[j + gap] = temp;
            }

            // 缩小增量
            gap = gap / 2;
        }
    }

    /**
     * 遍历打印
     */
    public static void display(int[] list) {
        if (list != null && list.length > 0) {
            for (int num :list) {
                System.out.print(num + " ");
            }
            System.out.println("");
        }
    }
}
