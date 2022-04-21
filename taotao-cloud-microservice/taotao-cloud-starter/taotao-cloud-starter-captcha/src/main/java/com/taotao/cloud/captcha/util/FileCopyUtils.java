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
package com.taotao.cloud.captcha.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.file.Files;

/**
 * FileCopyUtils
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-03 21:03:49
 */
public class FileCopyUtils {

	public static final int BUFFER_SIZE = 4096;

	public FileCopyUtils() {
	}

	/**
	 * copy
	 *
	 * @param in  in
	 * @param out out
	 * @return int
	 * @since 2021-09-03 21:04:45
	 */
	public static int copy(File in, File out) throws IOException {
		return copy(Files.newInputStream(in.toPath()), Files.newOutputStream(out.toPath()));
	}

	/**
	 * copy
	 *
	 * @param in  in
	 * @param out out
	 * @since 2021-09-03 21:04:39
	 */
	public static void copy(byte[] in, File out) throws IOException {
		copy(new ByteArrayInputStream(in),
			Files.newOutputStream(out.toPath()));
	}

	/**
	 * copyToByteArray
	 *
	 * @param in in
	 * @return byte[]
	 * @since 2021-09-03 21:04:35
	 */
	public static byte[] copyToByteArray(File in) throws IOException {
		return copyToByteArray(Files.newInputStream(in.toPath()));
	}

	/**
	 * copy
	 *
	 * @param in  in
	 * @param out out
	 * @return int
	 * @since 2021-09-03 21:04:27
	 */
	public static int copy(InputStream in, OutputStream out) throws IOException {
		int var2;
		try (in; out) {
			var2 = StreamUtils.copy(in, out);
		}

		return var2;
	}

	/**
	 * copy
	 *
	 * @param in  in
	 * @param out out
	 * @since 2021-09-03 21:04:16
	 */
	public static void copy(byte[] in, OutputStream out) throws IOException {
		try (out) {
			out.write(in);
		}
	}

	/**
	 * copyToByteArray
	 *
	 * @param in in
	 * @return byte[]
	 * @since 2021-09-03 21:04:12
	 */
	public static byte[] copyToByteArray(InputStream in) throws IOException {
		if (in == null) {
			return new byte[0];
		} else {
			ByteArrayOutputStream out = new ByteArrayOutputStream(4096);
			copy((InputStream) in, (OutputStream) out);
			return out.toByteArray();
		}
	}

	/**
	 * copy
	 *
	 * @param in  in
	 * @param out out
	 * @return int
	 * @since 2021-09-03 21:04:08
	 */
	public static int copy(Reader in, Writer out) throws IOException {
		try (in; out) {
			int byteCount = 0;
			char[] buffer = new char[4096];

			int bytesRead;
			for (boolean var4 = true; (bytesRead = in.read(buffer)) != -1; byteCount += bytesRead) {
				out.write(buffer, 0, bytesRead);
			}

			out.flush();
			return byteCount;
		}
	}

	/**
	 * copy
	 *
	 * @param in  in
	 * @param out out
	 * @since 2021-09-03 21:04:02
	 */
	public static void copy(String in, Writer out) throws IOException {
		try (out) {
			out.write(in);
		}
	}

	/**
	 * copyToString
	 *
	 * @param in in
	 * @return {@link java.lang.String }
	 * @since 2021-09-03 21:04:00
	 */
	public static String copyToString(Reader in) throws IOException {
		if (in == null) {
			return "";
		} else {
			StringWriter out = new StringWriter();
			copy((Reader) in, (Writer) out);
			return out.toString();
		}
	}
}

