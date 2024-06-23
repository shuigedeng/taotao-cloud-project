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

package com.taotao.cloud.sys.biz.shortlink.src.main.java.com.taotao.cloud.shortlink.biz.shortlink.common.constants;

import com.zc.shortlink.api.common.ErrorCode;

/**
 * ErrorCode 常量池
 *
 * @since 2022/03/30
 */
public class ErrorCodeConstant {

    // 通用错误码
    public static final ErrorCode COMMON_ERROR = new ErrorCode(10000, "服务器开小差啦，稍后重试~");
    public static final ErrorCode PARAMS_MISSING = new ErrorCode(10001, "缺少必要参数");
    public static final ErrorCode TYPE_CUSTOM_ERROR = new ErrorCode(10002, "类型转换错误");
    public static final ErrorCode PARAMETER_ERROR = new ErrorCode(10003, "参数错误");
    public static final ErrorCode PARAM_ILLEGAL = new ErrorCode(10004, "参数不合法");
    public static final ErrorCode SIGN_ILLEGAL = new ErrorCode(10005, "签名校验失败：非法签名");
    public static final ErrorCode SIGN_TIME_OUT = new ErrorCode(10006, "签名校验失败：签名超时");
    public static final ErrorCode LOGIN_FAILED_OVER_TIMES = new ErrorCode(10007, "已达到错误次数上限！");
    public static final ErrorCode ID_CARD_NO_FORMAT_INVALID = new ErrorCode(10008, "身份证格式不合法！");
    public static final ErrorCode VERIFY_CODE_SEND_FAILED = new ErrorCode(10009, "验证码发送失败");
    public static final ErrorCode VERIFY_CODE_NOT_PASS = new ErrorCode(10010, "验证码校验不通过");
    public static final ErrorCode VERIFY_CODE_VERIFY_FAILED = new ErrorCode(10011, "验证码校验失败");
    public static final ErrorCode VERIFY_CODE_TYPE_NOT_SUPPORT = new ErrorCode(10012, "验证码类型不支持");
    public static final ErrorCode EXPIRED_API_NOT_SUPPORT = new ErrorCode(10013, "过期的Api不再支持");
    public static final ErrorCode TOKEN_ERROR = new ErrorCode(100014, "无效token,请重新登录");
    public static final ErrorCode REMOTE_SERVICE_ERR = new ErrorCode(100017, "远程服务调用异常");

    public static final ErrorCode DOMAIN_NOT_FOUND = new ErrorCode(20002, "domain不存在");
    public static final ErrorCode LINK_GROUP_NOT_FOUND = new ErrorCode(20003, "短链分组不存在");
    public static final ErrorCode SHORT_LINK_CODE_GENERATE_ERROR = new ErrorCode(20004, "短链码生成失败");
}
