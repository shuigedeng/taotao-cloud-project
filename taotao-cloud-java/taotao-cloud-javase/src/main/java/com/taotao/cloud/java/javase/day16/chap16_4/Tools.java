package com.taotao.cloud.java.javase.day16.chap16_4;
/**
 * 工具类
 * @author wgy
 *
 */

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class Tools {
	//1加载属性文件
	public static Properties loadProperties() {
		//1创建属性集合
		Properties properties=new Properties();
		//2判断文件是否存在
		File file=new File("users.properties");
		if(file.exists()) {
			FileInputStream fis=null;
			try {
				fis = new FileInputStream(file);
				properties.load(fis);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally {
				if(fis!=null) {
					try {
						fis.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			
		}
		
		return properties;
	}
	
	//2保存属性文件
	
	public static void saveProperties(String json) {
		String[] infos=json.substring(1, json.length()-1).split(",");
		String id=infos[0].split(":")[1];
		//保存
		FileOutputStream fos=null;
		try {
			fos=new FileOutputStream("users.properties",true);
			Properties properties=new Properties();
			properties.setProperty(id, json);
			properties.store(fos, "");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			if(fos!=null) {
				try {
					fos.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
	}
}
