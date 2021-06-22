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
package com.taotao.cloud.common.base;

/**
 * 实现了此接口，表示此类将会支持验证框架。
 *
 * @author dengtao
 * @version 1.0.0
 * @since 2021/6/22 17:10
 */
public interface IValidatable {

	/**
	 * 此类需要检验什么值 支持length长度检验。也可以看情况实现支持类似于email，正则等等校验
	 *
	 * @return 需要验证的值
	 */
	Object value();
}
