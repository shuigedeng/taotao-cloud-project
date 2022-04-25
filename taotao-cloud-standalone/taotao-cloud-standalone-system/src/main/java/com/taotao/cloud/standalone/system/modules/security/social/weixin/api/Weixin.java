package com.taotao.cloud.standalone.system.modules.security.social.weixin.api;


/**
 * @Classname Weixin
 * @Description 微信API调用接口
 * @Author shuigedeng
 * @since 2019-08-23 16:50
 * 
 */
public interface Weixin {

    WeixinUserInfo getUserInfo(String openId);
}
