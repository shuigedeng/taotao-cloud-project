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
 * @version 2022.03
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
	private String userId;

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
	@JsonProperty(value = "ip")
	private String ip;

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
	@JsonProperty(value = "method_name")
	private String methodName;

	/**
	 * 请求方式
	 */
	@JsonProperty(value = "method")
	private String method;

	/**
	 * 请求url
	 */
	@JsonProperty(value = "url")
	private String url;

	/**
	 * 方法参数
	 */
	@JsonProperty(value = "args")
	private String args;

	/**
	 * 请求参数
	 */
	@JsonProperty(value = "params")
	private String params;

	/**
	 * 请求头
	 */
	@JsonProperty(value = "headers")
	private String headers;

	/**
	 * 类路径
	 */
	@JsonProperty(value = "classpath")
	private String classpath;

	/**
	 * 开始时间
	 */
	@JsonProperty(value = "start_time")
	private Long startTime;

	/**
	 * 完成时间
	 */
	@JsonProperty(value = "end_time")
	private Long endTime;

	/**
	 * 消耗时间
	 */
	@JsonProperty(value = "consuming_time")
	private Long consumingTime;

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

	/**
	 * 操作地点
	 */
	@JsonProperty(value = "location")
	private String location;
	/**
	 * 操作系统
	 */
	@JsonProperty(value = "os")
	private String os;
	/**
	 * 浏览器
	 */
	@JsonProperty(value = "browser")
	private String browser;

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

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
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

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
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

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getArgs() {
		return args;
	}

	public void setArgs(String args) {
		this.args = args;
	}

	public String getParams() {
		return params;
	}

	public void setParams(String params) {
		this.params = params;
	}

	public String getHeaders() {
		return headers;
	}

	public void setHeaders(String headers) {
		this.headers = headers;
	}

	public String getClasspath() {
		return classpath;
	}

	public void setClasspath(String classpath) {
		this.classpath = classpath;
	}

	public Long getStartTime() {
		return startTime;
	}

	public void setStartTime(Long startTime) {
		this.startTime = startTime;
	}

	public Long getEndTime() {
		return endTime;
	}

	public void setEndTime(Long endTime) {
		this.endTime = endTime;
	}

	public Long getConsumingTime() {
		return consumingTime;
	}

	public void setConsumingTime(Long consumingTime) {
		this.consumingTime = consumingTime;
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

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getLogday() {
		return logday;
	}

	public void setLogday(String logday) {
		this.logday = logday;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getOs() {
		return os;
	}

	public void setOs(String os) {
		this.os = os;
	}

	public String getBrowser() {
		return browser;
	}

	public void setBrowser(String browser) {
		this.browser = browser;
	}
}
