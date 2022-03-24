package com.taotao.cloud.portal.util;

/**
 * 常量类
 */
public interface Constants {
    /**
     * 响应code
     */
    String HTTP_RES_CODE_NAME = "code";
    /**
     * 响应msg
     */
    String HTTP_RES_CODE_MSG = "message";
    /**
     * 响应data
     */
    String HTTP_RES_CODE_DATA = "data";
    /**
     * 响应请求成功
     */
    String HTTP_RES_CODE_200_VALUE = "success";
    /**
     * 系统错误
     */
    String HTTP_RES_CODE_500_VALUE = "fail";
    /**
     * 响应请求成功code
     */
    Integer HTTP_RES_CODE_200 = 200;
    /**
     * 系统错误
     */
    Integer HTTP_RES_CODE_500 = 500;
    Integer HTTP_RES_CODE_201 = 201;

    String SMS_MAIL = "sms_email";

    String MEMBER_TOKEN = "member-token";

    /**
     * cookie 会员 totoken 名称
     */
    String COOKIE_MEMBER_TOKEN = "cookie_member_token";

    int COOKIE_TOKEN_MEMBER_TIME = (60 * 60 * 24 * 90);
}
