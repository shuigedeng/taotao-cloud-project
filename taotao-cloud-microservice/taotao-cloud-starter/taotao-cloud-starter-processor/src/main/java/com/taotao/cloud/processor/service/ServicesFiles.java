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

package com.taotao.cloud.processor.service;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.lang.model.util.Elements;
import javax.tools.FileObject;

/**
 * A helper class for reading and writing Services files.
 */
class ServicesFiles {

	private static final Charset UTF_8 = StandardCharsets.UTF_8;

	/**
	 * Reads the set of service classes from a service file.
	 *
	 * @param fileObject not {@code null}. Closed after use.
	 * @return a not {@code null Set} of service class names.
	 */
	public static Set<String> readServiceFile(FileObject fileObject, Elements elementUtils)
		throws IOException {
		HashSet<String> serviceClasses = new HashSet<>();
		try (
			InputStream input = fileObject.openInputStream();
			InputStreamReader isr = new InputStreamReader(input, UTF_8);
			BufferedReader r = new BufferedReader(isr)
		) {
			String line;
			while ((line = r.readLine()) != null) {
				// 跳过注释行
				int commentStart = line.indexOf('#');
				if (commentStart >= 0) {
					continue;
				}
				line = line.trim();
				// 校验是否删除文件
				if (!line.isEmpty() && Objects.nonNull(elementUtils.getTypeElement(line))) {
					serviceClasses.add(line);
				}
			}
			return serviceClasses;
		}
	}

	/**
	 * Writes the set of service class names to a service file.
	 *
	 * @param output   not {@code null}. Not closed after use.
	 * @param services a not {@code null Collection} of service class names.
	 */
	public static void writeServiceFile(Collection<String> services, OutputStream output)
		throws IOException {
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(output, UTF_8));
		for (String service : services) {
			writer.write(service);
			writer.newLine();
		}
		writer.flush();
	}
}
