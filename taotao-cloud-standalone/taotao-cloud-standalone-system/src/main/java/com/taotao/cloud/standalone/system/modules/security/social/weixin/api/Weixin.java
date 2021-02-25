package com.taotao.cloud.standalone.system.modules.security.social.weixin.api;


/**
 * @Classname Weixin
 * @Description 微信API调用接口
 * @Author Created by Lihaodong (alias:小东啊) lihaodongmail@163.com
 * @Date 2019-08-23 16:50
 * @Version 1.0
 */
public interface Weixin {

    WeixinUserInfo getUserInfo(String openId);
}
