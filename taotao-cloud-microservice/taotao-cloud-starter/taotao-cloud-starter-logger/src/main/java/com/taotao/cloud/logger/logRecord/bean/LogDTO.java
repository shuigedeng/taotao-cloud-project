/*
 * Copyright (c) 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
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
package com.taotao.cloud.logger.logRecord.bean;


import java.util.Date;

/**
 * 日志dto
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-26 14:49:17
 */
public class LogDTO {

	/**
	 * 日志id
	 * 日志唯一ID
	 */
	private String logId;
	/**
	 * 商业标识
	 * 业务ID
	 * 支持SpEL
	 */
	private String bizId;
	/**
	 * 商业类型
	 * 业务类型
	 */
	private String bizType;
	/**
	 * 异常
	 * 方法异常信息
	 */
	private String exception;
	/**
	 * 操作日期
	 * 日志操作时间
	 */
	private Date operateDate;
	/**
	 * 成功
	 * 方法是否成功
	 */
	private Boolean success;
	/**
	 * 味精
	 * 日志内容
	 * 支持SpEL
	 */
	private String msg;
	/**
	 * 标签
	 * 日志标签
	 */
	private String tag;
	/**
	 * 返回str
	 * 方法结果（JSON）
	 */
	private String returnStr;
	/**
	 * 执行时间
	 * 方法执行时间
	 */
	private Long executionTime;
	/**
	 * 额外
	 * 额外信息
	 * 支持SpEL
	 */
	private String extra;
	/**
	 * 操作符id
	 * 操作人ID
	 */
	private String operatorId;

	/**
	 * 获取日志id
	 *
	 * @return {@link String }
	 * @since 2022-04-26 14:49:17
	 */
	public String getLogId() {
		return logId;
	}

	/**
	 * 设置日志id
	 *
	 * @param logId 日志id
	 * @since 2022-04-26 14:49:17
	 */
	public void setLogId(String logId) {
		this.logId = logId;
	}

	/**
	 * 获得商业标识
	 *
	 * @return {@link String }
	 * @since 2022-04-26 14:49:17
	 */
	public String getBizId() {
		return bizId;
	}

	/**
	 * 设置商务id
	 *
	 * @param bizId 商业标识
	 * @since 2022-04-26 14:49:18
	 */
	public void setBizId(String bizId) {
		this.bizId = bizId;
	}

	/**
	 * 获取商业类型
	 *
	 * @return {@link String }
	 * @since 2022-04-26 14:49:18
	 */
	public String getBizType() {
		return bizType;
	}

	/**
	 * 设置商业类型
	 *
	 * @param bizType 商业类型
	 * @since 2022-04-26 14:49:18
	 */
	public void setBizType(String bizType) {
		this.bizType = bizType;
	}

	/**
	 * 有异常
	 *
	 * @return {@link String }
	 * @since 2022-04-26 14:49:18
	 */
	public String getException() {
		return exception;
	}

	/**
	 * 设置异常
	 *
	 * @param exception 异常
	 * @since 2022-04-26 14:49:18
	 */
	public void setException(String exception) {
		this.exception = exception;
	}

	/**
	 * 得到操作日期
	 *
	 * @return {@link Date }
	 * @since 2022-04-26 14:49:18
	 */
	public Date getOperateDate() {
		return operateDate;
	}

	/**
	 * 设置操作日期
	 *
	 * @param operateDate 操作日期
	 * @since 2022-04-26 14:49:18
	 */
	public void setOperateDate(Date operateDate) {
		this.operateDate = operateDate;
	}

	/**
	 * 获得成功
	 *
	 * @return {@link Boolean }
	 * @since 2022-04-26 14:49:18
	 */
	public Boolean getSuccess() {
		return success;
	}

	/**
	 * 设置成功
	 *
	 * @param success 成功
	 * @since 2022-04-26 14:49:19
	 */
	public void setSuccess(Boolean success) {
		this.success = success;
	}

	/**
	 * 得到味精
	 *
	 * @return {@link String }
	 * @since 2022-04-26 14:49:19
	 */
	public String getMsg() {
		return msg;
	}

	/**
	 * 设置味精
	 *
	 * @param msg 味精
	 * @since 2022-04-26 14:49:19
	 */
	public void setMsg(String msg) {
		this.msg = msg;
	}

	/**
	 * 得到标签
	 *
	 * @return {@link String }
	 * @since 2022-04-26 14:49:19
	 */
	public String getTag() {
		return tag;
	}

	/**
	 * 设置标签
	 *
	 * @param tag 标签
	 * @since 2022-04-26 14:49:19
	 */
	public void setTag(String tag) {
		this.tag = tag;
	}

	/**
	 * 得到返回str
	 *
	 * @return {@link String }
	 * @since 2022-04-26 14:49:19
	 */
	public String getReturnStr() {
		return returnStr;
	}

	/**
	 * 设置返回str
	 *
	 * @param returnStr 返回str
	 * @since 2022-04-26 14:49:19
	 */
	public void setReturnStr(String returnStr) {
		this.returnStr = returnStr;
	}

	/**
	 * 得到执行时间
	 *
	 * @return {@link Long }
	 * @since 2022-04-26 14:49:19
	 */
	public Long getExecutionTime() {
		return executionTime;
	}

	/**
	 * 设置执行时间
	 *
	 * @param executionTime 执行时间
	 * @since 2022-04-26 14:49:20
	 */
	public void setExecutionTime(Long executionTime) {
		this.executionTime = executionTime;
	}

	/**
	 * 得到额外
	 *
	 * @return {@link String }
	 * @since 2022-04-26 14:49:20
	 */
	public String getExtra() {
		return extra;
	}

	/**
	 * 设置额外
	 *
	 * @param extra 额外
	 * @since 2022-04-26 14:49:20
	 */
	public void setExtra(String extra) {
		this.extra = extra;
	}

	/**
	 * 得到运营商id
	 *
	 * @return {@link String }
	 * @since 2022-04-26 14:49:20
	 */
	public String getOperatorId() {
		return operatorId;
	}

	/**
	 * 集合操作符id
	 *
	 * @param operatorId 操作符id
	 * @since 2022-04-26 14:49:20
	 */
	public void setOperatorId(String operatorId) {
		this.operatorId = operatorId;
	}
}
