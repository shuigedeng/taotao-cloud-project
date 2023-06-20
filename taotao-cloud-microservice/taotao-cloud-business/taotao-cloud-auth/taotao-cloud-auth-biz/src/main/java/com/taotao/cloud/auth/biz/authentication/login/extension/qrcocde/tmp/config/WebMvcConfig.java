package com.taotao.cloud.auth.biz.authentication.login.extension.qrcocde.tmp.config;

import com.taotao.cloud.auth.biz.authentication.login.extension.qrcocde.tmp.controller.interceptor.ConfirmInterceptor;
import com.taotao.cloud.auth.biz.authentication.login.extension.qrcocde.tmp.controller.interceptor.LoginInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

public class WebMvcConfig implements WebMvcConfigurer {

    private LoginInterceptor loginInterceptor;

    private ConfirmInterceptor confirmInterceptor;

    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginInterceptor)
                .excludePathPatterns("/static/**")
                .addPathPatterns("/getUser", "/login/scan");
        registry.addInterceptor(confirmInterceptor)
                .excludePathPatterns("/static/**")
                .addPathPatterns("/login/confirm");
    }
}
