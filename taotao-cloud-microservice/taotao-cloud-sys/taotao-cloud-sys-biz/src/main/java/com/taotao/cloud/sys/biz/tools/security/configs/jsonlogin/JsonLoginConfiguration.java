package com.taotao.cloud.sys.biz.tools.security.configs.jsonlogin;

import com.taotao.cloud.sys.biz.tools.security.configs.jwt.TokenService;
import com.taotao.cloud.sys.biz.tools.security.service.dtos.SecurityUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.HttpSecurityBuilder;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.authentication.session.NullAuthenticatedSessionStrategy;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Configuration
public class JsonLoginConfiguration<T extends JsonLoginConfiguration<T, B>, B extends HttpSecurityBuilder<B>> extends AbstractHttpConfigurer<T, B> {
    @Autowired
    private ResponseHandler responseHandler;
    @Autowired
    private TokenService tokenService;

    @Override
    public void configure(B http) throws Exception {
        final JsonLoginAuthenticationFilter jsonLoginAuthenticationFilter = new JsonLoginAuthenticationFilter();
        jsonLoginAuthenticationFilter.setAuthenticationManager(http.getSharedObject(AuthenticationManager.class));
        jsonLoginAuthenticationFilter.setSessionAuthenticationStrategy(new NullAuthenticatedSessionStrategy());

        final LoginAuthenticationHandler loginAuthenticationHandler = new LoginAuthenticationHandler();

        jsonLoginAuthenticationFilter.setAuthenticationFailureHandler(loginAuthenticationHandler);
        jsonLoginAuthenticationFilter.setAuthenticationSuccessHandler(loginAuthenticationHandler);
        jsonLoginAuthenticationFilter.setAllowSessionCreation(false);

        http.addFilterAfter(postProcess(jsonLoginAuthenticationFilter), LogoutFilter.class);
    }

    /**
     * 登录处理类
     */
    public final class LoginAuthenticationHandler implements AuthenticationSuccessHandler , AuthenticationFailureHandler{

        @Override
        public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
            responseHandler.writeLoginFail(exception,response);
        }

        @Override
        public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
            final String username = ((SecurityUser) authentication.getPrincipal()).getToolUser().getUsername();
            final TokenService.TokenInfo tokenInfo = new TokenService.TokenInfo(username);
            final String generatorToken = tokenService.generatorToken(tokenInfo);
            responseHandler.writeTokenAndAuthenticationSuccess(generatorToken,response);
        }
    }

}
