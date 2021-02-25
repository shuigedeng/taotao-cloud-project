package com.taotao.cloud.standalone.system.modules.security.social.weixin.connect;

import com.taotao.cloud.standalone.system.modules.security.social.weixin.api.WeiXinImpl;
import com.taotao.cloud.standalone.system.modules.security.social.weixin.api.Weixin;
import org.springframework.social.oauth2.AbstractOAuth2ServiceProvider;

/**
 * @Classname WeixinServiceProvider
 * @Description 微信的OAuth2流程处理器的提供器，供spring social的connect体系调用
 * @Author Created by Lihaodong (alias:小东啊) lihaodongmail@163.com
 * @Date 2019-08-23 16:50
 * @Version 1.0
 */
public class WeixinServiceProvider extends AbstractOAuth2ServiceProvider<Weixin> {

    /**
     * 微信获取授权码的url
     */
    private static final String URL_AUTHORIZE = "https://open.weixin.qq.com/connect/qrconnect";
    /**
     * 微信获取accessToken的url
     */
    private static final String URL_ACCESS_TOKEN = "https://api.weixin.qq.com/sns/oauth2/access_token";

    /**
     *
     * @param appId
     * @param appSecret
     */
    public WeixinServiceProvider(String appId, String appSecret) {
        super(new WeixinOAuth2Template(appId, appSecret,URL_AUTHORIZE,URL_ACCESS_TOKEN));
    }

    /**
     *
     * @param accessToken
     * @return
     */
    @Override
    public Weixin getApi(String accessToken) {
        return new WeiXinImpl(accessToken);
    }

}
