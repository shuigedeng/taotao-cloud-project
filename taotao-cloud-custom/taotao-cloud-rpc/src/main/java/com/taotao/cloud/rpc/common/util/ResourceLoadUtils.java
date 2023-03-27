package com.taotao.cloud.rpc.common.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class ResourceLoadUtils {

	private static Map<String, String> resourceLoaders = new HashMap<>();

	/**
	 * 加载 Jar 包外置配置文件 找不到返回 null
	 *
	 * @param resourceName
	 * @return
	 */
	public static Map<String, String> load(String resourceName) {
		//2.1 创建Properties对象
		Properties p = new Properties();
		//2.2 调用p对象中的load方法进行配置文件的加载
		// 使用InPutStream流读取properties文件
		String currentWorkPath = System.getProperty("user.dir");
		InputStream is = null;
		String propertyValue = "";
		try (BufferedReader bufferedReader = new BufferedReader(
			new FileReader(currentWorkPath + "/config/" + resourceName));) {
			p.load(bufferedReader);
			Enumeration<Object> keys = p.keys();
			while (keys.hasMoreElements()) {
				String property = (String) keys.nextElement();
				propertyValue = p.getProperty(property);
				log.info("discover key: {}, value: {} in {}", property, propertyValue,
					resourceName);
				resourceLoaders.put(property, propertyValue);
			}
			log.info("read resource from resource path: {}",
				currentWorkPath + "/config/" + resourceName);
			return resourceLoaders;
		} catch (IOException e) {
			log.info("not found resource from resource path: {}",
				currentWorkPath + "/config/" + resourceName);
			return null;
		}
	}
}
