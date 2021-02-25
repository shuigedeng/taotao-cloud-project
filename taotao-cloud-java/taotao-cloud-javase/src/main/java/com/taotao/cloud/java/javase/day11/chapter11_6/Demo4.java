package com.taotao.cloud.java.javase.day11.chapter11_6;

public class Demo4 {
	public static void main(String[] args) {
		//1 arraycopy：数组的复制
		//src：源数组
		//srcPos:从那个位置开始复制 0
		//dest：目标数组
		//destPos:目标数组的位置
		//length：复制的长度
		int[] arr= {20,18,15,8,35,26,45,90};
		int[] dest=new int[8];
		System.arraycopy(arr, 4, dest, 4, 4);
		for(int i=0;i<dest.length;i++) {
			System.out.println(dest[i]);
		}
		//Arrays.copyOf(original, newLength)
		System.out.println(System.currentTimeMillis());
		
		long start=System.currentTimeMillis();
		for(int i=-9999999;i<99999999;i++) {
			for(int j=-999999;j<9999999;j++) {
				int result=i+j;
			}
		}
		//2 获取毫秒数
		long end=System.currentTimeMillis();
		System.out.println("用时:"+(end-start));
		
		
		new Student("aaa", 19);
		new Student("bbb", 19);
		new Student("ccc", 19);
		//3回收垃圾
		System.gc();//告诉垃圾回收期回收
		
		//4推出jvm
		System.exit(0);
		
		System.out.println("程序结束了....");
		
		
		
		
		
	}
}
