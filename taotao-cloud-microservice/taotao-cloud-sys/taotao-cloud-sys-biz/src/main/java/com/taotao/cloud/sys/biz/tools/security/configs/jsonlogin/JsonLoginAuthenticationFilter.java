package com.taotao.cloud.sys.biz.tools.security.configs.jsonlogin;

import java.io.IOException;
import java.nio.charset.Charset;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.StreamUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class JsonLoginAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    public JsonLoginAuthenticationFilter(){
        super(new AntPathRequestMatcher("/login","POST"));
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        String body = StreamUtils.copyToString(request.getInputStream(), Charset.forName("UTF-8"));
        if (StringUtils.isBlank(body)){
            throw new BadCredentialsException("用户名密码空");
        }
        final JSONObject jsonObject = JSON.parseObject(body);
        String usename = jsonObject.getString("username");
        String password = jsonObject.getString("password");

        final UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(usename, password);
        return getAuthenticationManager().authenticate(usernamePasswordAuthenticationToken);
    }
}
