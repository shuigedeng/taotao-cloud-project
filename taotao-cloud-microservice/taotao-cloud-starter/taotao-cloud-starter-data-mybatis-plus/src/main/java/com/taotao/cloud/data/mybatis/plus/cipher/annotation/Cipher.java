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
package com.taotao.cloud.data.mybatis.plus.cipher.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * EnableCipher
 *
 * <pre class="code">
 * &#064;Mapper
 * public interface UserMapper {
 *
 *     &#064;Cipher(EnableCipher.CipherType.ENCRYPT)
 *     int singleInsertAndReturnId(User user);
 *
 *     &#064;Cipher(value  = EnableCipher.CipherType.ENCRYPT,EnableBatch = true)
 *     int singleInsert(@Param(value = "xxx") User user);
 *
 *     &#064;Cipher(EnableCipher.CipherType.DECRYPT)
 *     User findById(long id);
 *
 *     &#064;Cipher(value  = EnableCipher.CipherType.DECRYPT,EnableBatch = true)
 *     List<User> findList();
 *
 *     &#064;EnableCipher(value  = EnableCipher.CipherType.ENCRYPT,EnableBatch = true)
 *     void insertList(@Param("parama") ArrayList<User> asList);
 * }
 * </pre>
 *
 * @author shuigedeng
 * @version 2022.07
 * @since 2022-07-12 13:30:25
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface Cipher {
	/**
	 * 加解密模式
	 *
	 * @return {@link CipherType }
	 * @since 2022-07-12 13:30:25
	 */
	CipherType value();

	/**
	 * 批量加密 默认不开启批量操作
	 * ps 如果需要使用批量操作请实现
	 *
	 * @return boolean
	 * @since 2022-07-12 13:30:25
	 */
	boolean EnableBatch() default false;


	/**
	 * 密码类型
	 *
	 * @author shuigedeng
	 * @version 2022.07
	 * @since 2022-07-12 13:30:25
	 */
	enum CipherType {
		/**
		 * 加密
		 */
		ENCRYPT,
		/**
		 * 解密
		 */
		DECRYPT,
	}
}
