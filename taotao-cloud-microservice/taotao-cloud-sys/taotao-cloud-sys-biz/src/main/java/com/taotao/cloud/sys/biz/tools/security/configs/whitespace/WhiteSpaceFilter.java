package com.taotao.cloud.sys.biz.tools.security.configs.whitespace;

import com.taotao.cloud.sys.biz.tools.security.configs.UrlSecurityPermsLoad;
import com.taotao.cloud.sys.biz.tools.security.configs.jwt.JwtAuthenticationToken;
import com.taotao.cloud.sys.biz.tools.security.configs.jwt.TokenService;
import io.jsonwebtoken.Claims;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class WhiteSpaceFilter extends OncePerRequestFilter {
    private UrlSecurityPermsLoad urlSecurityPermsLoad;
    private TokenService tokenService;

    AntPathMatcher antPathMatcher =  new AntPathMatcher();

    // 白名单列表
    private List<String> whitespaceUrls = new ArrayList<>();

    public WhiteSpaceFilter(UrlSecurityPermsLoad urlSecurityPermsLoad,TokenService tokenService) {
        this.urlSecurityPermsLoad = urlSecurityPermsLoad;
        this.tokenService = tokenService;

        // 加载出所有的白名单
        whitespaceUrls = urlSecurityPermsLoad.findAnonUrls();
    }

    /**
     *
     */
    private static final String anonUser = "anon";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String uri = request.getRequestURI();
        boolean isWhitespaceUrl = false;
        for (String whitespaceUrl : whitespaceUrls) {
            if (antPathMatcher.match(whitespaceUrl,uri)){
                isWhitespaceUrl = true;
                if(SecurityContextHolder.getContext().getAuthentication() == null){
                    final TokenService.TokenInfo tokenInfo = new TokenService.TokenInfo(anonUser);
                    final String generatorToken = tokenService.generatorToken(tokenInfo);
                    final JwtAuthenticationToken jwtAuthenticationToken = new JwtAuthenticationToken(generatorToken);
                    jwtAuthenticationToken.setAuthenticated(true);
                    SecurityContextHolder.getContext().setAuthentication(jwtAuthenticationToken);
                }

                break;
            }
        }

        // 非白名单访问时, 需要清空匿名认证
        if (!isWhitespaceUrl){
            final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null){
                if (authentication instanceof JwtAuthenticationToken){
                    JwtAuthenticationToken jwtAuthenticationToken = (JwtAuthenticationToken) authentication;
                    final String token = jwtAuthenticationToken.getToken();
                    final Claims claims = TokenService.parseToken(token);
                    final TokenService.TokenInfo tokenInfo = tokenService.parseTokenInfo(claims);
                    if (anonUser.equals(tokenInfo.getUsername())){
                        SecurityContextHolder.getContext().setAuthentication(null);
                    }
                }
            }
        }
        filterChain.doFilter(request,response);
    }
}
