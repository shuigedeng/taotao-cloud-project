package com.taotao.cloud.standalone.system.modules.security.properties;


import lombok.Data;

/**
 * @Classname QQProperties
 * @Description QQ第三方登录配置
 * @Author Created by Lihaodong (alias:小东啊) lihaodongmail@163.com
 * @Date 2019-07-08 20:16
 * @Version 1.0
 */
@Data
public class QQProperties extends SocialProperties{

    private String providerId = "qq";
}
