
package com.taotao.cloud.auth.biz.compliance.listener;

import cn.herodotus.engine.assistant.core.constants.BaseConstants;
import cn.herodotus.engine.cache.core.exception.MaximumLimitExceededException;
import cn.herodotus.engine.oauth2.compliance.service.OAuth2AccountStatusService;
import cn.herodotus.engine.oauth2.compliance.stamp.SignInFailureLimitedStampManager;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AuthorizationGrantAuthenticationToken;

import java.time.Duration;
import java.util.Map;

/**
 * <p>Description: 登出成功监听 </p>
 *
 * @author : gengwei.zheng
 * @date : 2021/12/18 17:58
 */
public class AuthenticationFailureListener implements ApplicationListener<AbstractAuthenticationFailureEvent> {

    private static final Logger log = LoggerFactory.getLogger(AuthenticationFailureListener.class);

    private final SignInFailureLimitedStampManager stampManager;
    private final OAuth2AccountStatusService accountLockService;

    public AuthenticationFailureListener(SignInFailureLimitedStampManager stampManager, OAuth2AccountStatusService accountLockService) {
        this.stampManager = stampManager;
        this.accountLockService = accountLockService;
    }

    @Override
    public void onApplicationEvent(AbstractAuthenticationFailureEvent event) {

        log.debug("[Herodotus] |- Current Event [{}] items", event.getClass().getName());

        if (event instanceof AuthenticationFailureBadCredentialsEvent) {
            Authentication authentication = event.getAuthentication();

            if (authentication instanceof OAuth2AuthorizationGrantAuthenticationToken) {
                OAuth2AuthorizationGrantAuthenticationToken token = (OAuth2AuthorizationGrantAuthenticationToken) authentication;

                Map<String, Object> params = token.getAdditionalParameters();
                String username = getPrincipal(params);

                if (StringUtils.isNotBlank(username)) {
                    int maxTimes = stampManager.getComplianceProperties().getSignInFailureLimited().getMaxTimes();
                    Duration expire = stampManager.getComplianceProperties().getSignInFailureLimited().getExpire();
                    try {
                        int times = stampManager.counting(username, maxTimes, expire, true, "AuthenticationFailureListener");

                        log.debug("[Herodotus] |- Sign in user input password error [{}] items", times);
                    }catch (MaximumLimitExceededException e) {
                        log.warn("[Herodotus] |- User [{}] password error [{}] items, LOCK ACCOUNT!", username, maxTimes);
                        accountLockService.lock(username);
                    }
                }
            }
        }
    }

    private String getPrincipal(Map<String, Object> params) {
        if (MapUtils.isNotEmpty(params)) {
            if (params.containsKey(BaseConstants.USER_NAME)) {
                Object value = params.get(BaseConstants.USER_NAME);
                if (ObjectUtils.isNotEmpty(value)) {
                    return (String) value;
                }
            }
        }

        return null;
    }
}
