

package com.taotao.cloud.auth.biz.authentication.response;

import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.security.springsecurity.core.exception.SecurityGlobalExceptionHandler;
import com.taotao.cloud.security.springsecurity.core.utils.WebUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.util.UrlUtils;
import org.springframework.util.Assert;

import java.io.IOException;

/**
 * <p> Description : 表单登录失败处理器 </p>
 *
 *
 * @date : 2020/1/26 18:08
 * @see SimpleUrlAuthenticationFailureHandler
 */
public class OAuth2FormLoginAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    private static final Logger log = LoggerFactory.getLogger(OAuth2FormLoginAuthenticationFailureHandler.class);

    private String defaultFailureUrl;
    private boolean forwardToDestination = false;
    private boolean allowSessionCreation = true;
    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    public OAuth2FormLoginAuthenticationFailureHandler() {
    }

    public OAuth2FormLoginAuthenticationFailureHandler(String defaultFailureUrl) {
        setDefaultFailureUrl(defaultFailureUrl);
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {

        if (this.defaultFailureUrl == null) {
            if (this.logger.isTraceEnabled()) {
                this.logger.trace("Sending 401 Unauthorized error since no failure URL is set");
            } else {
                this.logger.debug("Sending 401 Unauthorized error");
            }
            response.sendError(HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED.getReasonPhrase());
            return;
        }

        String errorMessage = "请刷新重试！";

        Result<String> result = SecurityGlobalExceptionHandler.resolveSecurityException(e, request.getRequestURI());
        if (ObjectUtils.isNotEmpty(result) && StringUtils.isNotBlank(result.getMessage())) {
            errorMessage = result.getMessage();
        } else {
            errorMessage = e.getClass().getSimpleName();
           log.info("[Herodotus] |- Form Login Authentication Failure Handler,  Can not find the exception name [{}] in dictionary, please do optimize ", errorMessage);
        }

        saveException(request, errorMessage);

        if (this.isUseForward()) {
           log.info("Forwarding to " + this.defaultFailureUrl);
            request.getRequestDispatcher(this.defaultFailureUrl).forward(request, response);
        } else {
            this.redirectStrategy.sendRedirect(request, response, this.defaultFailureUrl);
        }
    }

    protected final void saveException(HttpServletRequest request, String message) {
        if (this.isUseForward()) {
            request.setAttribute(WebAttributes.AUTHENTICATION_EXCEPTION, message);
            return;
        }
        HttpSession session = WebUtils.getSession(request);
        if (session != null || this.isAllowSessionCreation()) {
            request.getSession().setAttribute(WebAttributes.AUTHENTICATION_EXCEPTION, message);
        }
    }

    /**
     * The URL which will be used as the failure destination.
     *
     * @param defaultFailureUrl the failure URL, for example "/loginFailed.jsp".
     */
	@Override
    public void setDefaultFailureUrl(String defaultFailureUrl) {
        Assert.isTrue(UrlUtils.isValidRedirectUrl(defaultFailureUrl),
                () -> "'" + defaultFailureUrl + "' is not a valid redirect URL");
        this.defaultFailureUrl = defaultFailureUrl;
    }
	@Override
    protected boolean isUseForward() {
        return this.forwardToDestination;
    }

    /**
     * If set to <tt>true</tt>, performs a forward to the failure destination URL instead
     * of a redirect. Defaults to <tt>false</tt>.
     */
	@Override
    public void setUseForward(boolean forwardToDestination) {
        this.forwardToDestination = forwardToDestination;
    }

    /**
     * Allows overriding of the behaviour when redirecting to a target URL.
     */
	@Override
    public void setRedirectStrategy(RedirectStrategy redirectStrategy) {
        this.redirectStrategy = redirectStrategy;
    }

	@Override
    protected RedirectStrategy getRedirectStrategy() {
        return this.redirectStrategy;
    }

	@Override
    protected boolean isAllowSessionCreation() {
        return this.allowSessionCreation;
    }

	@Override
    public void setAllowSessionCreation(boolean allowSessionCreation) {
        this.allowSessionCreation = allowSessionCreation;
    }
}