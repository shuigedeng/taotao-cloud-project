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

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import lombok.extern.slf4j.Slf4j;

/**
 * http请求操作类
 *
 * @author dengtao
 * @version 1.0.0
 * @link https://bbs.huaweicloud.com/blogs/104013
 * @since 2020/6/2 16:39
 */
@Slf4j
public class HttpUtil {

	/**
	 * 发送GET请求
	 *
	 * @param requestUrl
	 * @return
	 */
	public static Object getRequest(String requestUrl, String charSetName) {
		String res = "";
		StringBuilder buffer = new StringBuilder();
		try {
			URL url = new URL(requestUrl);
			HttpURLConnection urlCon = (HttpURLConnection) url.openConnection();
			if (200 == urlCon.getResponseCode()) {
				InputStream is = urlCon.getInputStream();
				InputStreamReader isr = new InputStreamReader(is, charSetName);
				BufferedReader br = new BufferedReader(isr);
				String str = null;
				while ((str = br.readLine()) != null) {
					buffer.append(str);
				}
				br.close();
				isr.close();
				is.close();
				res = buffer.toString();
				return res;
			} else {
				throw new Exception("连接失败");
			}
		} catch (Exception e) {
			LogUtil.error(e.getMessage(), e);
		}
		return null;
	}

	/**
	 * 发送POST请求
	 *
	 * @param path
	 * @param post
	 * @return
	 */
	public static Object postRequest(String path, String post) {
		URL url = null;
		try {
			url = new URL(path);
			HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
			// 提交模式
			httpURLConnection.setRequestMethod("POST");
			//连接超时 单位毫秒
			httpURLConnection.setConnectTimeout(10000);
			//读取超时 单位毫秒
			httpURLConnection.setReadTimeout(2000);
			// 发送POST请求必须设置如下两行
			httpURLConnection.setDoOutput(true);
			httpURLConnection.setDoInput(true);
			// 获取URLConnection对象对应的输出流
			PrintWriter printWriter = new PrintWriter(httpURLConnection.getOutputStream());
			// 发送请求参数
			//post的参数 xx=xx&yy=yy
			printWriter.write(post);
			// flush输出流的缓冲
			printWriter.flush();
			//开始获取数据
			BufferedInputStream bis = new BufferedInputStream(httpURLConnection.getInputStream());
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			int len;
			byte[] arr = new byte[1024];
			while ((len = bis.read(arr)) != -1) {
				bos.write(arr, 0, len);
				bos.flush();
			}
			bos.close();

			return bos.toString("utf-8");
		} catch (Exception e) {
			LogUtil.error(e.getMessage(), e);
		}
		return null;
	}
}
