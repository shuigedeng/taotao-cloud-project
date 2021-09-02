/*
 * Copyright 2002-2021 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.taotao.cloud.common.utils;

import com.taotao.cloud.common.exception.BaseException;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

/**
 * FileUtil
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-02 16:44:44
 */
public class FileUtil {

	private FileUtil() {
	}

	/**
	 * 文件是否存在
	 *
	 * @param filepath 文件路径
	 * @return boolean
	 * @author shuigedeng
	 * @since 2021-09-02 16:46:10
	 */
	public static boolean existFile(String filepath) {
		File file = new File(filepath);
		return file.exists();
	}

	/**
	 * 获取文件目录路径
	 *
	 * @param path 文件路径
	 * @return {@link java.lang.String }
	 * @author shuigedeng
	 * @since 2021-09-02 16:46:03
	 */
	public static String getDirectoryPath(String path) {
		File file = new File(path);
		return file.getAbsolutePath();
	}

	/**
	 * 获取文件目录路径
	 *
	 * @param cls cls
	 * @return {@link java.lang.String }
	 * @author shuigedeng
	 * @since 2021-09-02 16:45:57
	 */
	public static String getDirectoryPath(Class<?> cls) {
		File file = getJarFile(cls);
		if (file == null) {
			return null;
		}
		if (!file.isDirectory()) {
			file = file.getParentFile();
		}
		return file.getAbsolutePath();
	}

	/**
	 * 获取文件
	 *
	 * @param cls 类型
	 * @return {@link java.io.File }
	 * @author shuigedeng
	 * @since 2021-09-02 16:45:49
	 */
	public static File getJarFile(Class<?> cls) {
		String path = cls.getProtectionDomain().getCodeSource().getLocation().getFile();
		try {
			// 转换处理中文及空格
			path = java.net.URLDecoder.decode(path, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			return null;
		}
		return new File(path);
	}

	// /**
	//  * 获取文件路径
	//  *
	//  * @param paths 路径列表
	//  * @return java.lang.String
	//  * @author shuigedeng
	//  * @since 2020/10/15 15:03
	//  * @version 1.0.0
	//  */
	// public String getFilePath(String... paths) {
	//     StringBuilder sb = new StringBuilder();
	//     for (String path : paths) {
	//         sb.append(org.springframework.util.StringUtils.trimTrailingCharacter(path, File.separatorChar));
	//         sb.append(File.separator);
	//     }
	//     return org.springframework.util.StringUtils.trimTrailingCharacter(sb.toString(), File.separatorChar);
	// }


	/**
	 * 创建目录
	 *
	 * @param path 文件路径
	 * @return {@link java.lang.Boolean }
	 * @author shuigedeng
	 * @since 2021-09-02 16:45:40
	 */
	public static Boolean createDirectory(String path) {
		File file = new File(path);
		if (!file.isDirectory()) {
			file = file.getParentFile();
		}
		if (!file.exists()) {
			return file.mkdirs();
		} else {
			return false;
		}
	}

	/**
	 * 追加文件内容
	 *
	 * @param path     文件路径
	 * @param contents 内容
	 * @author shuigedeng
	 * @since 2021-09-02 16:45:30
	 */
	public static void appendAllText(String path, String contents) {
		try {
			//如果文件存在，则追加内容；如果文件不存在，则创建文件
			File f = new File(path);
			try (FileWriter fw = new FileWriter(f, true)) {
				try (PrintWriter pw = new PrintWriter(fw)) {
					pw.println(contents);
					pw.flush();
					fw.flush();
				}
			}
		} catch (IOException exp) {
			throw new BaseException("追加文件异常", exp);
		}
	}

	/**
	 * 写文件内容
	 *
	 * @param path     文件路径
	 * @param contents 内容
	 * @author shuigedeng
	 * @since 2021-09-02 16:45:20
	 */
	public static void writeAllText(String path, String contents) {
		try {
			File f = new File(path);
			if (f.exists()) {
				f.delete();
			}

			if (f.createNewFile()) {
				try (BufferedWriter output = new BufferedWriter(new FileWriter(f))) {
					output.write(contents);
				}
			}
		} catch (IOException exp) {
			throw new BaseException("写文件异常", exp);
		}
	}

	/**
	 * 读取文件内容
	 *
	 * @param path 文件路径
	 * @return {@link java.lang.String }
	 * @author shuigedeng
	 * @since 2021-09-02 16:45:10
	 */
	public static String readAllText(String path) {
		try {
			File f = new File(path);
			if (f.exists()) {
				//获取文件长度
				long fileLength = f.length();
				byte[] fileContent = new byte[(int) fileLength];
				try (FileInputStream in = new FileInputStream(f)) {
					in.read(fileContent);
				}
				//返回文件内容,默认编码
				return new String(fileContent);
			} else {
				throw new FileNotFoundException(path);
			}
		} catch (IOException exp) {
			throw new BaseException("读文件异常", exp);
		}
	}

	/**
	 * 获取行分隔符
	 *
	 * @return {@link java.lang.String }
	 * @author shuigedeng
	 * @since 2021-09-02 16:45:02
	 */
	public static String lineSeparator() {
		return System.getProperty("line.separator");
	}

	/**
	 * 根据文件路径获取文件名
	 *
	 * @param filePath 文件路径
	 * @return {@link java.lang.String }
	 * @author shuigedeng
	 * @since 2021-09-02 16:44:55
	 */
	public static String getFileName(String filePath) {
		String path = filePath.replace("\\\\", "/");
		return path.substring(path.lastIndexOf("/") + 1, path.length());
	}
}
