package com.taotao.cloud.sys.biz.tools.security.configs;

import com.sanri.tools.modules.security.configs.jsonlogin.ResponseHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Slf4j
public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    @Autowired
    private ResponseHandler responseHandler;

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        final String requestURI = request.getRequestURI();
        log.error("CustomAccessDeniedHandler:{}",requestURI);
        responseHandler.writeAuthorizationFail(accessDeniedException,response);
    }
}
