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
package com.taotao.cloud.data.mybatis.plus.cipher.service;

import java.util.List;
import java.util.Map;

/**
 * 加解密服务接口
 *
 * @author shuigedeng
 * @version 2022.07
 * @since 2022-07-12 13:30:07
 */
public interface CryptService {
	/**
	 * 单条加密
	 *
	 * @param value 待加密字段
	 * @return {@link String }
	 * @since 2022-07-12 13:30:07
	 */
	String encrypt(String value);

	/**
	 * 单条解密
	 *
	 * @param value 待解密字段
	 * @return {@link String }
	 * @since 2022-07-12 13:30:07
	 */
	String decrypt(String value);

	/**
	 * 批量解密
	 *
	 * @param ori 待解密密文集合
	 * @return {@link Map }<{@link String }, {@link String }>
	 * @since 2022-07-12 13:30:07
	 */
	Map<String, String> batchDecrypt(List<String> ori);

	/**
	 * 批量加密
	 *
	 * @param ori 待解密密文集合
	 * @return {@link Map }<{@link String }, {@link String }>
	 * @since 2022-07-12 13:30:07
	 */
	Map<String, String> batchEncrypt(List<String> ori);

}
