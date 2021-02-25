package com.taotao.cloud.java.javase.day15.chap15_4;

import java.io.File;

/**
 * 案例1：递归遍历文件夹
 * 案例2：递归删除文件夹
 * @author wgy
 *
 */
public class ListDemo {
	public static void main(String[] args) {
		//listDir(new File("d:\\myfiles"));
		deleteDir(new File("d:\\myfiles"));
	}
	//案例1：递归遍历文件夹
	public static void listDir(File dir) {
		File[] files=dir.listFiles();
		System.out.println(dir.getAbsolutePath());
		if(files!=null&&files.length>0) {
			for (File file : files) {
				if(file.isDirectory()) {
					listDir(file);//递归
				}else {
					System.out.println(file.getAbsolutePath());
				}
			}
		}
	}
	//案例2：递归删除文件夹
	public static void deleteDir(File dir) {
		File[] files=dir.listFiles();
		if(files!=null&&files.length>0) {
			for (File file : files) {
				if(file.isDirectory()) {
					deleteDir(file);//递归
				}else {
					//删除文件
					System.out.println(file.getAbsolutePath()+"删除:"+file.delete());
				}
			}
		}
		System.out.println(dir.getAbsolutePath()+"删除:"+dir.delete());
	}
	
}
