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


import com.taotao.cloud.common.utils.log.LogUtil;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectStreamClass;

/**
 * 忽略序列化 id 的 jdk 对象序列化
 *
 * <p>
 * 参考：https://stackoverflow.com/questions/1816559/make-java-runtime-ignore-serialversionuids
 * </p>
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-02 19:41:13
 */
public class IgnoreSerIdObjectInputStream extends ObjectInputStream {

	public IgnoreSerIdObjectInputStream(byte[] bytes) throws IOException {
		this(new ByteArrayInputStream(bytes));
	}

	public IgnoreSerIdObjectInputStream(InputStream in) throws IOException {
		super(in);
	}

	@Override
	protected ObjectStreamClass readClassDescriptor() throws IOException, ClassNotFoundException {
		// initially streams descriptor
		ObjectStreamClass resultClassDescriptor = super.readClassDescriptor();
		// the class in the local JVM that this descriptor represents.
		Class<?> localClass;
		try {
			localClass = Class.forName(resultClassDescriptor.getName());
		} catch (ClassNotFoundException e) {
			LogUtil.warn("No local class for " + resultClassDescriptor.getName());
			return resultClassDescriptor;
		}

		ObjectStreamClass localClassDescriptor = ObjectStreamClass.lookup(localClass);
		// only if class implements serializable
		if (localClassDescriptor != null) {
			long localSerId = localClassDescriptor.getSerialVersionUID();
			long streamSerId = resultClassDescriptor.getSerialVersionUID();
			// check for serialVersionUID mismatch.
			if (streamSerId != localSerId) {
				LogUtil.warn(
					"Overriding serialized class {} version mismatch: local serialVersionUID = {} stream serialVersionUID = {}",
					localClass, localSerId, streamSerId);
				// Use local class descriptor for deserialization
				resultClassDescriptor = localClassDescriptor;
			}
		}
		return resultClassDescriptor;
	}

}
