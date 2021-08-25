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
package com.taotao.cloud.captcha.model;


import com.taotao.cloud.captcha.util.StringUtils;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

/**
 * RequestModel
 *
 * @author shuigedeng
 * @version 1.0.0
 * @since 2021/8/24 16:48
 */
public class RequestModel implements Serializable {

	private static final long serialVersionUID = -5800786065305114784L;

	/**
	 * 当前请求接口路径 /business/accessUser/login
	 */
	private String servletPath;

	/**
	 * {"reqData":{"password":"*****","userName":"admin"},"sign":"a304a7f296f565b6d2009797f68180f0","time":"1542456453355","token":""}
	 */
	private String requestString;

	/**
	 * {"password":"****","userName":"admin"}
	 */
	private HashMap reqData;

	private String token;

	private Long userId;

	private String userName;

	private List<Long> projectList;

	//拥有哪些分组
	private List<Long> groupIdList;

	private String target;

	private String sign;

	private String time;

	private String sourceIP;

	/**
	 * 校验自身参数合法性
	 */
	public boolean isVaildateRequest() {
		if (StringUtils.isBlank(sign) || StringUtils.isBlank(time)) {
			return false;
		}
		return true;
	}

	public String getServletPath() {
		return servletPath;
	}

	public void setServletPath(String servletPath) {
		this.servletPath = servletPath;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public List<Long> getProjectList() {
		return projectList;
	}

	public void setProjectList(List<Long> projectList) {
		this.projectList = projectList;
	}

	public List<Long> getGroupIdList() {
		return groupIdList;
	}

	public void setGroupIdList(List<Long> groupIdList) {
		this.groupIdList = groupIdList;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getSourceIP() {
		return sourceIP;
	}

	public void setSourceIP(String sourceIP) {
		this.sourceIP = sourceIP;
	}

	public String getRequestString() {
		return requestString;
	}

	public void setRequestString(String requestString) {
		this.requestString = requestString;
	}

	public HashMap getReqData() {
		return reqData;
	}

	public void setReqData(HashMap reqData) {
		this.reqData = reqData;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}
}
