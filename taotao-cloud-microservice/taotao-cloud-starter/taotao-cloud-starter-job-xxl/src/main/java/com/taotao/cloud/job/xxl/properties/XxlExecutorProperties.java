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
package com.taotao.cloud.job.xxl.properties;


/**
 * XxlExecutorProperties
 *
 * @author shuigedeng
 * @version 1.0.0
 * @since 2021/8/30 20:30
 */
public class XxlExecutorProperties {

	/**
	 * 执行器AppName [选填]：执行器心跳注册分组依据；为空则关闭自动注册
	 */
	private String appname;

	/**
	 * 服务注册地址,优先使用该配置作为注册地址 为空时使用内嵌服务 ”IP:PORT“ 作为注册地址 从而更灵活的支持容器类型执行器动态IP和动态映射端口问题
	 */
	private String address;

	/**
	 * 执行器IP [选填]：默认为空表示自动获取IP，多网卡时可手动设置指定IP ，该IP不会绑定Host仅作为通讯实用；地址信息用于 "执行器注册" 和 "调度中心请求并触发任务"
	 */
	private String ip;

	/**
	 * 执行器端口号 [选填]：小于等于0则自动获取；默认端口为9999，单机部署多个执行器时，注意要配置不同执行器端口；
	 */
	private Integer port = 0;

	/**
	 * 执行器通讯TOKEN [选填]：非空时启用；
	 */
	private String accessToken;

	/**
	 * 执行器运行日志文件存储磁盘路径 [选填] ：需要对该路径拥有读写权限；为空则使用默认路径；
	 */
	private String logPath = "logs/applogs/xxl-job/jobhandler";

	/**
	 * 执行器日志保存天数 [选填] ：值大于3时生效，启用执行器Log文件定期清理功能，否则不生效；
	 */
	private Integer logRetentionDays = 30;

	public String getAppname() {
		return appname;
	}

	public void setAppname(String appname) {
		this.appname = appname;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public Integer getPort() {
		return port;
	}

	public void setPort(Integer port) {
		this.port = port;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public String getLogPath() {
		return logPath;
	}

	public void setLogPath(String logPath) {
		this.logPath = logPath;
	}

	public Integer getLogRetentionDays() {
		return logRetentionDays;
	}

	public void setLogRetentionDays(Integer logRetentionDays) {
		this.logRetentionDays = logRetentionDays;
	}
}
