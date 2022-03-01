package com.taotao.cloud.sys.biz.tools.security.configs;

import com.sanri.tools.modules.security.configs.jsonlogin.ResponseHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Slf4j
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Autowired
    private ResponseHandler responseHandler;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        log.error("CustomAuthenticationEntryPoint:{}",request.getRequestURI());
        responseHandler.writeAuthenticationFail(authException,response);
    }
}
