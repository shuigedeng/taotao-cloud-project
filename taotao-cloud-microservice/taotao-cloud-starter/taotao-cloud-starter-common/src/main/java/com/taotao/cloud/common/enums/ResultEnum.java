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
package com.taotao.cloud.common.enums;

/**
 * 返回结果枚举 code规则 500 + 自增三位数
 *
 * @author dengtao
 * @version 1.0.0
 * @since 2020/4/29 15:36
 */
public enum ResultEnum implements BaseEnum {
	/**
	 * 请求成功
	 */
	SUCCESS(200, "请求成功"),
	/**
	 * 系统错误
	 */
	ERROR(500, "系统内部错误"),
	/**
	 * 登录成功
	 */
	LOGIN_SUCCESS(200, "登录成功"),
	/**
	 * 退出成功
	 */
	LOGOUT_SUCCESS(200, "退出成功"),
	/**
	 * 请求错误
	 */
	BAD_REQUEST(400, "请求错误"),
	/**
	 * 尚未登录 认证失败
	 */
	UNAUTHORIZED(401, "用户未认证"),
	/**
	 * 权限不足
	 */
	FORBIDDEN(403, "权限不足"),
	/**
	 * 请求不存在
	 */
	REQUEST_NOT_FOUND(404, "请求不存在"),
	/**
	 * 用户名或密码错误
	 */
	USERNAME_OR_PASSWORD_ERROR(500000, "用户名或密码错误"),
	/**
	 * 用户不存在
	 */
	USER_NOT_EXIST(500001, "用户不存在"),
	/**
	 * 用户已被禁用
	 */
	USER_DISABLE(500002, "账号已被禁用"),
	/**
	 * 账号已删除
	 */
	USER_DELETED(500003, "账号已删除"),
	/**
	 * 验证码错误
	 */
	VERIFY_CODE_ERROR(500004, "验证码错误"),
	/**
	 * 验证码异常
	 */
	CODE_ERROR(500007, "验证码异常"),
	/**
	 * 获取验证码的值失败
	 */
	CODE_GET_ERROR(500008, "获取验证码的值失败"),
	/**
	 * 验证码的值不能为空
	 */
	CODE_VALUE_NOT_NULL(500009, "验证码的值不能为空"),
	/**
	 * 验证码不存在
	 */
	CODE_NOT_FOUND(500010, "验证码不存在"),
	/**
	 * 验证码已过期
	 */
	CODE_IS_EXPIRED(500011, "验证码已过期"),
	/**
	 * 验证码不匹配
	 */
	CODE_NOT_MATCH(500012, "验证码不匹配"),
	/**
	 * 验证码发送异常
	 */
	CODE_SEND_ERROR(500013, "验证码发送错误"),
	/**
	 * 参数认证错误
	 */
	ILLEGAL_ARGUMENT_ERROR(500014, "参数认证错误"),
	/**
	 * 参数校验错误
	 */
	VERIFY_ARGUMENT_ERROR(500015, "参数校验错误"),
	/**
	 * 消息发送错误
	 */
	MESSAGE_SEND_ERROR(500015, "消息发送错误"),
	/**
	 * 不支持当前请求方法
	 */
	METHOD_NOT_SUPPORTED_ERROR(500016, "不支持当前请求方法"),
	/**
	 * 不支持当前媒体类型
	 */
	MEDIA_TYPE_NOT_SUPPORTED_ERROR(500017, "不支持当前媒体类型"),
	/**
	 * sql错误
	 */
	SQL_ERROR(500018, "sql错误"),
	/**
	 * 字典code已存在
	 */
	DICT_CODE_REPEAT_ERROR(500019, "字典code已存在"),
	/**
	 * 用户手机已存在
	 */
	USER_PHONE_EXISTS_ERROR(500020, "用户手机已存在"),
	/**
	 * 用户手机不一致
	 */
	USER_PHONE_INCONSISTENT_ERROR(500021, "用户手机不一致"),
	/**
	 * 字典数据不存在
	 */
	DICT_NOT_EXIST(500022, "字典数据不存在"),
	/**
	 * 会员昵称已存在
	 */
	MEMBER_NICKNAME_EXIST(500023, "昵称已存在"),
	/**
	 * 会员手机已存在
	 */
	MEMBER_PHONE_EXIST(500023, "手机已存在"),
	/**
	 * 用户认证失败
	 */
	USER_UNAUTHORIZED_ERROR(500024, "用户认证失败"),
	/**
	 * 角色不存在
	 */
	ROLE_NOT_EXIST(500025, "角色不存在"),
	/**
	 * 资源不存在
	 */
	RESOURCE_NOT_EXIST(500026, "资源不存在"),
	/**
	 * 商品不存在
	 */
	PRODUCT_NOT_EXIST(500027, "商品不存在"),
	/**
	 * 资源名称已存在
	 */
	RESOURCE_NAME_EXISTS_ERROR(500028, "资源名称已存在"),
	/**
	 * 文件不存在
	 */
	FILE_NOT_EXIST(500029, "文件不存在"),
	/**
	 * 会员登录日志不存在
	 */
	MEMBER_LOGIN_NOT_EXIST(500030, "会员登录日志不存在"),
	/**
	 * 物流公司不存在
	 */
	EXPRESS_COMPANY_NOT_EXIST(500031, "物流公司不存在"),
	/**
	 * 邮件信息不存在
	 */
	EMAIL_NOT_EXIST(500032, "邮件信息不存在"),
	/**
	 * 支付信息不存在
	 */
	PAY_FLOW_NOT_EXIST(500033, "支付信息不存在"),
	/**
	 * 会员用户信息不存在
	 */
	MEMBER_NOT_EXIST(500034, "会员用户信息不存在"),
	/**
	 * 参数类型不匹配
	 */
	METHOD_ARGUMENTS_TYPE_MISMATCH(500035, "参数类型不匹配,传入参数格式不正确或参数解析异常"),
	/**
	 * 缺少参数
	 */
	MISSING_SERVLET_REQUEST_PARAMETER(500036, "缺少参数"),
	/**
	 * 请求method不匹配
	 */
	HTTP_REQUEST_METHOD_NOT_SUPPORTED(500037, "请求method不匹配"),
	/**
	 * RequestBody类型参数数据类型转换异常  HttpMessageNotReadable
	 */
	HTTP_MESSAGE_NOT_READABLE(500038, "类型参数数据类型转换异常");

	/**
	 * 返回码
	 */
	private final int code;

	/**
	 * 描述
	 */
	private final String data;

	ResultEnum(Integer code, String data) {
		this.code = code;
		this.data = data;
	}

	/**
	 * 根据返回码得到描述信息
	 *
	 * @param code code
	 * @return java.lang.String
	 * @author dengtao
	 * @since 2021/2/25 15:48
	 */
	public static String getMessageByCode(int code) {
		for (ResultEnum result : ResultEnum.values()) {
			if (result.getCode() == code) {
				return result.getData();
			}
		}
		return null;
	}

	@Override
	public String getNameByCode(int code) {
		for (ResultEnum result : ResultEnum.values()) {
			if (result.getCode() == code) {
				return result.name().toLowerCase();
			}
		}
		return null;
	}

	@Override
	public Integer getCode() {
		return code;
	}

	public String getData() {
		return data;
	}

}
