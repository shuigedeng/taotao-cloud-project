package com.taotao.cloud.standalone.system.modules.security.code.sms;

import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Classname SmsCodeAuthenticationFilter
 * @Description
 * 1.认证请求的方法必须为POST
 * 2.从request中获取手机号
 * 3.封装成自己的Authenticaiton的实现类SmsCodeAuthenticationToken（未认证）
 * 4.调用 AuthenticationManager 的 authenticate 方法进行验证（即SmsCodeAuthenticationProvider）
 * @Author shuigedeng
 * @since 2019-07-08 11:46
 * @Version 1.0
 */
public class SmsCodeAuthenticationFilter extends AbstractAuthenticationProcessingFilter {
    /**
     * request中必须含有mobile参数
     */
    private static final String PHONE_KEY = "phone";
    /**
     * post请求
     */
    private boolean postOnly = true;


    /**
     * 处理的手机验证码登录请求处理url
     */
    SmsCodeAuthenticationFilter() {
        super(new AntPathRequestMatcher("/mobile/login", HttpMethod.POST.toString()));
    }


    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        //判断是不是post请求
        if (postOnly && !request.getMethod().equals(HttpMethod.POST.toString())) {
            throw new AuthenticationServiceException("认证方法不支持: " + request.getMethod());
        }
        //从请求中获取手机号码
        String mobile = obtainMobile(request);
        if (mobile == null) {
            mobile = "";
        }
        mobile = mobile.trim();
        //创建SmsCodeAuthenticationToken(未认证)
        SmsCodeAuthenticationToken authRequest = new SmsCodeAuthenticationToken(mobile);
        //设置用户信息
        setDetails(request, authRequest);
        //返回Authentication实例
        return this.getAuthenticationManager().authenticate(authRequest);
    }
    private void setDetails(HttpServletRequest request,SmsCodeAuthenticationToken authRequest) {
        authRequest.setDetails(this.authenticationDetailsSource.buildDetails(request));
    }

    /**
     * 获取手机号
     * @param request
     * @return
     */
    private String obtainMobile(HttpServletRequest request) {
        return request.getParameter(PHONE_KEY);
    }
}
