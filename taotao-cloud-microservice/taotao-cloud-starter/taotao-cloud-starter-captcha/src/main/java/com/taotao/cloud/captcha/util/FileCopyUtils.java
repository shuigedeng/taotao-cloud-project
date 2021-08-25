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
 * @version 1.0.0
 * @since 2021/8/24 16:52
 */
public abstract class FileCopyUtils {

	public static final int BUFFER_SIZE = 4096;

	public FileCopyUtils() {
	}

	public static int copy(File in, File out) throws IOException {
		return copy(Files.newInputStream(in.toPath()), Files.newOutputStream(out.toPath()));
	}

	public static void copy(byte[] in, File out) throws IOException {
		copy((InputStream) (new ByteArrayInputStream(in)),
			(OutputStream) Files.newOutputStream(out.toPath()));
	}

	public static byte[] copyToByteArray(File in) throws IOException {
		return copyToByteArray(Files.newInputStream(in.toPath()));
	}

	public static int copy(InputStream in, OutputStream out) throws IOException {
		int var2;
		try {
			var2 = StreamUtils.copy(in, out);
		} finally {
			try {
				in.close();
			} catch (IOException var12) {
			}

			try {
				out.close();
			} catch (IOException var11) {
			}

		}

		return var2;
	}

	public static void copy(byte[] in, OutputStream out) throws IOException {
		try {
			out.write(in);
		} finally {
			try {
				out.close();
			} catch (IOException var8) {
			}

		}

	}

	public static byte[] copyToByteArray(InputStream in) throws IOException {
		if (in == null) {
			return new byte[0];
		} else {
			ByteArrayOutputStream out = new ByteArrayOutputStream(4096);
			copy((InputStream) in, (OutputStream) out);
			return out.toByteArray();
		}
	}

	public static int copy(Reader in, Writer out) throws IOException {
		try {
			int byteCount = 0;
			char[] buffer = new char[4096];

			int bytesRead;
			for (boolean var4 = true; (bytesRead = in.read(buffer)) != -1; byteCount += bytesRead) {
				out.write(buffer, 0, bytesRead);
			}

			out.flush();
			int var5 = byteCount;
			return var5;
		} finally {
			try {
				in.close();
			} catch (IOException var15) {
			}

			try {
				out.close();
			} catch (IOException var14) {
			}

		}
	}

	public static void copy(String in, Writer out) throws IOException {
		try {
			out.write(in);
		} finally {
			try {
				out.close();
			} catch (IOException var8) {
			}

		}

	}

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

