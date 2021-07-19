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

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import lombok.Data;
/**
 * 日志
 *
 * @author shuigedeng
 * @version 1.0.0
 * @since 2020/6/15 11:00
 */
@Data
public class RequestLog implements Serializable {

	private static final long serialVersionUID = -749360940290141180L;

	/**
	 * 请求日志id
	 */
	@JsonProperty(value = "trace_id")
	private String traceId;

	/**
	 * 服务名称
	 */
	@JsonProperty(value = "application_name")
	private String applicationName;

	/**
	 * 操作人ID
	 */
	@JsonProperty(value = "username")
	private String username;

	/**
	 * 操作人ID
	 */
	@JsonProperty(value = "user_id")
	private Long userId;

	/**
	 * 客户端ID
	 */
	@JsonProperty(value = "client_id")
	private String clientId;

	/**
	 * 操作描述
	 */
	@JsonProperty(value = "description")
	private String description;

	/**
	 * 操作IP
	 */
	@JsonProperty(value = "request_ip")
	private String requestIp;

	/**
	 * 操作类型 1 操作记录 2异常记录
	 */
	@JsonProperty(value = "operate_type")
	private Integer operateType;

	/**
	 * 请求类型（1查询/获取，2添加，3修改，4删除）
	 */
	@JsonProperty(value = "request_type")
	private Integer requestType;

	/**
	 * 请求方法名称
	 */
	@JsonProperty(value = "request_method_name")
	private String requestMethodName;

	/**
	 * 请求方式
	 */
	@JsonProperty(value = "request_method")
	private String requestMethod;

	/**
	 * 请求url
	 */
	@JsonProperty(value = "request_url")
	private String requestUrl;

	/**
	 * 方法参数
	 */
	@JsonProperty(value = "request_args")
	private String requestArgs;

	/**
	 * 请求参数
	 */
	@JsonProperty(value = "request_params")
	private String requestParams;

	/**
	 * 请求头
	 */
	@JsonProperty(value = "request_headers")
	private String requestHeaders;

	/**
	 * 浏览器
	 */
	@JsonProperty(value = "request_ua")
	private String requestUa;

	/**
	 * 类路径
	 */
	@JsonProperty(value = "classpath")
	private String classpath;

	/**
	 * 开始时间
	 */
	@JsonProperty(value = "request_start_time")
	private Long requestStartTime;

	/**
	 * 完成时间
	 */
	@JsonProperty(value = "request_end_time")
	private Long requestEndTime;

	/**
	 * 消耗时间
	 */
	@JsonProperty(value = "request_consuming_time")
	private Long requestConsumingTime;

	/**
	 * 异常详情信息 堆栈信息
	 */
	@JsonProperty(value = "ex_detail")
	private String exDetail;

	/**
	 * 异常描述 e.getMessage
	 */
	@JsonProperty(value = "ex_desc")
	private String exDesc;

	/**
	 * 租户id
	 */
	@JsonProperty(value = "tenant_id")
	private String tenantId;

	/**
	 * 来源
	 */
	@JsonProperty(value = "source")
	private String source;

	/**
	 * 记录时间
	 */
	@JsonProperty(value = "ctime")
	private String ctime;
}
