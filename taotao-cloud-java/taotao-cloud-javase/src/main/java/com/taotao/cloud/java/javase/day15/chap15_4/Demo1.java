package com.taotao.cloud.java.javase.day15.chap15_4;

import java.io.File;
import java.io.FileFilter;
import java.sql.Date;
import java.util.Properties;

/**
 * File类的使用
 * （1）分隔符
 * （2）文件操作
 * （3）文件夹操作
 * @author wgy
 *
 */
public class Demo1 {
	public static void main(String[] args) throws Exception {
		//separator();
		//fileOpe();
		directoryOpe();
	}
	//（1）分隔符
	public static void separator() {
		System.out.println("路径分隔符"+File.pathSeparator);
		System.out.println("名称分隔符"+File.separator);
	}
	//（2）文件操作
	public static void fileOpe() throws Exception {
		//1创建文件 createNewFile()
		File file=new File("d:\\file.txt");
		//System.out.println(file.toString());
		if(!file.exists()) {
			boolean b=file.createNewFile();
			System.out.println("创建结果:"+b);
		}
		//2删除文件
		//2.1直接删除
		//System.out.println("删除结果:"+file.delete());
		//2.2使用jvm退出时删除
//		file.deleteOnExit();
//		Thread.sleep(5000);
		
		//3获取文件信息
		System.out.println("获取文件的绝对路径:"+file.getAbsolutePath());
		System.out.println("获取路径:"+file.getPath());
		System.out.println("获取文件名称:"+file.getName());
		System.out.println("获取父目录:"+file.getParent());
		System.out.println("获取文件长度:"+file.length());
		System.out.println("文件创建时间:"+new Date(file.lastModified()).toLocaleString());
		
		
		//4判断
		System.out.println("是否可写:"+file.canWrite());
		System.out.println("是否时文件:"+file.isFile());
		System.out.println("是否隐藏:"+file.isHidden());
		
	}
	
	//（3）文件夹操作
	public static void directoryOpe() throws Exception{
		//1 创建文件夹
		File dir=new File("d:\\aaa\\bbb\\ccc");
		System.out.println(dir.toString());
		if(!dir.exists()) {
			//dir.mkdir();//只能创建单级目录
			System.out.println("创建结果:"+dir.mkdirs());//创建多级目录
		}
		
		//2 删除文件夹
		//2.1直接删除(注意删除空目录)
		//System.out.println("删除结果:"+dir.delete());
		//2.2使用jvm删除
//		dir.deleteOnExit();
//		Thread.sleep(5000);
		//3获取文件夹信息
		System.out.println("获取绝对路径："+dir.getAbsolutePath());
		System.out.println("获取路径:"+dir.getPath());
		System.out.println("获取文件夹名称："+dir.getName());
		System.out.println("获取父目录："+dir.getParent());
		System.out.println("获取创建时间:"+new Date(dir.lastModified()).toLocaleString());
		
		
		//4判断
		System.out.println("是否时文件夹:"+dir.isDirectory());
		System.out.println("是否时隐藏："+dir.isHidden());
		
		//5遍历文件夹
		File dir2=new File("d:\\图片");
		String[] files=dir2.list();
		System.out.println("--------------------------------");
		for (String string : files) {
			System.out.println(string);
		}
		System.out.println("-----------FileFilter接口的使用-----------");
		
		File[] files2=dir2.listFiles(new FileFilter() {
			
			@Override
			public boolean accept(File pathname) {
				if(pathname.getName().endsWith(".jpg")) {
					return true;
				}
				return false;
			}
		});
		for (File file : files2) {
			System.out.println(file.getName());
		}
	}
	
}
