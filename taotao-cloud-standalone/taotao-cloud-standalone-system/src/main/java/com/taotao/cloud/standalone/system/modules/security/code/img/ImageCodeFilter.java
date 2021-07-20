package com.taotao.cloud.standalone.system.modules.security.code.img;

import cn.hutool.core.util.StrUtil;
import com.taotao.cloud.standalone.common.constant.PreConstant;
import com.taotao.cloud.standalone.common.exception.ValidateCodeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Classname ImageCodeFilter
 * @Description 图形验证码过滤器
 * @Author shuigedeng
 * @since 2019-07-07 23:02
 * @Version 1.0
 */
@Component
public class ImageCodeFilter extends OncePerRequestFilter {
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private AuthenticationFailureHandler authenticationFailureHandler;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        // 获取token参数
        String token = obtainToken(request);
        // 必须为/login请求和post请求
        if (StrUtil.equals("/login", request.getRequestURI()) && StrUtil.isEmpty(token) && StrUtil.equalsIgnoreCase(request.getMethod(), "POST")) {
            try {
                // 1. 进行验证码的校验
                validateCode(request);
            } catch (ValidateCodeException e) {
                // 2. 如果校验不通过，调用SpringSecurity的校验失败处理器
                authenticationFailureHandler.onAuthenticationFailure(request, response, e);
                return;
            }
        }
        // 3. 通过放行
        chain.doFilter(request, response);
    }

    /**
     * 验证流程
     *
     * @param request
     */
    private void validateCode(HttpServletRequest request) {

        String captcha = obtainImageCode(request);
        String t = obtainT(request);
        // 验证验证码
        if (StrUtil.isBlank(captcha)) {
            throw new ValidateCodeException("验证码不能为空");
        }
        // 从redis中获取之前保存的验证码跟前台传来的验证码进行匹配
        Object kaptcha = redisTemplate.opsForValue().get(PreConstant.PRE_IMAGE_KEY + t);
        if (kaptcha == null) {
            throw new ValidateCodeException("验证码已失效");
        }
        if (!captcha.toLowerCase().equals(kaptcha)) {
            throw new ValidateCodeException("验证码错误");
        }
    }

    /**
     * 获取前端传来的图片验证码
     *
     * @param request
     * @return
     */
    private String obtainImageCode(HttpServletRequest request) {
        String imageCode = "code";
        return request.getParameter(imageCode);
    }

    /**
     * 获取前端传来的图片验证码
     *
     * @param request
     * @return
     */
    private String obtainToken(HttpServletRequest request) {
        String token = "token";
        return request.getParameter(token);
    }

    /**
     * 获取前端传来的图片验证码
     *
     * @param request
     * @return
     */
    private String obtainT(HttpServletRequest request) {
        String token = "key";
        return request.getParameter(token);
    }

    /**
     * 失败处理器
     *
     * @param authenticationFailureHandler
     */
    public void setAuthenticationFailureHandler(AuthenticationFailureHandler authenticationFailureHandler) {
        this.authenticationFailureHandler = authenticationFailureHandler;
    }

}
