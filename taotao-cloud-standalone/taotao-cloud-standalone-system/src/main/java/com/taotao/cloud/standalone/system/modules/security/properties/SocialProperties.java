package com.taotao.cloud.standalone.system.modules.security.properties;

/**
 * @Classname SocialProperties
 * @Description TODO
 * @Author Created by Lihaodong (alias:小东啊) lihaodongmail@163.com
 * @since 2019-08-06 14:50
 * @Version 1.0
 */
public abstract class SocialProperties {
    private String appId;
    private String appSecret;

    public SocialProperties() {
    }

    public String getAppId() {
        return this.appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getAppSecret() {
        return this.appSecret;
    }

    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
    }
}
