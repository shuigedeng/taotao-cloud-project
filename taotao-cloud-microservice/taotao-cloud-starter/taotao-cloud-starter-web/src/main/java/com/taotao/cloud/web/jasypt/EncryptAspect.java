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
package com.taotao.cloud.web.jasypt;

import static com.taotao.cloud.web.jasypt.EncryptConstant.DECRYPT;
import static com.taotao.cloud.web.jasypt.EncryptConstant.ENCRYPT;

import java.lang.reflect.Field;
import java.util.Objects;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.jasypt.encryption.StringEncryptor;

/**
 * EncryptAspect
 *
 * @EncryptMethod
 * @PostMapping(value = "test")
 * @ResponseBody
 * public Object testEncrypt(@RequestBody UserVo user, @EncryptField String name) {
 *
 *     return insertUser(user, name);
 * }
 *
 * private UserVo insertUser(UserVo user, String name) {
 *     System.out.println("加密后的数据：user" + JSON.toJSONString(user));
 *     return user;
 * }
 *
 * @Data
 * public class UserVo implements Serializable {
 *
 *     private Long userId;
 *
 *     @EncryptField
 *     private String mobile;
 *
 *     @EncryptField
 *     private String address;
 *
 *     private String age;
 * }
 *
 * @author shuigedeng
 * @version 1.0.0
 * @since 2021/08/04 08:26
 */
@Aspect
public class EncryptAspect {

	private final StringEncryptor stringEncryptor;

	public EncryptAspect(StringEncryptor stringEncryptor) {
		this.stringEncryptor = stringEncryptor;
	}

	@Pointcut("@annotation(com.taotao.cloud.web.jasypt.EncryptMethod)")
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
	 * @param joinPoint  joinPoint
	 * @author shuigedeng
	 * @since 2021/8/24 23:39
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
	 * @param joinPoint  joinPoint
	 * @return java.lang.Object
	 * @author shuigedeng
	 * @since 2021/8/24 23:39
	 */
	public Object decrypt(ProceedingJoinPoint joinPoint) {
		Object result = null;
		try {
			Object obj = joinPoint.proceed();
			if (obj != null) {
				if (obj instanceof String) {
					decryptValue(obj);
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
	 *  handler
	 *
	 * @param obj obj
	 * @param type  type
	 * @return java.lang.Object
	 * @author shuigedeng
	 * @since 2021/8/24 23:39
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

	public String encryptValue(Object realValue) {
		String value = null;
		try {
			value = stringEncryptor.encrypt(String.valueOf(realValue));
		} catch (Exception ex) {
			return value;
		}
		return value;
	}

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
