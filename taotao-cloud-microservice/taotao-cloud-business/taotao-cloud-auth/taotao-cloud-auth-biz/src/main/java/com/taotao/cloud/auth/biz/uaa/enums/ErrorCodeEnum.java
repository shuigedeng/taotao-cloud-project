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

package com.taotao.cloud.auth.biz.uaa.enums;

import org.springframework.http.HttpStatus;

/**
 * 错误代码
 */
@SuppressWarnings("AlibabaEnumConstantsMustHaveComment")
public enum ErrorCodeEnum {
    LOGOUT_SUCCESS(0, "登出成功"),

    NOT_FOUND(404, "not found"),

    UNAUTHORIZED(HttpStatus.UNAUTHORIZED.value(), "操作未授权"),
    INVALID_SESSION(HttpStatus.UNAUTHORIZED.value(), "session 失效"),
    EXPIRED_SESSION(HttpStatus.UNAUTHORIZED.value(), "session 过期"),
    CONCURRENT_SESSION(HttpStatus.UNAUTHORIZED.value(), "你的账号在其他客户端上登录, 此客户端退出登录状态, 如非本人, 请更改密码"),
    SESSION_ENHANCE_CHECK(HttpStatus.UNAUTHORIZED.value(), "session 非法"),
    SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR.value(), "功能还在开发中"),
    INTERNAL_SERVER_ERROR(HttpStatus.NOT_FOUND.value(), "服务器开小差，请重试"),

    USERNAME_USED(900, "用户名已存在"),
    USER_NOT_EXIST(901, "用户不存在"),
    USERNAME_NOT_EMPTY(902, "用户名不能为空"),
    PASSWORD_NOT_EMPTY(903, "密码不能为空"),
    QUERY_USER_INFO_ERROR(904, "未能获取到用户信息，请重试"),
    USER_REGISTER_FAILURE(905, "用户注册失败"),
    GET_REQUEST_PARAMETER_FAILURE(906, "获取注册信息失败"),
    UPDATE_CONNECTION_DATA_FAILURE(940, "更新第三方授权登录用户信息失败"),
    REFRESH_TOKEN_FAILURE(950, "refresh Token 刷新失败"),

    AUTH2_PROVIDER_NOT_SUPPORT(960, "此服务商的第三方授权登录不支持"),
    UN_BINDING_ERROR(961, "解绑第三方账号异常"),
    BINDING_ERROR(962, "绑定第三方账号异常"),
    BOUND_ERROR(963, "此第三方账号已绑定在其他账号上"),

    QUERY_MOBILE_FAILURE_OF_ONE_CLICK_LOGIN(968, "一键登录获取手机号失败"),
    ACCESS_TOKEN_NOT_EMPTY(969, "accessToken 不能为空"),

    USER_REGISTER_OAUTH2_FAILURE(999, "本地用户注册成功, 第三方信息保存失败"),

    VALIDATE_CODE_PARAM_ERROR(600, "验证码参数错误"),
    VALIDATE_CODE_NOT_EMPTY(601, "验证码的值不能为空"),
    VALIDATE_CODE_EXPIRED(602, "验证码已失效, 请刷新"),
    VALIDATE_CODE_ERROR(603, "验证码错误"),
    ILLEGAL_VALIDATE_CODE_TYPE(604, "非法的验证码类型"),
    GET_VALIDATE_CODE_FAILURE(605, "获取验证码失败，请重试"),
    VALIDATE_CODE_FAILURE(606, "验证码校验不通过，请重试"),

    SMS_CODE_PARAMETER_ERROR(610, "短信验证码参数错误"),
    SMS_CODE_ERROR(611, "短信验证码错误"),

    MOBILE_NOT_EMPTY(620, "手机号不能为空"),
    MOBILE_PARAMETER_ERROR(621, "手机号参数错误"),
    MOBILE_FORMAT_ERROR(622, "手机号格式错误，请检查你的手机号码"),

    IMAGE_CODE_ERROR(630, "图片验证码错误"),

    TRACK_CODE_ERROR(640, "轨迹验证码错误"),

    SLIDER_CODE_ERROR(650, "滑块验证码错误"),

    SELECTION_CODE_ERROR(660, "选择验证码错误"),

    CUSTOMIZE_CODE_ERROR(670, "验证码错误"),

    PARAMETER_ERROR(700, "参数错误"),
    BUSINESS_ERROR(701, "业务异常"),

    ADD_PERMISSION_FAILURE(710, "添加权限失败"),
    DEL_PERMISSION_FAILURE(720, "删除权限失败"),
    PERMISSION_DENY(730, "您没有访问权限或未登录"),

    REDIRECT_URL_PARAMETER_ILLEGAL(800, "非法的回调地址"),
    REDIRECT_URL_PARAMETER_ERROR(801, "回调地址不正确"),
    TAMPER_WITH_REDIRECT_URL_PARAMETER(802, "回调参数被篡改"),
    ILLEGAL_ACCESS_URL_ERROR(803, "非法访问");

    /**
     * 错误代码
     */
    private final Integer code;

    /**
     * 错误消息
     */
    private final String msg;

    ErrorCodeEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
