package com.taotao.cloud.oauth2.client.security;

import com.taotao.cloud.common.enums.ResultEnum;
import com.taotao.cloud.common.utils.LogUtil;
import com.taotao.cloud.common.utils.ResponseUtil;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;


/**
 * 接口需要特定的权限，但是当前用户是匿名用户或者是记住我的用户
 *
 * @author zyc
 */
public class CustomizedAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
	    LogUtil.error("用户未认证", authException);
	    ResponseUtil.fail(response, ResultEnum.UNAUTHORIZED);
    }
}
