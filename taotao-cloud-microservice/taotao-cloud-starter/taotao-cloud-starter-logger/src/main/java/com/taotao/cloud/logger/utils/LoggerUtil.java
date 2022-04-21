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
package com.taotao.cloud.logger.utils;

import com.taotao.cloud.logger.annotation.RequestLogger;
import java.lang.reflect.Method;
import org.aspectj.lang.JoinPoint;

/**
 * LogUtil
 *
 * @author shuigedeng
 * @version v1.0
 * @since 2020/4/27 16:16
 */
public class LoggerUtil {

	/**
	 * 获取操作信息
	 *
	 * @param point point
	 * @return java.lang.String
	 * @author shuigedeng
	 * @since 2020/4/30 10:21
	 */
	public static String getControllerMethodDescription(JoinPoint point) throws Exception {
		// 获取连接点目标类名
		String targetName = point.getTarget().getClass().getName();
		// 获取连接点签名的方法名
		String methodName = point.getSignature().getName();
		//获取连接点参数
		Object[] args = point.getArgs();
		//根据连接点类的名字获取指定类
		Class<?> targetClass = Class.forName(targetName);
		//获取类里面的方法
		Method[] methods = targetClass.getMethods();
		String description = "";
		for (Method method : methods) {
			if (method.getName().equals(methodName)) {
				Class<?>[] clazzs = method.getParameterTypes();
				if (clazzs.length == args.length) {
					description = method.getAnnotation(RequestLogger.class).value();
					break;
				}
			}
		}
		return description;
	}
}
