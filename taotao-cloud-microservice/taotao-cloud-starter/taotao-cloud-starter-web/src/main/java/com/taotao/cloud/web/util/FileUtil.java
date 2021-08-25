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
package com.taotao.cloud.web.util;

import com.google.common.base.Preconditions;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

/**
 * 文件处理工具类
 *
 * @author shuigedeng
 * @version 1.0.0
 * @since 2021/8/24 23:00
 */
public class FileUtil {

	/**
	 * 允许的文件类型，可根据需求添加
	 */
	public static final String[] VALID_FILE_TYPE = {"xlsx", "zip"};

	/**
	 * 获取文件类型
	 *
	 * @param file 文件
	 * @return java.lang.String
	 * @throws Exception Exception
	 * @author shuigedeng
	 * @since 2021/8/24 23:00
	 */
	private static String getFileType(File file) throws Exception {
		Preconditions.checkNotNull(file);
		if (file.isDirectory()) {
			throw new Exception("file不是文件");
		}
		String fileName = file.getName();
		return fileName.substring(fileName.lastIndexOf(".") + 1);
	}

	/**
	 * 校验文件类型是否是允许下载的类型
	 *
	 * @param fileType fileType
	 * @return java.lang.Boolean
	 * @author shuigedeng
	 * @since 2021/8/24 23:01
	 */
	private static Boolean fileTypeIsValid(String fileType) {
		Preconditions.checkNotNull(fileType);
		fileType = StringUtils.lowerCase(fileType);
		return ArrayUtils.contains(VALID_FILE_TYPE, fileType);
	}

	/**
	 * 下载
	 *
	 * @param filePath filePath
	 * @param fileName fileName
	 * @param delete   delete
	 * @param response response
	 * @author shuigedeng
	 * @since 2021/8/24 23:01
	 */
	public static void download(String filePath, String fileName, Boolean delete,
		HttpServletResponse response) throws Exception {
		File file = new File(filePath);
		if (!file.exists()) {
			throw new Exception("文件未找到");
		}

		String fileType = getFileType(file);
		if (!fileTypeIsValid(fileType)) {
			throw new Exception("暂不支持该类型文件下载");
		}
		response.setHeader(HttpHeaders.CONTENT_DISPOSITION,
			"attachment;fileName=" + java.net.URLEncoder.encode(fileName, "utf-8"));
		response.setContentType(MediaType.MULTIPART_FORM_DATA_VALUE);
		response.setCharacterEncoding("utf-8");
		try (InputStream inputStream = new FileInputStream(
			file); OutputStream os = response.getOutputStream()) {
			byte[] b = new byte[2048];
			int length;
			while ((length = inputStream.read(b)) > 0) {
				os.write(b, 0, length);
			}
		} finally {
			if (delete) {
				delete(filePath);
			}
		}
	}

	/**
	 * 递归删除文件或目录
	 *
	 * @param filePath 文件或目录
	 * @author shuigedeng
	 * @since 2021/8/24 23:02
	 */
	public static void delete(String filePath) {
		File file = new File(filePath);
		if (file.isDirectory()) {
			File[] files = file.listFiles();
			if (files != null) {
				Arrays.stream(files).forEach(f -> delete(f.getPath()));
			}
		}
		file.delete();
	}

	/**
	 * 导出文件
	 *
	 * @param file file
	 * @return org.springframework.http.ResponseEntity<org.springframework.core.io.FileSystemResource>
	 * @author shuigedeng
	 * @since 2021/8/24 23:02
	 */
	public static ResponseEntity<FileSystemResource> export(File file) {
		if (file == null) {
			return null;
		}
		HttpHeaders headers = new HttpHeaders();
		headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
		headers.add("Content-Disposition", "attachment; filename=\"" + file.getName() + "\"");
		headers.add("Pragma", "no-cache");
		headers.add("Expires", "0");
		return ResponseEntity
			.ok()
			.headers(headers)
			.contentLength(file.length())
			.contentType(MediaType.parseMediaType("application/octet-stream"))
			.body(new FileSystemResource(file));
	}
}
