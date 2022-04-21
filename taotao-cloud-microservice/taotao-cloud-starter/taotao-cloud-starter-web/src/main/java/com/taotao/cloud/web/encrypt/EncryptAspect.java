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
package com.taotao.cloud.web.encrypt;

import static com.taotao.cloud.web.encrypt.EncryptConstant.DECRYPT;
import static com.taotao.cloud.web.encrypt.EncryptConstant.ENCRYPT;

import java.lang.reflect.Field;
import java.util.Objects;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.jasypt.encryption.StringEncryptor;

/**
 * EncryptAspect
 * <p>
 * {@code
 *
 * @author shuigedeng
 * @version 2021.9
 * @EncryptMethod
 * @PostMapping(value = "test")
 * @ResponseBody public Object testEncrypt(@RequestBody UserVo user, @EncryptField String name) {
 * <p>
 * return insertUser(user, name); }
 * <p>
 * private UserVo insertUser(UserVo user, String name) { System.out.println("加密后的数据：user" +
 * JSON.toJSONString(user)); return user; }
 * @Data public class UserVo implements Serializable {
 * <p>
 * private Long userId;
 * @EncryptField private String mobile;
 * @EncryptField private String address;
 * <p>
 * private String age; } }
 * @since 2021-09-02 22:01:53
 */
@Aspect
public class EncryptAspect {

	/**
	 * stringEncryptor
	 */
	private final StringEncryptor stringEncryptor;

	public EncryptAspect(StringEncryptor stringEncryptor) {
		this.stringEncryptor = stringEncryptor;
	}

	@Pointcut("@annotation(com.taotao.cloud.web.encrypt.EncryptMethod)")
	public void pointCut() {
	}

	@Around("pointCut()")
	public Object around(ProceedingJoinPoint joinPoint) {
		encrypt(joinPoint);

		return decrypt(joinPoint);
	}

	/**
	 * 加密
	 *
	 * @param joinPoint joinPoint
	 * @since 2021-09-02 22:03:39
	 */
	public void encrypt(ProceedingJoinPoint joinPoint) {
		try {
			Object[] objects = joinPoint.getArgs();
			if (objects.length != 0) {
				for (Object o : objects) {
					if (o instanceof String) {
						encryptValue(o);
					} else {
						handler(o, ENCRYPT);
					}
					//TODO 其余类型自己看实际情况加
				}
			}
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 解密
	 *
	 * @param joinPoint joinPoint
	 * @return {@link java.lang.Object }
	 * @since 2021-09-02 22:03:45
	 */
	public Object decrypt(ProceedingJoinPoint joinPoint) {
		Object result = null;
		try {
			Object obj = joinPoint.proceed();
			if (obj != null) {
				if (obj instanceof String) {
					result = decryptValue(obj);
				} else {
					result = handler(obj, DECRYPT);
				}
				//TODO 其余类型自己看实际情况加
			}
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * handler
	 *
	 * @param obj  obj
	 * @param type type
	 * @return {@link java.lang.Object }
	 * @since 2021-09-02 22:03:53
	 */
	private Object handler(Object obj, String type) throws IllegalAccessException {
		if (Objects.isNull(obj)) {
			return null;
		}
		Field[] fields = obj.getClass().getDeclaredFields();
		for (Field field : fields) {
			boolean hasSecureField = field.isAnnotationPresent(EncryptField.class);
			if (hasSecureField) {
				field.setAccessible(true);
				String realValue = (String) field.get(obj);
				String value;
				if (DECRYPT.equals(type)) {
					value = stringEncryptor.decrypt(realValue);
				} else {
					value = stringEncryptor.encrypt(realValue);
				}
				field.set(obj, value);
			}
		}
		return obj;
	}

	/**
	 * encryptValue
	 *
	 * @param realValue realValue
	 * @return {@link java.lang.String }
	 * @since 2021-09-02 22:03:57
	 */
	public String encryptValue(Object realValue) {
		String value = null;
		try {
			value = stringEncryptor.encrypt(String.valueOf(realValue));
		} catch (Exception ex) {
			return value;
		}
		return value;
	}

	/**
	 * decryptValue
	 *
	 * @param realValue realValue
	 * @return {@link java.lang.String }
	 * @since 2021-09-02 22:04:05
	 */
	public String decryptValue(Object realValue) {
		String value = String.valueOf(realValue);
		try {
			value = stringEncryptor.decrypt(value);
		} catch (Exception ex) {
			return value;
		}
		return value;
	}
}
