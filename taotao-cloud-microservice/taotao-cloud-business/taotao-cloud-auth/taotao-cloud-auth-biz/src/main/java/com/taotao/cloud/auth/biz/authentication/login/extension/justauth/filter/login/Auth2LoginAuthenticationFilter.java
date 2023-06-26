package com.taotao.cloud.auth.biz.authentication.login.extension.justauth.filter.login;

import com.taotao.cloud.auth.biz.authentication.login.extension.justauth.JustAuthLoginAuthenticationToken;
import com.taotao.cloud.auth.biz.authentication.login.extension.justauth.JustAuthRequestHolder;
import com.taotao.cloud.auth.biz.authentication.login.extension.justauth.filter.redirect.Auth2DefaultRequestResolver;
import com.taotao.cloud.auth.biz.authentication.login.extension.justauth.userdetails.TemporaryUser;
import com.taotao.cloud.common.utils.common.JsonUtils;
import com.taotao.cloud.security.justauth.justauth.request.Auth2DefaultRequest;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.data.redis.core.types.Expiration;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.authentication.event.InteractiveAuthenticationSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

import static java.util.Objects.nonNull;

/**
 * An implementation of an {@link AbstractAuthenticationProcessingFilter} for OAuth 2.0
 * Login.
 *
 * @author Joe Grandja
 * @author YongWu zheng
 * @since 5.0
 * @see AbstractAuthenticationProcessingFilter
 * @see <a target="_blank" href="https://tools.ietf.org/html/rfc6749#section-4.1">Section
 * 4.1 Authorization Code Grant</a>
 * @see <a target="_blank" href=
 * "https://tools.ietf.org/html/rfc6749#section-4.1.2">Section 4.1.2 Authorization
 * Response</a>
 */
