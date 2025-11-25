/*
 * Copyright (c) 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.taotao.cloud.gateway.anti_reptile.filter;

import com.taotao.boot.common.utils.log.LogUtils;
import com.taotao.boot.common.utils.servlet.ResponseUtils;
import com.taotao.cloud.gateway.anti_reptile.AntiReptileProperties;
import com.taotao.cloud.gateway.anti_reptile.module.VerifyImageDTO;
import com.taotao.cloud.gateway.anti_reptile.rule.RuleActuator;
import com.taotao.cloud.gateway.anti_reptile.util.VerifyImageUtil;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Pattern;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

public class AntiReptileFilter implements WebFilter, ApplicationContextAware {

    private final RuleActuator actuator;

    private final VerifyImageUtil verifyImageUtil;

    private final AntiReptileProperties antiReptileProperties;

    private String antiReptileForm;

    private List<String> includeUrls;

    private boolean globalFilterMode;

    private ApplicationContext ctx;

    private final AtomicBoolean initialized = new AtomicBoolean(false);

    public AntiReptileFilter(
            RuleActuator actuator,
            VerifyImageUtil verifyImageUtil,
            AntiReptileProperties antiReptileProperties) {
        this.actuator = actuator;
        this.verifyImageUtil = verifyImageUtil;
        this.antiReptileProperties = antiReptileProperties;
    }

    public void init() {
        ClassPathResource classPathResource = new ClassPathResource("verify/index.html");
        try {
            classPathResource.getInputStream();
            byte[] bytes = FileCopyUtils.copyToByteArray(classPathResource.getInputStream());
            this.antiReptileForm = new String(bytes, StandardCharsets.UTF_8);
        } catch (IOException e) {
            LogUtils.info("反爬虫验证模板加载失败！");
            LogUtils.error(e);
        }

        // this.actuator = ctx.getBean(RuleActuator.class);
        // this.verifyImageUtil = ctx.getBean(VerifyImageUtil.class);
        // this.includeUrls = ctx.getBean(AntiReptileProperties.class).getIncludeUrls();
        // this.globalFilterMode = ctx.getBean(AntiReptileProperties.class).isGlobalFilterMode();

        this.includeUrls = antiReptileProperties.getIncludeUrls();
        this.globalFilterMode = antiReptileProperties.isGlobalFilterMode();

        if (this.includeUrls == null) {
            this.includeUrls = new ArrayList<>();
        }
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        if (!initialized.get()) {
            init();
            initialized.set(true);
        }

        // HandlerMethod handlerMethod;
        // try {
        //	handlerMethod = (HandlerMethod) handler;
        // } catch (ClassCastException e) {
        //	return true;
        // }
        //
        // Method method = handlerMethod.getMethod();
        // AntiReptile antiReptile = AnnotationUtils.findAnnotation(method, AntiReptile.class);
        // boolean isAntiReptileAnnotation = antiReptile != null;

        // String requestUrl = request.getRequestURI();
        // if (isIntercept(requestUrl, isAntiReptileAnnotation) && !actuator.isAllowed(request,
        //	response)) {
        //	CrosUtil.setCrosHeader(response);
        //	response.setContentType("text/html;charset=utf-8");
        //	response.setStatus(509);
        //	VerifyImageDTO verifyImage = verifyImageUtil.generateVerifyImg();
        //	verifyImageUtil.saveVerifyCodeToRedis(verifyImage);
        //	String str1 = this.antiReptileForm.replace("verifyId_value", verifyImage.getVerifyId());
        //	String str2 = str1.replaceAll("verifyImg_value", verifyImage.getVerifyImgStr());
        //	String str3 = str2.replaceAll("realRequestUri_value", requestUrl);
        //	response.getWriter().write(str3);
        //	response.getWriter().close();
        //	return false;
        // }
        // return true;

        String requestUrl = exchange.getRequest().getURI().getRawPath();
        if (isIntercept(requestUrl, false) && !actuator.isAllowed(exchange)) {

            VerifyImageDTO verifyImage = verifyImageUtil.generateVerifyImg();
            verifyImageUtil.saveVerifyCodeToRedis(verifyImage);

            String str1 = this.antiReptileForm.replace("verifyId_value", verifyImage.getVerifyId());
            String str2 = str1.replaceAll("verifyImg_value", verifyImage.getVerifyImgStr());
            String str3 = str2.replaceAll("realRequestUri_value", requestUrl);

            return ResponseUtils.writeResponseTextHtml(
                    exchange, HttpStatus.LOOP_DETECTED, str3);
        }
        return chain.filter(exchange);
    }

    /**
     * 是否拦截
     *
     * @param requestUrl              请求uri
     * @param isAntiReptileAnnotation 是否有AntiReptile注解
     * @return 是否拦截
     */
    public boolean isIntercept(String requestUrl, Boolean isAntiReptileAnnotation) {
        if (this.globalFilterMode
                || isAntiReptileAnnotation
                || this.includeUrls.contains(requestUrl)) {
            return true;
        } else {
            for (String includeUrl : includeUrls) {
                if (Pattern.matches(includeUrl, requestUrl)) {
                    return true;
                }
            }
            return false;
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext)
            throws BeansException {}
}
