/*
 * Copyright ©2015-2021 Jaemon. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.taotao.cloud.dingtalk.core;

import static com.taotao.cloud.dingtalk.constant.DingerConstant.SPOT_SEPERATOR;
import static com.taotao.cloud.dingtalk.enums.ExceptionEnum.IMAGETEXT_METHOD_PARAM_EXCEPTION;
import static com.taotao.cloud.dingtalk.enums.ExceptionEnum.LINK_METHOD_PARAM_EXCEPTION;
import static com.taotao.cloud.dingtalk.utils.DingerUtils.methodParamsGenericType;
import static com.taotao.cloud.dingtalk.utils.DingerUtils.methodParamsType;

import com.taotao.cloud.common.utils.LogUtil;
import com.taotao.cloud.dingtalk.annatations.DingerImageText;
import com.taotao.cloud.dingtalk.annatations.DingerLink;
import com.taotao.cloud.dingtalk.annatations.DingerMarkdown;
import com.taotao.cloud.dingtalk.annatations.DingerText;
import com.taotao.cloud.dingtalk.entity.DingerMethod;
import com.taotao.cloud.dingtalk.enums.MessageMainType;
import com.taotao.cloud.dingtalk.enums.MessageSubType;
import com.taotao.cloud.dingtalk.exception.DingerException;
import java.lang.reflect.Method;
import java.util.List;


/**
 * dinger定义-注解方式
 *
 * @author Jaemon
 * @since 1.2
 */
public class AnnotationDingerDefinitionResolver extends
	AbstractDingerDefinitionResolver<List<Class<?>>> {

	@Override
	public void resolver(List<Class<?>> dingerClasses) {
		for (Class<?> dingerClass : dingerClasses) {
			// dinger 层钉钉机器人配置
			DingerConfig dingerConfiguration = dingerConfiguration(dingerClass);

			String namespace = dingerClass.getName();
			Method[] methods = dingerClass.getMethods();
			for (Method method : methods) {
				String dingerName = namespace + SPOT_SEPERATOR + method.getName();
				String dingerDefinitionKey = MessageMainType.ANNOTATION + SPOT_SEPERATOR;

				Object source;
				MessageSubType messageSubType;
				int[] paramTypes = null;
				if (method.isAnnotationPresent(DingerText.class)) {
					source = method.getAnnotation(DingerText.class);
					messageSubType = MessageSubType.TEXT;
				} else if (method.isAnnotationPresent(DingerMarkdown.class)) {
					source = method.getAnnotation(DingerMarkdown.class);
					messageSubType = MessageSubType.MARKDOWN;
				} else if (method.isAnnotationPresent(DingerImageText.class)) {
					paramTypes = methodParamsGenericType(method, DingerImageText.clazz);
					if (paramTypes.length != 1) {
						throw new DingerException(IMAGETEXT_METHOD_PARAM_EXCEPTION, dingerName);
					}
					source = method.getAnnotation(DingerImageText.class);
					messageSubType = MessageSubType.IMAGETEXT;
				} else if (method.isAnnotationPresent(DingerLink.class)) {
					paramTypes = methodParamsType(method, DingerLink.clazz);
					if (paramTypes.length != 1) {
						throw new DingerException(LINK_METHOD_PARAM_EXCEPTION, dingerName);
					}
					source = method.getAnnotation(DingerLink.class);
					messageSubType = MessageSubType.LINK;
				} else {
					LogUtil.debug(
						"register annotation dingerDefinition and skip method={}(possible use xml definition).",
						dingerName);
					continue;
				}

				registerDingerDefinition(
					dingerName, source,
					dingerDefinitionKey + messageSubType,
					dingerConfiguration,
					new DingerMethod(dingerName, parameterNameDiscoverer.getParameterNames(method),
						paramTypes)
				);
			}

		}
	}
}
