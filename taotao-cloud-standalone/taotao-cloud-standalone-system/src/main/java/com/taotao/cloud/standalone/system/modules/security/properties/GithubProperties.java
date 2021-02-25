package com.taotao.cloud.standalone.system.modules.security.properties;

import lombok.Data;

/**
 * @Classname GithubProperties
 * @Description github第三方登录配置
 * @Author Created by Lihaodong (alias:小东啊) lihaodongmail@163.com
 * @since 2019-07-08 21:49
 * @Version 1.0
 */
@Data
public class GithubProperties extends SocialProperties {

    private String providerId = "github";
}
