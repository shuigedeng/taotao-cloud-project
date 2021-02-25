package com.taotao.cloud.standalone.system.modules.security.social.qq.api;

/**
 * 个性化api接口类，定义QQ获取用户信息的api接口
 */
public interface QQ {

    /**
     * 根据accessToken、appId、openid发请求，获取用户信息
     */
    QQUserInfo getUserInfo();


}
