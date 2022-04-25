package com.taotao.cloud.standalone.system.modules.security.properties;


import lombok.Data;

/**
 * @Classname QQProperties
 * @Description QQ第三方登录配置
 * @Author shuigedeng
 * @since 2019-07-08 20:16
 * 
 */
@Data
public class QQProperties extends SocialProperties{

    private String providerId = "qq";
}
