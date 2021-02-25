package com.taotao.cloud.standalone.system.modules.security.social.weixin.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.social.oauth2.AbstractOAuth2ApiBinding;
import org.springframework.social.oauth2.TokenStrategy;

import java.nio.charset.Charset;
import java.util.List;


/**
 * @Classname WeiXinImpl
 * @Description 微信API调用接口
 * @Author Created by Lihaodong (alias:小东啊) lihaodongmail@163.com
 * @since 2019-08-23 16:50
 * @Version 1.0
 */
public class WeiXinImpl extends AbstractOAuth2ApiBinding implements Weixin {

    /**
     * 获取用户信息的url
     */
    private static final String WEIXIN_URL_GET_USER_INFO = "https://api.weixin.qq.com/sns/userinfo?openid=";

    private ObjectMapper objectMapper = new ObjectMapper();

    public WeiXinImpl(String accessToken) {
        super(accessToken, TokenStrategy.ACCESS_TOKEN_PARAMETER);
    }

    /**
     * 获取用户信息
     *
     * @param openId
     * @return
     */
    @Override
    public WeixinUserInfo getUserInfo(String openId) {
        String url = WEIXIN_URL_GET_USER_INFO + openId;

        String result = getRestTemplate().getForObject(url, String.class);
        if(StringUtils.contains(result, "errcode")) {
            return null;
        }

        WeixinUserInfo userInfo = null;

        try{
            userInfo = objectMapper.readValue(result,WeixinUserInfo.class);
            System.out.println(userInfo);
        }catch (Exception e){
            e.printStackTrace();
        }

        return userInfo;
    }

    /**
     * 使用utf-8 替换默认的ISO-8859-1编码
     * @return
     */
    @Override
    protected List<HttpMessageConverter<?>> getMessageConverters() {
        List<HttpMessageConverter<?>> messageConverters = super.getMessageConverters();
        messageConverters.remove(0);
        messageConverters.add(new StringHttpMessageConverter(Charset.forName("UTF-8")));
        return messageConverters;
    }
}
