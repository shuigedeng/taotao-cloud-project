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

package com.taotao.cloud.common.utils.compiler;

import com.baomidou.mybatisplus.core.toolkit.StringPool;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.util.Collections;
import javax.tools.FileObject;
import javax.tools.ForwardingJavaFileManager;
import javax.tools.JavaCompiler;
import javax.tools.JavaCompiler.CompilationTask;
import javax.tools.JavaFileObject;
import javax.tools.JavaFileObject.Kind;
import javax.tools.SimpleJavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
import org.springframework.util.FastByteArrayOutputStream;

/**
 * 内存的代码编译器，参考自 oracle jdk
 *
  * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-02 19:41:13
 */
public class InMemoryJavaCompiler {

	/**
	 * JavaCompiler
	 */
	private static final JavaCompiler COMPILER = ToolProvider.getSystemJavaCompiler();

	/**
	 * Compiles the class with the given name and source code.
	 *
	 * @param className  The name of the class
	 * @param sourceCode The source code for the class with name {@code className}
	 * @return The resulting byte code from the compilation
	 * @throws IllegalArgumentException if the compilation did not succeed
	 */
	public static byte[] compile(String className, CharSequence sourceCode) {
		MemoryJavaFileObject file = new MemoryJavaFileObject(className, sourceCode);
		CompilationTask task = getCompilationTask(file);

		if (!task.call()) {
			throw new IllegalArgumentException(
				"Could not compile " + className + " with source code :\t" + sourceCode);
		}

		return file.getByteCode();
	}

	private static CompilationTask getCompilationTask(MemoryJavaFileObject file) {
		return COMPILER.getTask(null, new FileManagerWrapper(file), null, null, null,
			Collections.singletonList(file));
	}

	private static class MemoryJavaFileObject extends SimpleJavaFileObject {

		private final String className;
		private final CharSequence sourceCode;
		private final FastByteArrayOutputStream byteCode;

		public MemoryJavaFileObject(String className, CharSequence sourceCode) {
			super(URI.create("string:///" + className.replace(StringPool.DOT, StringPool.SLASH)
				+ Kind.SOURCE.extension), Kind.SOURCE);
			this.className = className;
			this.sourceCode = sourceCode;
			this.byteCode = new FastByteArrayOutputStream();
		}

		@Override
		public CharSequence getCharContent(boolean ignoreEncodingErrors) {
			return sourceCode;
		}

		@Override
		public OutputStream openOutputStream() throws IOException {
			return byteCode;
		}

		public byte[] getByteCode() {
			return byteCode.toByteArray();
		}

		public String getClassName() {
			return className;
		}
	}

	private static class FileManagerWrapper extends
		ForwardingJavaFileManager<StandardJavaFileManager> {

		private final MemoryJavaFileObject file;

		public FileManagerWrapper(MemoryJavaFileObject file) {
			super(COMPILER.getStandardFileManager(null, null, null));
			this.file = file;
		}

		@Override
		public JavaFileObject getJavaFileForOutput(Location location, String className, Kind kind,
			FileObject sibling) throws IOException {
			if (!file.getClassName().equals(className)) {
				throw new IOException(
					"Expected class with name " + file.getClassName() + ", but got " + className);
			}
			return file;
		}
	}

}
