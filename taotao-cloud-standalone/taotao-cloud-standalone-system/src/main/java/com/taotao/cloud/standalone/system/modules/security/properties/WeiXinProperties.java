package com.taotao.cloud.standalone.system.modules.security.properties;

import lombok.Data;

/**
 * @Classname GithubProperties
 * @Description github第三方登录配置
 * @Author shuigedeng
 * @since 2019-07-08 21:49
 * 
 */
@Data
public class WeiXinProperties extends SocialProperties {

    private String providerId = "weixin";
}
