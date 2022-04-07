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

package com.taotao.cloud.common.utils.exception;


import com.baomidou.mybatisplus.core.toolkit.StringPool;
import java.io.PrintWriter;

/**
 * 快速的 PrintWriter，用来处理异常信息，转化为字符串
 *
 * 1. 默认容量为 256
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-02 19:41:13
 */
public class FastStringPrintWriter extends PrintWriter {
	private final FastStringWriter writer;

	public FastStringPrintWriter() {
		this(256);
	}

	public FastStringPrintWriter(int initialSize) {
		super(new FastStringWriter(initialSize));
		this.writer = (FastStringWriter) out;
	}

	/**
	 * Throwable printStackTrace，只掉用了该方法
	 *
	 * @param x Object
	 */
	@Override
	public void println(Object x) {
		writer.write(String.valueOf(x));
		writer.write(StringPool.NEWLINE);
	}

	@Override
	public String toString() {
		return writer.toString();
	}
}
