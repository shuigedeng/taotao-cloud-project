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
package com.taotao.cloud.log.model;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.SerializerFeature;
import lombok.Data;

import java.io.Serializable;

/**
 * 日志
 *
 * @author dengtao
 * @since 2020/6/15 11:00
 * @version 1.0.0
 */
@Data
public class RequestLog implements Serializable {

	private static final long serialVersionUID = -749360940290141180L;

	/**
	 * 请求日志id
	 */
	@JSONField(serialzeFeatures = SerializerFeature.WriteMapNullValue, name = "trace_id")
	private String traceId;

	/**
	 * 服务名称
	 */
	@JSONField(serialzeFeatures = SerializerFeature.WriteMapNullValue, name = "application_name")
	private String applicationName;

	/**
	 * 操作人ID
	 */
	@JSONField(serialzeFeatures = SerializerFeature.WriteMapNullValue, name = "username")
	private String username;

	/**
	 * 操作人ID
	 */
	@JSONField(serialzeFeatures = SerializerFeature.WriteMapNullValue, name = "user_id")
	private Long userId;

	/**
	 * 客户端ID
	 */
	@JSONField(serialzeFeatures = SerializerFeature.WriteMapNullValue, name = "client_id")
	private String clientId;

	/**
	 * 操作描述
	 */
	@JSONField(serialzeFeatures = SerializerFeature.WriteMapNullValue, name = "description")
	private String description;

	/**
	 * 操作IP
	 */
	@JSONField(serialzeFeatures = SerializerFeature.WriteMapNullValue, name = "request_ip")
	private String requestIp;

	/**
	 * 操作类型 1 操作记录 2异常记录
	 */
	@JSONField(serialzeFeatures = SerializerFeature.WriteMapNullValue, name = "operate_type")
	private Integer operateType;

	/**
	 * 请求类型（1查询/获取，2添加，3修改，4删除）
	 */
	@JSONField(serialzeFeatures = SerializerFeature.WriteMapNullValue, name = "request_type")
	private Integer requestType;

	/**
	 * 请求方法名称
	 */
	@JSONField(serialzeFeatures = SerializerFeature.WriteMapNullValue, name = "request_method_name")
	private String requestMethodName;

	/**
	 * 请求方式
	 */
	@JSONField(serialzeFeatures = SerializerFeature.WriteMapNullValue, name = "request_method")
	private String requestMethod;

	/**
	 * 请求url
	 */
	@JSONField(serialzeFeatures = SerializerFeature.WriteMapNullValue, name = "request_url")
	private String requestUrl;

	/**
	 * 方法参数
	 */
	@JSONField(serialzeFeatures = SerializerFeature.WriteMapNullValue, name = "request_args")
	private String requestArgs;

	/**
	 * 请求参数
	 */
	@JSONField(serialzeFeatures = SerializerFeature.WriteMapNullValue, name = "request_params")
	private String requestParams;

	/**
	 * 请求头
	 */
	@JSONField(serialzeFeatures = SerializerFeature.WriteMapNullValue, name = "request_headers")
	private String requestHeaders;

	/**
	 * 浏览器
	 */
	@JSONField(serialzeFeatures = SerializerFeature.WriteMapNullValue, name = "request_ua")
	private String requestUa;

	/**
	 * 类路径
	 */
	@JSONField(serialzeFeatures = SerializerFeature.WriteMapNullValue, name = "classpath")
	private String classpath;

	/**
	 * 开始时间
	 */
	@JSONField(serialzeFeatures = SerializerFeature.WriteMapNullValue, name = "request_start_time")
	private Long requestStartTime;

	/**
	 * 完成时间
	 */
	@JSONField(serialzeFeatures = SerializerFeature.WriteMapNullValue, name = "request_end_time")
	private Long requestEndTime;

	/**
	 * 消耗时间
	 */
	@JSONField(serialzeFeatures = SerializerFeature.WriteMapNullValue, name = "request_consuming_time")
	private Long requestConsumingTime;

	/**
	 * 异常详情信息 堆栈信息
	 */
	@JSONField(serialzeFeatures = SerializerFeature.WriteMapNullValue, name = "ex_detail")
	private String exDetail;

	/**
	 * 异常描述 e.getMessage
	 */
	@JSONField(serialzeFeatures = SerializerFeature.WriteMapNullValue, name = "ex_desc")
	private String exDesc;

	/**
	 * 租户id
	 */
	@JSONField(serialzeFeatures = SerializerFeature.WriteMapNullValue, name = "tenant_id")
	private String tenantId;

	/**
	 * 来源
	 */
	@JSONField(serialzeFeatures = SerializerFeature.WriteMapNullValue, name = "source")
	private String source;

	/**
	 * 记录时间
	 */
	@JSONField(serialzeFeatures = SerializerFeature.WriteMapNullValue, name = "ctime")
	private String ctime;
}
