package com.taotao.cloud.standalone.security;


import lombok.Getter;

/**
 * @Classname PreAuthenticationSuccessHandler
 * @Description 登录类型 现在有用户名 短信 社交
 * @Author shuigedeng
 * @since 2019-07-08 13:50
 * 
 */
@Getter
public enum LoginType {
	normal, sms, social;
}
