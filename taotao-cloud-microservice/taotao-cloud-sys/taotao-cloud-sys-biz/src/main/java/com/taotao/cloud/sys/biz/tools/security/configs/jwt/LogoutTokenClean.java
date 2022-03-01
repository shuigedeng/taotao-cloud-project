package com.taotao.cloud.sys.biz.tools.security.configs.jwt;

import com.sanri.tools.modules.core.exception.SystemMessage;
import com.sanri.tools.modules.security.configs.jsonlogin.ResponseHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Slf4j
public class LogoutTokenClean implements LogoutSuccessHandler {
    @Autowired
    private ResponseHandler responseHandler;

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        // 前端清空就行了
        responseHandler.writeSuccess(response);
    }
}
