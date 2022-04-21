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
package com.taotao.cloud.p6spy.logger;


import com.p6spy.engine.spy.P6SpyOptions;
import com.p6spy.engine.spy.appender.FileLogger;
import com.taotao.cloud.common.utils.context.ContextUtil;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * FileLogger
 *
 * @author shuigedeng
 * @version 2022.03
 * @since 2021/10/28 09:33
 */
public class CustomFileLogger extends FileLogger {

	private String fileName = null;
	private PrintStream printStream = null;

	private void init() {
		if (fileName == null) {
			throw new IllegalStateException("setLogfile() must be called before init()");
		}

		ConfigurableApplicationContext applicationContext = ContextUtil.getApplicationContext();
		String realFileName = applicationContext.getEnvironment().resolvePlaceholders(fileName);

		try {
			File file = new File(realFileName);
			if (!file.getParentFile().exists()) {
				file.getParentFile().mkdirs();
			}

			if (!file.exists()) {
				file.createNewFile();
			}

			printStream = new PrintStream(
				new FileOutputStream(file, P6SpyOptions.getActiveInstance().getAppend()));
		} catch (IOException e) {
			throw new IllegalStateException("couldn't create PrintStream for " + fileName, e);
		}
	}

	@Override
	protected PrintStream getStream() {
		// Lazy init to allow for the appender to be changed at Runtime without creating an empty log file (assuming
		// that no log message has been written yet)
		if (printStream == null) {
			synchronized (this) {
				if (printStream == null) {
					init();
				}
			}
		}
		return printStream;
	}

	public void setLogfile(String fileName) {
		this.fileName = fileName;
	}
}
