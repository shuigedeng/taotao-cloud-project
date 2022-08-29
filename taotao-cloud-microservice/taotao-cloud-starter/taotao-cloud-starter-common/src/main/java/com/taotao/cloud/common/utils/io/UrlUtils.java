/*
 * Copyright (c) 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
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

package com.taotao.cloud.common.utils.io;


import com.taotao.cloud.common.constant.CommonConstant;
import com.taotao.cloud.common.exception.CommonRuntimeException;
import com.taotao.cloud.common.utils.common.ArgUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * url处理工具类
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-02 19:41:13
 */
public class UrlUtils extends org.springframework.web.util.UriUtils {

	/**
	 * encode
	 *
	 * @param source source
	 * @return sourced String
	 */
	public static String encode(String source) {
		return UrlUtils.encode(source, StandardCharsets.UTF_8);
	}

	/**
	 * decode
	 *
	 * @param source source
	 * @return decoded String
	 */
	public static String decode(String source) {
		return UrlUtils.decode(source, StandardCharsets.UTF_8);
	}

	/**
	 * 读取每一行的内容
	 * @param url url 信息
	 * @return 结果
	 */
	public static List<String> readAllLines(final URL url) {
		return readAllLines(url, CommonConstant.UTF8);
	}

	/**
	 * 读取每一行的内容
	 * @param url url 信息
	 * @param charset 文件编码
	 * @return 结果
	 */
	public static List<String> readAllLines(final URL url,
											final String charset) {
		ArgUtils.notNull(url, "url");
		ArgUtils.notEmpty(charset, "charset");

		List<String> resultList = new ArrayList<>();

		try(InputStream is = url.openStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(is, Charset.forName(charset)))) {
			// 按行读取信息
			String line;
			while ((line = br.readLine()) != null) {
				resultList.add(line);
			}
		} catch (IOException e) {
			throw new CommonRuntimeException(e);
		}
		return resultList;
	}
}
