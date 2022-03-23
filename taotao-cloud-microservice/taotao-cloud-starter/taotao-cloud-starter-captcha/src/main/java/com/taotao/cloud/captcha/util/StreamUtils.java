
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
import java.io.FilterInputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;

/**
 * StreamUtils
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-03 21:06:23
 */
public class StreamUtils {

	public static final int BUFFER_SIZE = 4096;
	private static final byte[] EMPTY_CONTENT = new byte[0];

	public StreamUtils() {
	}

	/**
	 * copyToByteArray
	 *
	 * @param in in
	 * @return byte[]
	 * @since 2021-09-03 21:06:25
	 */
	public static byte[] copyToByteArray(InputStream in) throws IOException {
		if (in == null) {
			return new byte[0];
		} else {
			ByteArrayOutputStream out = new ByteArrayOutputStream(4096);
			copy(in, out);
			return out.toByteArray();
		}
	}

	/**
	 * copyToString
	 *
	 * @param in      in
	 * @param charset charset
	 * @return {@link String }
	 * @since 2021-09-03 21:06:28
	 */
	public static String copyToString(InputStream in, Charset charset) throws IOException {
		if (in == null) {
			return "";
		} else {
			StringBuilder out = new StringBuilder();
			InputStreamReader reader = new InputStreamReader(in, charset);
			char[] buffer = new char[4096];
			boolean var5 = true;

			int bytesRead;
			while ((bytesRead = reader.read(buffer)) != -1) {
				out.append(buffer, 0, bytesRead);
			}

			return out.toString();
		}
	}

	/**
	 * copy
	 *
	 * @param in  in
	 * @param out out
	 * @since 2021-09-03 21:06:31
	 */
	public static void copy(byte[] in, OutputStream out) throws IOException {
		out.write(in);
	}

	/**
	 * copy
	 *
	 * @param in      in
	 * @param charset charset
	 * @param out     out
	 * @since 2021-09-03 21:06:33
	 */
	public static void copy(String in, Charset charset, OutputStream out) throws IOException {
		Writer writer = new OutputStreamWriter(out, charset);
		writer.write(in);
		writer.flush();
	}

	/**
	 * copy
	 *
	 * @param in  in
	 * @param out out
	 * @return int
	 * @since 2021-09-03 21:06:35
	 */
	public static int copy(InputStream in, OutputStream out) throws IOException {
		int byteCount = 0;
		byte[] buffer = new byte[4096];

		int bytesRead;
		for (boolean var4 = true; (bytesRead = in.read(buffer)) != -1; byteCount += bytesRead) {
			out.write(buffer, 0, bytesRead);
		}

		out.flush();
		return byteCount;
	}

	/**
	 * copyRange
	 *
	 * @param in    in
	 * @param out   out
	 * @param start start
	 * @param end   end
	 * @return long
	 * @since 2021-09-03 21:06:37
	 */
	public static long copyRange(InputStream in, OutputStream out, long start, long end)
		throws IOException {
		long skipped = in.skip(start);
		if (skipped < start) {
			throw new IOException(
				"Skipped only " + skipped + " bytes out of " + start + " required");
		} else {
			long bytesToCopy = end - start + 1L;
			byte[] buffer = new byte[4096];

			while (bytesToCopy > 0L) {
				int bytesRead = in.read(buffer);
				if (bytesRead == -1) {
					break;
				}

				if ((long) bytesRead <= bytesToCopy) {
					out.write(buffer, 0, bytesRead);
					bytesToCopy -= (long) bytesRead;
				} else {
					out.write(buffer, 0, (int) bytesToCopy);
					bytesToCopy = 0L;
				}
			}

			return end - start + 1L - bytesToCopy;
		}
	}

	/**
	 * drain
	 *
	 * @param in in
	 * @return int
	 * @since 2021-09-03 21:06:42
	 */
	public static int drain(InputStream in) throws IOException {
		byte[] buffer = new byte[4096];
		int byteCount;
		int bytesRead;
		for (byteCount = 0; (bytesRead = in.read(buffer)) != -1; byteCount += bytesRead) {
		}

		return byteCount;
	}

	/**
	 * emptyInput
	 *
	 * @return {@link InputStream }
	 * @since 2021-09-03 21:06:44
	 */
	public static InputStream emptyInput() {
		return new ByteArrayInputStream(EMPTY_CONTENT);
	}

	/**
	 * nonClosing
	 *
	 * @param in in
	 * @return {@link InputStream }
	 * @since 2021-09-03 21:06:46
	 */
	public static InputStream nonClosing(InputStream in) {
		return new NonClosingInputStream(in);
	}

	/**
	 * nonClosing
	 *
	 * @param out out
	 * @return {@link java.io.OutputStream }
	 * @author shuigedeng
	 * @since 2021-09-03 21:06:49
	 */
	public static OutputStream nonClosing(OutputStream out) {
		return new NonClosingOutputStream(out);
	}

	/**
	 * NonClosingOutputStream
	 *
	 * @version 2021.9
	 * @since 2021-09-03 21:06:57
	 */
	private static class NonClosingOutputStream extends FilterOutputStream {

		public NonClosingOutputStream(OutputStream out) {
			super(out);
		}

		public void write(byte[] b, int off, int let) throws IOException {
			this.out.write(b, off, let);
		}

		public void close() throws IOException {
		}
	}

	/**
	 * NonClosingInputStream
	 *
	 * @version 2021.9
	 * @since 2021-09-03 21:07:04
	 */
	private static class NonClosingInputStream extends FilterInputStream {

		public NonClosingInputStream(InputStream in) {
			super(in);
		}

		public void close() throws IOException {
		}
	}
}

