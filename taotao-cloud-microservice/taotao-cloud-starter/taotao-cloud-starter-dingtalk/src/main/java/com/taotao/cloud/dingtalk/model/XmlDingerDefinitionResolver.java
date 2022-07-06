/*
 * Copyright (c) ©2015-2021 Jaemon. All Rights Reserved.
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
package com.taotao.cloud.dingtalk.model;

import static com.taotao.cloud.dingtalk.constant.DingerConstant.SPOT_SEPERATOR;
import static com.taotao.cloud.dingtalk.enums.ExceptionEnum.DINER_XML_MSGTYPE_INVALID;
import static com.taotao.cloud.dingtalk.enums.ExceptionEnum.DINER_XML_NAMESPACE_INVALID;
import static com.taotao.cloud.dingtalk.enums.ExceptionEnum.IMAGETEXT_METHOD_PARAM_EXCEPTION;
import static com.taotao.cloud.dingtalk.enums.ExceptionEnum.LINK_METHOD_PARAM_EXCEPTION;
import static com.taotao.cloud.dingtalk.enums.ExceptionEnum.RESOURCE_CONFIG_EXCEPTION;
import static com.taotao.cloud.dingtalk.utils.DingerUtils.methodParamsGenericType;
import static com.taotao.cloud.dingtalk.utils.DingerUtils.methodParamsType;

import com.taotao.cloud.common.utils.log.LogUtil;
import com.taotao.cloud.dingtalk.annatations.DingerImageText;
import com.taotao.cloud.dingtalk.annatations.DingerLink;
import com.taotao.cloud.dingtalk.entity.DingerMethod;
import com.taotao.cloud.dingtalk.enums.MessageMainType;
import com.taotao.cloud.dingtalk.enums.MessageSubType;
import com.taotao.cloud.dingtalk.exception.DingerException;
import com.taotao.cloud.dingtalk.utils.XmlUtils;
import com.taotao.cloud.dingtalk.xml.BeanTag;
import com.taotao.cloud.dingtalk.xml.MessageTag;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.core.io.Resource;
import org.springframework.util.FileCopyUtils;


/**
 * dinger定义-xml方式
 *
 * @author shuigedeng
 * @version 2022.07
 * @since 2022-07-06 15:23:37
 */
public class XmlDingerDefinitionResolver extends AbstractDingerDefinitionResolver<Resource[]> {

	@Override
	public void resolver(Resource[] resources) {
		for (Resource resource : resources) {
			if (!resource.isReadable()) {
				LogUtil.debug("Ignored because not readable: {} ", resource.getFilename());
				continue;
			}
			String xml;
			try {
				xml = new String(FileCopyUtils.copyToByteArray(resource.getInputStream()), "UTF-8");
			} catch (IOException e) {
				throw new DingerException(RESOURCE_CONFIG_EXCEPTION, resource.getFilename());
			}
			xml = transferXml(xml);
			BeanTag dingerBean = XmlUtils.xmlToJavaBean(xml, BeanTag.class);
			if (dingerBean == null) {
				LogUtil.debug("dinger xml file: {} content is empty.", resource.getFilename());
				continue;
			}
			String namespace = dingerBean.getNamespace();
			Class<?> dingerClass;
			try {
				dingerClass = Class.forName(namespace);
			} catch (ClassNotFoundException e) {
				throw new DingerException(DINER_XML_NAMESPACE_INVALID, namespace);
			}

			Map<String, DingerMethod> dingerClassMethods = dingerClassMethods(dingerClass);

			DingerConfig dingerConfiguration = dingerConfiguration(dingerClass);

			List<MessageTag> messages = dingerBean.getMessages();
			for (MessageTag message : messages) {
				String methodName = message.getIdentityId();
				if (!dingerClassMethods.containsKey(methodName)) {
					LogUtil.debug("namespace={}, messageId={} undefined in dingerClass.",
						namespace, methodName);
					continue;
				}
				String dingerName = namespace + SPOT_SEPERATOR + methodName;
				String messageSubType = message.getDingerType();
				if (!MessageSubType.contains(messageSubType)) {
					throw new DingerException(DINER_XML_MSGTYPE_INVALID, dingerName,
						messageSubType);
				}
				String dingerDefinitionKey =
					MessageMainType.XML + SPOT_SEPERATOR + message.getDingerType();

				registerDingerDefinition(
					dingerName, message,
					dingerDefinitionKey,
					dingerConfiguration,
					dingerClassMethods.get(methodName)
				);
			}
		}
	}


	/**
	 * 获取当前Dinger接口层方法的所有参数信息
	 *
	 * @param dingerClass Dinger接口层类
	 * @return 当前Dinger接口定义的方法的参数信息和泛型信息
	 */
	protected Map<String, DingerMethod> dingerClassMethods(Class<?> dingerClass) {
		Method[] methods = dingerClass.getMethods();
		Map<String, DingerMethod> dingerMethodParams = new HashMap<>();
		for (Method method : methods) {
			String methodName = method.getName();
			String methodAllName =
				dingerClass.getSimpleName() + SPOT_SEPERATOR + methodName;
			int[] paramTypes = null;
			if (method.isAnnotationPresent(DingerImageText.class)) {
				paramTypes = methodParamsGenericType(method, DingerImageText.clazz);
				if (paramTypes.length != 1) {
					throw new DingerException(IMAGETEXT_METHOD_PARAM_EXCEPTION, methodAllName);
				}
			} else if (method.isAnnotationPresent(DingerLink.class)) {
				paramTypes = methodParamsType(method, DingerLink.clazz);
				if (paramTypes.length != 1) {
					throw new DingerException(LINK_METHOD_PARAM_EXCEPTION, methodAllName);
				}
			}
			String[] methodParams = parameterNameDiscoverer.getParameterNames(method);
			dingerMethodParams.put(methodName,
				new DingerMethod(methodAllName, methodParams, paramTypes));
		}
		return dingerMethodParams;
	}

	String transferXml(String sourceXml) {
		return sourceXml.replaceAll("<!DOCTYPE.*>", "");
	}
}
