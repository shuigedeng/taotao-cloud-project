package com.taotao.cloud.member.biz.connect.request;


import com.alibaba.fastjson.JSONObject;
import com.taotao.cloud.common.utils.common.UrlBuilder;
import com.taotao.cloud.member.biz.connect.config.AuthConfig;
import com.taotao.cloud.member.biz.connect.config.ConnectAuthEnum;
import com.taotao.cloud.member.biz.connect.entity.dto.AuthCallback;
import com.taotao.cloud.member.biz.connect.entity.dto.AuthResponse;
import com.taotao.cloud.member.biz.connect.entity.dto.AuthToken;
import com.taotao.cloud.member.biz.connect.entity.dto.ConnectAuthUser;
import com.taotao.cloud.member.biz.connect.entity.enums.AuthResponseStatus;
import com.taotao.cloud.member.biz.connect.entity.enums.AuthUserGender;
import com.taotao.cloud.member.biz.connect.exception.AuthException;
import com.taotao.cloud.redis.repository.RedisRepository;


/**
 * 微博登录
 */
public class BaseAuthWeiboRequest extends BaseAuthRequest {

    public BaseAuthWeiboRequest(AuthConfig config, RedisRepository redisRepository) {
        super(config, ConnectAuthEnum.WEIBO, redisRepository);
    }

    @Override
    protected AuthToken getAccessToken(AuthCallback authCallback) {
        String response = doPostAuthorizationCode(authCallback.getCode());
        JSONObject accessTokenObject = JSONObject.parseObject(response);
        if (accessTokenObject.containsKey("error")) {
            throw new AuthException(accessTokenObject.getString("error_description"));
        }
        return AuthToken.builder()
                .accessToken(accessTokenObject.getString("access_token"))
                .uid(accessTokenObject.getString("uid"))
                .openId(accessTokenObject.getString("uid"))
                .expireIn(accessTokenObject.getIntValue("expires_in"))
                .build();
    }

    @Override
    protected ConnectAuthUser getUserInfo(AuthToken authToken) {
        String accessToken = authToken.getAccessToken();
        String uid = authToken.getUid();
        String oauthParam = String.format("uid=%s&access_token=%s", uid, accessToken);

        HttpHeader httpHeader = new HttpHeader();
        httpHeader.add("Authorization", "OAuth2 " + oauthParam);
        httpHeader.add("API-RemoteIP", IpUtils.getLocalIp());
        String userInfo = new HttpUtils(config.getHttpConfig()).get(userInfoUrl(authToken), null, httpHeader, false);
        JSONObject object = JSONObject.parseObject(userInfo);
        if (object.containsKey("error")) {
            throw new AuthException(object.getString("error"));
        }
        return ConnectAuthUser.builder()
                .rawUserInfo(object)
                .uuid(object.getString("id"))
                .username(object.getString("name"))
                .avatar(object.getString("profile_image_url"))
                .blog(StringUtils.isEmpty(object.getString("url")) ? "https://weibo.com/" + object.getString("profile_url") : object
                        .getString("url"))
                .nickname(object.getString("screen_name"))
                .location(object.getString("location"))
                .remark(object.getString("description"))
                .gender(AuthUserGender.getRealGender(object.getString("gender")))
                .token(authToken)
                .source(source.toString())
                .build();
    }

    /**
     * 返回获取userInfo的url
     *
     * @param authToken authToken
     * @return 返回获取userInfo的url
     */
    @Override
    protected String userInfoUrl(AuthToken authToken) {
        return UrlBuilder.fromBaseUrl(source.userInfo())
                .queryParam("access_token", authToken.getAccessToken())
                .queryParam("uid", authToken.getUid())
                .build();
    }

    @Override
    public String authorize(String state) {
        return UrlBuilder.fromBaseUrl(super.authorize(state))
                .queryParam("scope", "all")
                .build();
    }

    @Override
    public AuthResponse revoke(AuthToken authToken) {
        String response = doGetRevoke(authToken);
        JSONObject object = JSONObject.parseObject(response);
        if (object.containsKey("error")) {
            return AuthResponse.builder()
                    .code(AuthResponseStatus.FAILURE.getCode())
                    .msg(object.getString("error"))
                    .build();
        }
        //返回 result = true 表示取消授权成功，否则失败
        AuthResponseStatus status = object.getBooleanValue("result") ? AuthResponseStatus.SUCCESS : AuthResponseStatus.FAILURE;
        return AuthResponse.builder().code(status.getCode()).msg(status.getMsg()).build();
    }
}
