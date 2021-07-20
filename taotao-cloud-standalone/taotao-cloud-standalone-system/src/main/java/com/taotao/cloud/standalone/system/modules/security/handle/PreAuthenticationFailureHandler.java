package com.taotao.cloud.standalone.system.modules.security.handle;


import com.taotao.cloud.standalone.common.exception.ValidateCodeException;
import com.taotao.cloud.standalone.common.utils.R;
import com.taotao.cloud.standalone.security.util.SecurityUtil;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Classname FebsAuthenticationFailureHandler
 * @Description 登录失败处理器
 * @Author shuigedeng
 * @since 2019-07-07 23:45
 * @Version 1.0
 */
@Component
public class PreAuthenticationFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException {
        String message;

        if (exception instanceof ValidateCodeException) {
            message = exception.getMessage();
        } else {
            message = "认证失败，请联系网站管理员！";
        }
        response.setContentType("application/json;charset=utf-8");
        SecurityUtil.writeJavaScript(R.error(message), response);
    }
}