@SuppressWarnings("JavaDoc")
public class Auth2LoginAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    public static final String TEMPORARY_USER_CACHE_KEY_PREFIX = "TEMPORARY_USER_REDIS_CACHE_KEY:";
    public static final String TEMPORARY_USERNAME_PARAM_NAME = "temporary_username";
    private static final String AUTHORIZATION_REQUEST_NOT_FOUND_ERROR_CODE = "authorization_request_not_found";

    private final Auth2DefaultRequestResolver authorizationRequestResolver;
    private final RedisConnectionFactory redisConnectionFactory;

    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    /**
     * 第三方授权登录后如未注册用户不支持自动注册功能, 则跳转到此 url 进行注册逻辑, 此 url 必须开发者自己实现
     */
    private final String signUpUrl;

    /**
     * Constructs an {@code Auth2LoginAuthenticationFilter} using the provided
     * parameters.
     * @param filterProcessesUrl the {@code URI} where this {@code Filter} will process
     * the authentication requests, not null
     * @param signUpUrl          第三方授权登录后如未注册用户不支持自动注册功能, 则跳转到此 url 进行注册逻辑, 此 url 必须开发者自己实现
     * @param authenticationDetailsSource      {@link AuthenticationDetailsSource}
     * @param redisConnectionFactory           redis connection factory
     * @since 5.1
     */
    public Auth2LoginAuthenticationFilter(@NonNull String filterProcessesUrl, @Nullable String signUpUrl,
                                          @Nullable AuthenticationDetailsSource<HttpServletRequest, ?> authenticationDetailsSource,
                                          @Autowired(required = false)
                                          @Nullable RedisConnectionFactory redisConnectionFactory) {
        super(filterProcessesUrl + "/*");
        this.authorizationRequestResolver = new Auth2DefaultRequestResolver(filterProcessesUrl);
        this.signUpUrl = signUpUrl;
        this.redisConnectionFactory = redisConnectionFactory;
        if (authenticationDetailsSource != null) {
            setAuthenticationDetailsSource(authenticationDetailsSource);
        }
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        MultiValueMap<String, String> params = Auth2AuthorizationResponseUtils.toMultiMap(request.getParameterMap());
        if (!Auth2AuthorizationResponseUtils.isAuthorizationResponse(params)) {
            OAuth2Error oauth2Error = new OAuth2Error(OAuth2ErrorCodes.INVALID_REQUEST);
            throw new OAuth2AuthenticationException(oauth2Error, oauth2Error.toString());
        }

        String registrationId = this.authorizationRequestResolver.resolveRegistrationId(request);
        Auth2DefaultRequest auth2DefaultRequest = null;
        if (StringUtils.hasText(registrationId)) {
            auth2DefaultRequest = JustAuthRequestHolder.getAuth2DefaultRequest(registrationId);
        }

        if (auth2DefaultRequest == null) {

            OAuth2Error oauth2Error = new OAuth2Error(AUTHORIZATION_REQUEST_NOT_FOUND_ERROR_CODE,
                                                      "Client Registration not found with Id: " + registrationId, null);
            throw new OAuth2AuthenticationException(oauth2Error, oauth2Error.toString());
        }

        JustAuthLoginAuthenticationToken authenticationRequest = new JustAuthLoginAuthenticationToken(auth2DefaultRequest, request);

        // Allow subclasses to set the "details" property
        setDetails(request, authenticationRequest);

        // 通过 AuthenticationManager 转到相应的 Provider 对 Auth2LoginAuthenticationToken 进行认证
        return this.getAuthenticationManager().authenticate(authenticationRequest);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
											HttpServletResponse response, FilterChain chain, Authentication authResult)
            throws IOException, ServletException {

        if (logger.isDebugEnabled()) {
            logger.debug("Authentication success. Updating SecurityContextHolder to contain: "
                                 + authResult);
        }

        SecurityContextHolder.getContext().setAuthentication(authResult);

        // Fire event
        if (this.eventPublisher != null) {
            eventPublisher.publishEvent(new InteractiveAuthenticationSuccessEvent(
                    authResult, this.getClass()));
        }

        // 自定义注册逻辑
        final Object principal = authResult.getPrincipal();
        if (principal instanceof TemporaryUser temporaryUser && StringUtils.hasText(this.signUpUrl)) {
			String username = temporaryUser.getUsername();
            String key = TEMPORARY_USER_CACHE_KEY_PREFIX + username;
            if (nonNull(redisConnectionFactory)) {
                // 存入 redis
                try (RedisConnection connection = redisConnectionFactory.getConnection()) {
                    connection.stringCommands().set(key.getBytes(StandardCharsets.UTF_8),
                                   JsonUtils.toJson(temporaryUser).getBytes(StandardCharsets.UTF_8),
                                   Expiration.from(86400L, TimeUnit.SECONDS),
                                   RedisStringCommands.SetOption.UPSERT);
                }
            }
            else {
                // 存入 session
                request.getSession().setAttribute(key, temporaryUser);
            }
            this.redirectStrategy.sendRedirect(request, response,
                                               this.signUpUrl + "?"
                                                        + TEMPORARY_USERNAME_PARAM_NAME + "="
                                                        + URLEncoder.encode(username, StandardCharsets.UTF_8));

            return;
        }
        else {
            getRememberMeServices().loginSuccess(request, response, authResult);
        }

        getSuccessHandler().onAuthenticationSuccess(request, response, authResult);
    }

    /**
     * Provided so that subclasses may configure what is put into the auth
     * request's details property.
     *
     * @param request that an auth request is being created for
     * @param authRequest the auth request object that should have its details
     * set
     */
    protected void setDetails(HttpServletRequest request, JustAuthLoginAuthenticationToken authRequest) {
        authRequest.setDetails(authenticationDetailsSource.buildDetails(request));
    }

    @SuppressWarnings("unused")
    public void setRedirectStrategy(RedirectStrategy redirectStrategy) {
        this.redirectStrategy = redirectStrategy;
    }
}
