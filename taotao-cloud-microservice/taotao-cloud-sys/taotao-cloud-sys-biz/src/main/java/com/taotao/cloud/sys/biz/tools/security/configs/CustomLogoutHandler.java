package com.taotao.cloud.sys.biz.tools.security.configs;

import com.sanri.tools.modules.security.configs.jsonlogin.ResponseHandler;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CustomLogoutHandler implements LogoutHandler, LogoutSuccessHandler {
    private ResponseHandler responseHandler;

    public CustomLogoutHandler(ResponseHandler responseHandler) {
        this.responseHandler = responseHandler;
    }

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        clearToken(authentication);
    }

    /**
     * 如果需要严格防止 token 泄露 , 那么还是需要存储 token , 然后登出时清空, 请求时检测
     * 目前不做严格的 token 检测, 只在前端 cookie 中清空
     * @param authentication
     */
    protected void clearToken(Authentication authentication) {
        if(authentication == null){
            return ;
        }
        UserDetails user = (UserDetails)authentication.getPrincipal();

    }

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        responseHandler.writeSuccess(response);
    }
}
