/*
 * Copyright 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
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
package com.taotao.cloud.common.utils.secure;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.zip.CRC32;
import java.util.zip.CheckedInputStream;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;

/**
 * 加密工具类，包含MD5,BASE64,SHA,CRC32
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-02 17:35:02
 */
public class CryptoUtil {

	private CryptoUtil() {
	}

	/**
	 * DEFAULT_CHARSET
	 */
	private static final String DEFAULT_CHARSET = "UTF-8";

	/**
	 * MD5加密
	 *
	 * @param bytes bytes
	 * @return {@link java.lang.String }
	 * @author shuigedeng
	 * @since 2021-09-02 17:35:29
	 */
	public static String encodeMD5(final byte[] bytes) {
		return DigestUtils.md5Hex(bytes);
	}

	/**
	 * MD5加密，默认UTF-8
	 *
	 * @param str str
	 * @return {@link java.lang.String }
	 * @author shuigedeng
	 * @since 2021-09-02 17:35:34
	 */
	public static String encodeMD5(final String str) {
		return encodeMD5(str, DEFAULT_CHARSET);
	}

	/**
	 * MD5加密
	 *
	 * @param str     str
	 * @param charset charset
	 * @return {@link java.lang.String }
	 * @author shuigedeng
	 * @since 2021-09-02 17:35:38
	 */
	public static String encodeMD5(final String str, final String charset) {
		if (str == null) {
			return null;
		}
		try {
			byte[] bytes = str.getBytes(charset);
			return encodeMD5(bytes);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * SHA加密
	 *
	 * @param bytes bytes
	 * @return {@link java.lang.String }
	 * @author shuigedeng
	 * @since 2021-09-02 17:35:43
	 */
	public static String encodeSHA(final byte[] bytes) {
		return DigestUtils.sha512Hex(bytes);
	}

	/**
	 * SHA加密
	 *
	 * @param str     str
	 * @param charset charset
	 * @return {@link java.lang.String }
	 * @author shuigedeng
	 * @since 2021-09-02 17:35:48
	 */
	public static String encodeSHA(final String str, final String charset) {
		if (str == null) {
			return null;
		}
		try {
			byte[] bytes = str.getBytes(charset);
			return encodeSHA(bytes);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * SHA加密,默认utf-8
	 *
	 * @param str str
	 * @return {@link java.lang.String }
	 * @author shuigedeng
	 * @since 2021-09-02 17:35:53
	 */
	public static String encodeSHA(final String str) {
		return encodeSHA(str, DEFAULT_CHARSET);
	}

	/**
	 * BASE64加密
	 *
	 * @param bytes bytes
	 * @return {@link java.lang.String }
	 * @author shuigedeng
	 * @since 2021-09-02 17:35:57
	 */
	public static String encodeBASE64(final byte[] bytes) {
		return new String(Base64.encodeBase64String(bytes));
	}

	/**
	 * BASE64加密
	 *
	 * @param str     str
	 * @param charset charset
	 * @return {@link java.lang.String }
	 * @author shuigedeng
	 * @since 2021-09-02 17:36:02
	 */
	public static String encodeBASE64(final String str, String charset) {
		if (str == null) {
			return null;
		}
		try {
			byte[] bytes = str.getBytes(charset);
			return encodeBASE64(bytes);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * BASE64加密,默认UTF-8
	 *
	 * @param str str
	 * @return {@link java.lang.String }
	 * @author shuigedeng
	 * @since 2021-09-02 17:36:07
	 */
	public static String encodeBASE64(final String str) {
		return encodeBASE64(str, DEFAULT_CHARSET);
	}

	/**
	 * BASE64解密,默认UTF-8
	 *
	 * @param str str
	 * @return {@link java.lang.String }
	 * @author shuigedeng
	 * @since 2021-09-02 17:36:14
	 */
	public static String decodeBASE64(String str) {
		return decodeBASE64(str, DEFAULT_CHARSET);
	}

	/**
	 * BASE64解密
	 *
	 * @param str     str
	 * @param charset charset
	 * @return {@link java.lang.String }
	 * @author shuigedeng
	 * @since 2021-09-02 17:36:18
	 */
	public static String decodeBASE64(String str, String charset) {
		try {
			byte[] bytes = str.getBytes(charset);
			return new String(Base64.decodeBase64(bytes));
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * CRC32字节校验
	 *
	 * @param bytes bytes
	 * @return {@link java.lang.String }
	 * @author shuigedeng
	 * @since 2021-09-02 17:36:22
	 */
	public static String crc32(byte[] bytes) {
		CRC32 crc32 = new CRC32();
		crc32.update(bytes);
		return Long.toHexString(crc32.getValue());
	}

	/**
	 * CRC32字符串校验
	 *
	 * @param str     str
	 * @param charset charset
	 * @return {@link java.lang.String }
	 * @author shuigedeng
	 * @since 2021-09-02 17:36:27
	 */
	public static String crc32(final String str, String charset) {
		try {
			byte[] bytes = str.getBytes(charset);
			return crc32(bytes);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * CRC32字符串校验,默认UTF-8编码读取
	 *
	 * @param str str
	 * @return {@link java.lang.String }
	 * @author shuigedeng
	 * @since 2021-09-02 17:36:37
	 */
	public static String crc32(final String str) {
		return crc32(str, DEFAULT_CHARSET);
	}

	/**
	 * CRC32流校验
	 *
	 * @param input input
	 * @return {@link java.lang.String }
	 * @author shuigedeng
	 * @since 2021-09-02 17:36:42
	 */
	public static String crc32(InputStream input) {
		CRC32 crc32 = new CRC32();
		CheckedInputStream checkInputStream = null;
		int test = 0;
		try {
			checkInputStream = new CheckedInputStream(input, crc32);
			do {
				test = checkInputStream.read();
			} while (test != -1);
			return Long.toHexString(crc32.getValue());
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	/**
	 * CRC32文件唯一校验
	 *
	 * @param file file
	 * @return {@link java.lang.String }
	 * @author shuigedeng
	 * @since 2021-09-02 17:36:47
	 */
	public static String crc32(File file) {
		InputStream input = null;
		try {
			input = new BufferedInputStream(new FileInputStream(file));
			return crc32(input);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} finally {
			IOUtils.closeQuietly(input);
		}
	}

	/**
	 * CRC32文件唯一校验
	 *
	 * @param url url
	 * @return {@link java.lang.String }
	 * @author shuigedeng
	 * @since 2021-09-02 17:36:51
	 */
	public static String crc32(URL url) {
		InputStream input = null;
		try {
			input = url.openStream();
			return crc32(input);
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} finally {
			IOUtils.closeQuietly(input);
		}
	}

}
