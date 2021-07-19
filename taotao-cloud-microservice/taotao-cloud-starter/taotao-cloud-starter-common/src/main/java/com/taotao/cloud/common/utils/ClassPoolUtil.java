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
package com.taotao.cloud.common.utils;

import javassist.ClassPool;
import javassist.LoaderClassPath;
import lombok.experimental.UtilityClass;

/**
 * ClassPoolUtils
 *
 * @author shuigedeng
 * @version 1.0.0
 * @since 2020/6/2 16:33
 */
@UtilityClass
public class ClassPoolUtil {

	public static volatile ClassPool instance;

	/**
	 * 获取对象池
	 *
	 * @return javassist.ClassPool
	 * @author shuigedeng
	 * @since 2021/2/25 16:12
	 */
	public ClassPool getInstance() {
		if (instance == null) {
			synchronized (ClassPoolUtil.class) {
				if (instance == null) {
					ClassPool aDefault = ClassPool.getDefault();
					aDefault.appendClassPath(
						new LoaderClassPath(Thread.currentThread().getContextClassLoader()));
					instance = aDefault;
				}
			}
		}
		return instance;
	}
}
