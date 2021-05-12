package com.taotao.cloud.oauth2.api.tmp.social;

import com.taotao.cloud.oauth2.api.tmp.AuthUserOauth2;
import com.taotao.cloud.oauth2.api.tmp.social.user.CustomOAuth2User;
import java.util.Map;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;

/**
 * 测试分享代码
 */
public interface SocialDetailsService {

    /**
     * @param registrationId 第三方平台的注册Id
     * @param name           第三方平台的用户唯一id
     * @return 用户跟人信息
     * @throws OAuth2AuthenticationException 返回结果为空,抛出异常
     */
    UserDetails loadUserBySocial(String registrationId, String name);

    /**
     * 绑定当前账号
     *
     * @param oAuth2User
     * @param registrationId
     * @param username
     * @return
     */
    boolean bindSocial(CustomOAuth2User oAuth2User, String registrationId, String username);

    /**
     * 解绑
     *
     * @return
     */
    boolean unbindSocial(String registrationId, String principalName);

    /**
     * 查找用户所有的登录信息
     *
     * @param all is true, 会返回未绑定的
     * @return
     */
    Map<String, AuthUserOauth2> getSocials(boolean all);

}
