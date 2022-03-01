package com.taotao.cloud.sys.biz.tools.security.configs.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.CredentialsProvider;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.www.NonceExpiredException;
import org.springframework.util.Assert;

public class JwtAuthenticationProvider implements AuthenticationProvider , InitializingBean {
    private TokenService tokenService;
    private UserDetailsService userDetailsService;
    private PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        final String token = ((JwtAuthenticationToken) authentication).getToken();
        try {
            if (StringUtils.isBlank(token)){
                throw new InsufficientAuthenticationException("token 为空");
            }

            final TokenService.TokenInfo tokenInfo = tokenService.parseTokenInfo(TokenService.parseToken(token));
            final String username = tokenInfo.getUsername();
            final UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            return new JwtAuthenticationToken(userDetails,token, userDetails.getAuthorities());
        }catch (JwtException e){
            throw new InsufficientAuthenticationException(e.getMessage(),e);
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.isAssignableFrom(JwtAuthenticationToken.class);
    }

    public void setTokenService(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    public void setUserDetailsService(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(this.userDetailsService, "A UserDetailsService must be set");
        Assert.notNull(this.tokenService, "A tokenService must be set");
        Assert.notNull(this.passwordEncoder, "A passwordEncoder must be set");
    }
}
