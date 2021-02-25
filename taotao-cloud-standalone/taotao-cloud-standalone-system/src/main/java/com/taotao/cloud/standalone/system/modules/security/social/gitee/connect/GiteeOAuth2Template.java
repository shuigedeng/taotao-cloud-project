package com.taotao.cloud.standalone.system.modules.security.social.gitee.connect;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.social.oauth2.AccessGrant;
import org.springframework.social.oauth2.OAuth2Template;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;

/**
 * @Classname GiteeAdapter
 * @Description
 * @Author Created by Lihaodong (alias:小东啊) lihaodongmail@163.com
 * @Date 2019-07-08 21:49
 * @Version 1.0
 */
@Slf4j
public class GiteeOAuth2Template extends OAuth2Template {


    public GiteeOAuth2Template(String clientId, String clientSecret, String authorizeUrl, String accessTokenUrl) {
        super(clientId, clientSecret, authorizeUrl, accessTokenUrl);
        setUseParametersForClientAuthentication(true);
    }

    @Override
    protected AccessGrant postForAccessGrant(String accessTokenUrl, MultiValueMap<String, String> parameters) {

        // https://gitee.com/oauth/token?grant_type=authorization_code&code={code}&client_id={client_id}&redirect_uri={redirect_uri}&client_secret={client_secret}
        // 自己拼接url
        String clientId = parameters.getFirst("client_id");
        String clientSecret = parameters.getFirst("client_secret");
        String code = parameters.getFirst("code");
        String redirectUri = parameters.getFirst("redirect_uri");

        String url = String.format("https://gitee.com/oauth/token?grant_type=authorization_code&code=%s&client_id=%s&redirect_uri=%s&client_secret=%s", code, clientId, redirectUri, clientSecret);
        String post = HttpUtil.post(url, "",5000);

        log.info("获取accessToke的响应：" + post);
        JSONObject object = JSONObject.parseObject(post);
        String accessToken = (String) object.get("access_token");
        String scope = (String) object.get("scope");
        String refreshToken = (String) object.get("refresh_token");
        int expiresIn = (Integer) object.get("expires_in");

        log.info("获取Toke的响应:{},scope响应:{},refreshToken响应:{},expiresIn响应:{}", accessToken, scope, refreshToken, expiresIn);
        return new AccessGrant(accessToken, scope, refreshToken, (long) expiresIn);
    }

    @Override
    protected RestTemplate createRestTemplate() {
        RestTemplate restTemplate = super.createRestTemplate();
        restTemplate.getMessageConverters().add(new StringHttpMessageConverter(Charset.forName("UTF-8")));
        return restTemplate;
    }

}
