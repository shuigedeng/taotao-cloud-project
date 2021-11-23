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
package com.taotao.cloud.logger.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

/**
 * 日志
 *
 * @author shuigedeng
 * @version 1.0.0
 * @since 2020/6/15 11:00
 */
public class RequestLogger implements Serializable {

	@Serial
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

	/**
	 * 返回值
	 */
	@JsonProperty(value = "result")
	private String result;

	/**
	 * 记录时间
	 */
	@JsonProperty(value = "logday")
	private String logday;

	public RequestLogger(){}


	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getTraceId() {
		return traceId;
	}

	public void setTraceId(String traceId) {
		this.traceId = traceId;
	}

	public String getApplicationName() {
		return applicationName;
	}

	public void setApplicationName(String applicationName) {
		this.applicationName = applicationName;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getRequestIp() {
		return requestIp;
	}

	public void setRequestIp(String requestIp) {
		this.requestIp = requestIp;
	}

	public Integer getOperateType() {
		return operateType;
	}

	public void setOperateType(Integer operateType) {
		this.operateType = operateType;
	}

	public Integer getRequestType() {
		return requestType;
	}

	public void setRequestType(Integer requestType) {
		this.requestType = requestType;
	}

	public String getRequestMethodName() {
		return requestMethodName;
	}

	public void setRequestMethodName(String requestMethodName) {
		this.requestMethodName = requestMethodName;
	}

	public String getRequestMethod() {
		return requestMethod;
	}

	public void setRequestMethod(String requestMethod) {
		this.requestMethod = requestMethod;
	}

	public String getRequestUrl() {
		return requestUrl;
	}

	public void setRequestUrl(String requestUrl) {
		this.requestUrl = requestUrl;
	}

	public String getRequestArgs() {
		return requestArgs;
	}

	public void setRequestArgs(String requestArgs) {
		this.requestArgs = requestArgs;
	}

	public String getRequestParams() {
		return requestParams;
	}

	public void setRequestParams(String requestParams) {
		this.requestParams = requestParams;
	}

	public String getRequestHeaders() {
		return requestHeaders;
	}

	public void setRequestHeaders(String requestHeaders) {
		this.requestHeaders = requestHeaders;
	}

	public String getRequestUa() {
		return requestUa;
	}

	public void setRequestUa(String requestUa) {
		this.requestUa = requestUa;
	}

	public String getClasspath() {
		return classpath;
	}

	public void setClasspath(String classpath) {
		this.classpath = classpath;
	}

	public Long getRequestStartTime() {
		return requestStartTime;
	}

	public void setRequestStartTime(Long requestStartTime) {
		this.requestStartTime = requestStartTime;
	}

	public Long getRequestEndTime() {
		return requestEndTime;
	}

	public void setRequestEndTime(Long requestEndTime) {
		this.requestEndTime = requestEndTime;
	}

	public Long getRequestConsumingTime() {
		return requestConsumingTime;
	}

	public void setRequestConsumingTime(Long requestConsumingTime) {
		this.requestConsumingTime = requestConsumingTime;
	}

	public String getExDetail() {
		return exDetail;
	}

	public void setExDetail(String exDetail) {
		this.exDetail = exDetail;
	}

	public String getExDesc() {
		return exDesc;
	}

	public void setExDesc(String exDesc) {
		this.exDesc = exDesc;
	}

	public String getTenantId() {
		return tenantId;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getCtime() {
		return ctime;
	}

	public void setCtime(String ctime) {
		this.ctime = ctime;
	}

	public String getLogday() {
		return logday;
	}

	public void setLogday(String logday) {
		this.logday = logday;
	}

}
