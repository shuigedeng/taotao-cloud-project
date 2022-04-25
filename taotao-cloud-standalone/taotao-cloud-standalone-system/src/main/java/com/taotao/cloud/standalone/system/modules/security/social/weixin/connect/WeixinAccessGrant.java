package com.taotao.cloud.standalone.system.modules.security.social.weixin.connect;

import lombok.Getter;
import lombok.Setter;
import org.springframework.social.oauth2.AccessGrant;

/**
 * @Classname WeixinAuthConfig
 * @Description 微信的access_token信息。与标准OAuth2协议不同，微信在获取access_token时会同时返回openId,并没有单独的通过accessToke换取openId的服务
 *              所以在这里继承了标准AccessGrant，添加了openId字段，作为对微信access_token信息的封装。
 * @Author shuigedeng
 * @since 2019-08-23 16:50
 * 
 */
@Setter
@Getter
public class WeixinAccessGrant extends AccessGrant{

    private String openId;

    public WeixinAccessGrant() {
        super("");
    }

    public WeixinAccessGrant(String accessToken, String scope, String refreshToken, Long expiresIn) {
        super(accessToken, scope, refreshToken, expiresIn);
    }
}
