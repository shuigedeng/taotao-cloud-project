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

import java.io.Serializable;
import java.util.List;

/**
 * Captcha
 *
 * @author shuigedeng
 * @version 1.0.0
 * @since 2021/8/24 16:47
 */
public class Captcha implements Serializable {

	/**
	 * 验证码id(后台申请)
	 */
	private String captchaId;
	/**
	 *
	 */
	private String projectCode;

	/**
	 * 验证码类型:(clickWord,blockPuzzle)
	 */
	private String captchaType;

	private String captchaOriginalPath;

	private String captchaFontType;

	private Integer captchaFontSize;

	private String secretKey;

	/**
	 * 原生图片base64
	 */
	private String originalImageBase64;

	/**
	 * 滑块点选坐标
	 */
	private Point point;

	/**
	 * 滑块图片base64
	 */
	private String jigsawImageBase64;

	/**
	 * 点选文字
	 */
	private List<String> wordList;

	/**
	 * 点选坐标
	 */
	private List<java.awt.Point> pointList;

	/**
	 * 点坐标(base64加密传输)
	 */
	private String pointJson;

	/**
	 * UUID(每次请求的验证码唯一标识)
	 */
	private String token;

	/**
	 * 校验结果
	 */
	private Boolean result = false;

	/**
	 * 后台二次校验参数
	 */
	private String captchaVerification;

	/**
	 * 客户端UI组件id,组件初始化时设置一次，UUID
	 */
	private String clientUid;

	/**
	 * 客户端的请求时间，预留字段
	 */
	private Long ts;

	/***
	 * 客户端ip+userAgent
	 */
	private String browserInfo;

	public void resetClientFlag() {
		this.browserInfo = null;
		this.clientUid = null;
	}

	public String getCaptchaId() {
		return captchaId;
	}

	public void setCaptchaId(String captchaId) {
		this.captchaId = captchaId;
	}

	public String getProjectCode() {
		return projectCode;
	}

	public void setProjectCode(String projectCode) {
		this.projectCode = projectCode;
	}

	public String getCaptchaType() {
		return captchaType;
	}

	public void setCaptchaType(String captchaType) {
		this.captchaType = captchaType;
	}

	public String getCaptchaOriginalPath() {
		return captchaOriginalPath;
	}

	public void setCaptchaOriginalPath(String captchaOriginalPath) {
		this.captchaOriginalPath = captchaOriginalPath;
	}

	public String getCaptchaFontType() {
		return captchaFontType;
	}

	public void setCaptchaFontType(String captchaFontType) {
		this.captchaFontType = captchaFontType;
	}

	public Integer getCaptchaFontSize() {
		return captchaFontSize;
	}

	public void setCaptchaFontSize(Integer captchaFontSize) {
		this.captchaFontSize = captchaFontSize;
	}

	public String getOriginalImageBase64() {
		return originalImageBase64;
	}

	public void setOriginalImageBase64(String originalImageBase64) {
		this.originalImageBase64 = originalImageBase64;
	}

	public Point getPoint() {
		return point;
	}

	public void setPoint(Point point) {
		this.point = point;
	}

	public String getJigsawImageBase64() {
		return jigsawImageBase64;
	}

	public void setJigsawImageBase64(String jigsawImageBase64) {
		this.jigsawImageBase64 = jigsawImageBase64;
	}

	public List<String> getWordList() {
		return wordList;
	}

	public void setWordList(List<String> wordList) {
		this.wordList = wordList;
	}

	public List<java.awt.Point> getPointList() {
		return pointList;
	}

	public void setPointList(List<java.awt.Point> pointList) {
		this.pointList = pointList;
	}

	public String getPointJson() {
		return pointJson;
	}

	public void setPointJson(String pointJson) {
		this.pointJson = pointJson;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Boolean getResult() {
		return result;
	}

	public void setResult(Boolean result) {
		this.result = result;
	}

	public String getCaptchaVerification() {
		return captchaVerification;
	}

	public void setCaptchaVerification(String captchaVerification) {
		this.captchaVerification = captchaVerification;
	}

	public String getSecretKey() {
		return secretKey;
	}

	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}

	public String getClientUid() {
		return clientUid;
	}

	public void setClientUid(String clientUid) {
		this.clientUid = clientUid;
	}

	public Long getTs() {
		return ts;
	}

	public void setTs(Long ts) {
		this.ts = ts;
	}

	public String getBrowserInfo() {
		return browserInfo;
	}

	public void setBrowserInfo(String browserInfo) {
		this.browserInfo = browserInfo;
	}
}
