
package com.taotao.cloud.auth.biz.compliance.listener;

import cn.herodotus.engine.assistant.core.constants.BaseConstants;
import cn.herodotus.engine.oauth2.compliance.service.OAuth2ComplianceService;
import cn.herodotus.engine.oauth2.compliance.stamp.SignInFailureLimitedStampManager;
import cn.hutool.crypto.SecureUtil;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AccessTokenAuthenticationToken;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * <p>Description: 登录成功事件监听 </p>
 *
 * @author : gengwei.zheng
 * @date : 2022/7/7 20:58
 */
public class AuthenticationSuccessListener implements ApplicationListener<AuthenticationSuccessEvent> {

    private static final Logger log = LoggerFactory.getLogger(AuthenticationSuccessListener.class);


    private final SignInFailureLimitedStampManager stampManager;
    private final OAuth2ComplianceService complianceService;

    public AuthenticationSuccessListener(SignInFailureLimitedStampManager stampManager, OAuth2ComplianceService complianceService) {
        this.stampManager = stampManager;
        this.complianceService = complianceService;
    }

    @Override
    public void onApplicationEvent(AuthenticationSuccessEvent event) {

        Authentication authentication = event.getAuthentication();

        if (authentication instanceof OAuth2AccessTokenAuthenticationToken) {
            OAuth2AccessTokenAuthenticationToken authenticationToken = (OAuth2AccessTokenAuthenticationToken) authentication;
            Map<String, Object> params = authenticationToken.getAdditionalParameters();
            Object userName = params.get(BaseConstants.USER_NAME);
            String clientId = authenticationToken.getRegisteredClient().getId();

            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            if (ObjectUtils.isNotEmpty(request)) {
                String principal = ObjectUtils.isNotEmpty(userName) ? userName.toString() : null;
                complianceService.save(principal, clientId, "用户登录", request);
                if (StringUtils.isNotBlank(principal)) {
                    String key = SecureUtil.md5(principal);
                    boolean hasKey = stampManager.containKey(key);
                    if (hasKey) {
                        stampManager.delete(key);
                    }
                }

            } else {
                log.warn("[Herodotus] |- Can not get request, skip!");
            }
        }
    }
}
