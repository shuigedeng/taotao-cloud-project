package com.taotao.cloud.standalone.system.modules.security.social.qq.api;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.social.oauth2.AbstractOAuth2ApiBinding;
import org.springframework.social.oauth2.TokenStrategy;

/**
 * 个性化api接口实现类，实现QQ获取用户信息的逻辑
 * AbstractOAuth2ApiBinding类非单例，每个用户都创建对象存储自己的accessToken走自己的认证流程
 * 所以不放入ioc中
 */
@Slf4j
public class QQImpl extends AbstractOAuth2ApiBinding implements QQ {

    /**
     * 获取openid的请求地址
     */
    private static final String URL_GET_OPENID = "https://graph.qq.com/oauth2.0/me?access_token=%s";
    /**
     * 获取用户信息的请求地址
     */
    private static final String URL_GET_USERINFO = "https://graph.qq.com/user/get_user_info?oauth_consumer_key=%s&openid=%s";

    /**
     * 申请QQ登录成功后 分配给应用的appid
     */
    private String appid;
    /**
     * 用户的ID，与QQ号码一一对应
     */
    private String openid;

    public QQImpl(String accessToken, String appid) {
        //将token作为查询参数
        super(accessToken, TokenStrategy.ACCESS_TOKEN_PARAMETER);
        this.appid = appid;

        //拼接成最终的openid的请求地址
        String url = String.format(URL_GET_OPENID, accessToken);
        String result = getRestTemplate().getForObject(url, String.class);
        this.openid = StringUtils.substringBetween(result, "\"openid\":\"", "\"}");
    }

    @Override
    public QQUserInfo getUserInfo() {
        //拼接成最终的获取用户信息的请求地址
        String url = String.format(URL_GET_USERINFO, appid, openid);
        String result = getRestTemplate().getForObject(url, String.class);
        QQUserInfo userInfo = null;
        try {
            userInfo = JSON.parseObject(result, QQUserInfo.class);
            userInfo.setOpenId(openid);
            return userInfo;
        } catch (Exception e) {
            throw new RuntimeException("获取用户信息失败", e);
        }
    }
}
