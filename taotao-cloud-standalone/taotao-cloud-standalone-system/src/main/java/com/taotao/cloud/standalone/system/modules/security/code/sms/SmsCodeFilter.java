package com.taotao.cloud.standalone.system.modules.security.code.sms;

import cn.hutool.core.util.ObjectUtil;
import com.taotao.cloud.standalone.common.exception.ValidateCodeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * @Classname SmsCodeFilter
 * @Description 短信验证码过滤器
 * @Author Created by Lihaodong (alias:小东啊) lihaodongmail@163.com
 * @Date 2019-07-08 12:10
 * @Version 1.0
 */
@Component
public class SmsCodeFilter extends OncePerRequestFilter {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private AuthenticationFailureHandler authenticationFailureHandler;

    private Set<String> urls = new HashSet<>();

    private AntPathMatcher antPathMatcher = new AntPathMatcher();

    @Override
    public void afterPropertiesSet() throws ServletException {
        super.afterPropertiesSet();
        // 这里配置需要拦截的地址
        urls.add("/mobile/login");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        boolean action = false;
        for (String url : urls) {
            if (antPathMatcher.match(url, request.getRequestURI())) {
                action = true;
                break;
            }
        }
        if (action) {
            try {
                validate(request);
            } catch (ValidateCodeException e) {
                authenticationFailureHandler.onAuthenticationFailure(request, response, e);
                return;
            }
        }
        filterChain.doFilter(request, response);
    }

    private void validate(HttpServletRequest request) {
        //短信验证码
        String smsCode = obtainSmsCode(request);
        // 手机号
        String mobile = obtainMobile(request);
        Object redisCode = redisTemplate.opsForValue().get(mobile);
        if (smsCode == null || smsCode.isEmpty()) {
            throw new ValidateCodeException("短信验证码不能为空");
        }
        if (ObjectUtil.isNull(redisCode)) {
            throw new ValidateCodeException("验证码已失效");
        }
        if (!smsCode.toLowerCase().equals(redisCode)) {
            throw new ValidateCodeException("短信验证码错误");
        }
    }

    /**
     * 获取验证码
     *
     * @param request
     * @return
     */
    private String obtainSmsCode(HttpServletRequest request) {
        return request.getParameter("smsCode");
    }

    /**
     * 获取手机号
     *
     * @param request
     * @return
     */
    private String obtainMobile(HttpServletRequest request) {
        return request.getParameter("phone");
    }

}
