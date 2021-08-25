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
package com.taotao.cloud.captcha.properties;

import com.taotao.cloud.captcha.model.CaptchaTypeEnum;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * CaptchaProperties
 *
 * @author shuigedeng
 * @verosion 1.0.0
 * @since 2021/8/24 16:49
 */
@ConfigurationProperties(CaptchaProperties.PREFIX)
public class CaptchaProperties {

	public static final String PREFIX = "taotao.cloud.captcha";

	private boolean enabled = false;

	/**
	 * 验证码类型.
	 */
	private CaptchaTypeEnum type = CaptchaTypeEnum.DEFAULT;

	/**
	 * 滑动拼图底图路径.
	 */
	private String jigsaw = "";

	/**
	 * 点选文字底图路径.
	 */
	private String picClick = "";

	/**
	 * 右下角水印文字(我的水印).
	 */
	private String waterMark = "我的水印";

	/**
	 * 右下角水印字体(文泉驿正黑).
	 */
	private String waterFont = "WenQuanZhengHei.ttf";

	/**
	 * 点选文字验证码的文字字体(文泉驿正黑).
	 */
	private String fontType = "WenQuanZhengHei.ttf";

	/**
	 * 校验滑动拼图允许误差偏移量(默认5像素).
	 */
	private String slipOffset = "5";

	/**
	 * aes加密坐标开启或者禁用(true|false).
	 */
	private Boolean aesStatus = true;

	/**
	 * 滑块干扰项(0/1/2)
	 */
	private String interferenceOptions = "0";

	/**
	 * local缓存的阈值
	 */
	private String cacheNumber = "1000";

	/**
	 * 定时清理过期local缓存(单位秒)
	 */
	private String timingClear = "180";

	/**
	 * 缓存类型redis/local/....
	 */
	private StorageType cacheType = StorageType.redis;

	/**
	 * 历史数据清除开关
	 */
	private boolean historyDataClearEnable = false;

	/**
	 * 一分钟内接口请求次数限制 开关
	 */
	private boolean reqFrequencyLimitEnable = false;

	/***
	 * 一分钟内check接口失败次数
	 */
	private int reqGetLockLimit = 5;
	/**
	 *
	 */
	private int reqGetLockSeconds = 300;

	/***
	 * get接口一分钟内限制访问数
	 */
	private int reqGetMinuteLimit = 100;
	private int reqCheckMinuteLimit = 100;
	private int reqVerifyMinuteLimit = 100;

	public boolean isHistoryDataClearEnable() {
		return historyDataClearEnable;
	}

	public void setHistoryDataClearEnable(boolean historyDataClearEnable) {
		this.historyDataClearEnable = historyDataClearEnable;
	}

	public boolean isReqFrequencyLimitEnable() {
		return reqFrequencyLimitEnable;
	}

	public boolean getReqFrequencyLimitEnable() {
		return reqFrequencyLimitEnable;
	}

	public void setReqFrequencyLimitEnable(boolean reqFrequencyLimitEnable) {
		this.reqFrequencyLimitEnable = reqFrequencyLimitEnable;
	}

	public int getReqGetLockLimit() {
		return reqGetLockLimit;
	}

	public void setReqGetLockLimit(int reqGetLockLimit) {
		this.reqGetLockLimit = reqGetLockLimit;
	}

	public int getReqGetLockSeconds() {
		return reqGetLockSeconds;
	}

	public void setReqGetLockSeconds(int reqGetLockSeconds) {
		this.reqGetLockSeconds = reqGetLockSeconds;
	}

	public int getReqGetMinuteLimit() {
		return reqGetMinuteLimit;
	}

	public void setReqGetMinuteLimit(int reqGetMinuteLimit) {
		this.reqGetMinuteLimit = reqGetMinuteLimit;
	}

	public int getReqCheckMinuteLimit() {
		return reqGetMinuteLimit;
	}

	public void setReqCheckMinuteLimit(int reqCheckMinuteLimit) {
		this.reqCheckMinuteLimit = reqCheckMinuteLimit;
	}

	public int getReqVerifyMinuteLimit() {
		return reqVerifyMinuteLimit;
	}

	public void setReqVerifyMinuteLimit(int reqVerifyMinuteLimit) {
		this.reqVerifyMinuteLimit = reqVerifyMinuteLimit;
	}

	public enum StorageType {
		/**
		 * 内存.
		 */
		local,
		/**
		 * redis.
		 */
		redis,
		/**
		 * 其他.
		 */
		other,
	}

	public static String getPREFIX() {
		return PREFIX;
	}

	public CaptchaTypeEnum getType() {
		return type;
	}

	public void setType(CaptchaTypeEnum type) {
		this.type = type;
	}

	public String getJigsaw() {
		return jigsaw;
	}

	public void setJigsaw(String jigsaw) {
		this.jigsaw = jigsaw;
	}

	public String getPicClick() {
		return picClick;
	}

	public void setPicClick(String picClick) {
		this.picClick = picClick;
	}

	public String getWaterMark() {
		return waterMark;
	}

	public void setWaterMark(String waterMark) {
		this.waterMark = waterMark;
	}

	public String getWaterFont() {
		return waterFont;
	}

	public void setWaterFont(String waterFont) {
		this.waterFont = waterFont;
	}

	public String getFontType() {
		return fontType;
	}

	public void setFontType(String fontType) {
		this.fontType = fontType;
	}

	public String getSlipOffset() {
		return slipOffset;
	}

	public void setSlipOffset(String slipOffset) {
		this.slipOffset = slipOffset;
	}

	public Boolean getAesStatus() {
		return aesStatus;
	}

	public void setAesStatus(Boolean aesStatus) {
		this.aesStatus = aesStatus;
	}

	public StorageType getCacheType() {
		return cacheType;
	}

	public void setCacheType(StorageType cacheType) {
		this.cacheType = cacheType;
	}

	public String getInterferenceOptions() {
		return interferenceOptions;
	}

	public void setInterferenceOptions(String interferenceOptions) {
		this.interferenceOptions = interferenceOptions;
	}

	public String getCacheNumber() {
		return cacheNumber;
	}

	public void setCacheNumber(String cacheNumber) {
		this.cacheNumber = cacheNumber;
	}

	public String getTimingClear() {
		return timingClear;
	}

	public void setTimingClear(String timingClear) {
		this.timingClear = timingClear;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
}
