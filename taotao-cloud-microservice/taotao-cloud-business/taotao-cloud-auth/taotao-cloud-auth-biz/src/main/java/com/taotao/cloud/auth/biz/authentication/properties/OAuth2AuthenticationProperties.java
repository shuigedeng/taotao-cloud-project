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

package com.taotao.cloud.auth.biz.authentication.properties;

import com.google.common.base.MoreObjects;
import java.time.Duration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.AbstractRememberMeServices;
import org.springframework.security.web.authentication.ui.DefaultLoginPageGeneratingFilter;

/**
 * <p>OAuth2 合规性配置参数 </p>
 *
 * @since : 2022/7/7 0:16
 */
@ConfigurationProperties(prefix = OAuth2Constants.PROPERTY_OAUTH2_AUTHENTICATION)
public class OAuth2AuthenticationProperties {

    public static final String PREFIX = "taotao.cloud.security.oauth2.authentication";

    /**
     * 开启登录失败限制
     */
    private SignInFailureLimited signInFailureLimited = new SignInFailureLimited();

    /**
     * 同一终端登录限制
     */
    private SignInEndpointLimited signInEndpointLimited = new SignInEndpointLimited();

    /**
     * 账户踢出限制
     */
    private SignInKickOutLimited signInKickOutLimited = new SignInKickOutLimited();

    private FormLogin formLogin = new FormLogin();

    public SignInEndpointLimited getSignInEndpointLimited() {
        return signInEndpointLimited;
    }

    public void setSignInEndpointLimited( SignInEndpointLimited signInEndpointLimited ) {
        this.signInEndpointLimited = signInEndpointLimited;
    }

    public SignInFailureLimited getSignInFailureLimited() {
        return signInFailureLimited;
    }

    public void setSignInFailureLimited( SignInFailureLimited signInFailureLimited ) {
        this.signInFailureLimited = signInFailureLimited;
    }

    public SignInKickOutLimited getSignInKickOutLimited() {
        return signInKickOutLimited;
    }

    public void setSignInKickOutLimited( SignInKickOutLimited signInKickOutLimited ) {
        this.signInKickOutLimited = signInKickOutLimited;
    }

    public FormLogin getFormLogin() {
        return formLogin;
    }

    public void setFormLogin( FormLogin formLogin ) {
        this.formLogin = formLogin;
    }

    /**
     * SignInFailureLimited
     *
     * @author shuigedeng
     * @version 2026.01
     * @since 2025-12-19 09:30:45
     */
    public static class SignInFailureLimited {

        /**
         * 是否开启登录失败检测，默认开启
         */
        private Boolean enabled = true;

        /**
         * 允许允许最大失败次数
         */
        private Integer maxTimes = 5;

        /**
         * 是否自动解锁被锁定用户，默认开启
         */
        private Boolean autoUnlock = true;

        /**
         * 记录失败次数的缓存过期时间，默认：2小时。
         */
        private Duration expire = Duration.ofHours(2);

        public Boolean getEnabled() {
            return enabled;
        }

        public void setEnabled( Boolean enabled ) {
            this.enabled = enabled;
        }

        public Integer getMaxTimes() {
            return maxTimes;
        }

        public void setMaxTimes( Integer maxTimes ) {
            this.maxTimes = maxTimes;
        }

        public Duration getExpire() {
            return expire;
        }

        public void setExpire( Duration expire ) {
            this.expire = expire;
        }

        public Boolean getAutoUnlock() {
            return autoUnlock;
        }

        public void setAutoUnlock( Boolean autoUnlock ) {
            this.autoUnlock = autoUnlock;
        }

        @Override
        public String toString() {
            return MoreObjects.toStringHelper(this)
                    .add("enabled", enabled)
                    .add("maxTimes", maxTimes)
                    .add("autoUnlock", autoUnlock)
                    .add("expire", expire)
                    .toString();
        }
    }

    /**
     * SignInEndpointLimited
     *
     * @author shuigedeng
     * @version 2026.01
     * @since 2025-12-19 09:30:45
     */
    public static class SignInEndpointLimited {

        /**
         * 同一终端登录限制是否开启，默认开启。
         */
        private Boolean enabled = false;

        /**
         * 统一终端，允许同时登录的最大数量
         */
        private Integer maximum = 1;

        public Boolean getEnabled() {
            return enabled;
        }

        public void setEnabled( Boolean enabled ) {
            this.enabled = enabled;
        }

        public Integer getMaximum() {
            return maximum;
        }

        public void setMaximum( Integer maximum ) {
            this.maximum = maximum;
        }

        @Override
        public String toString() {
            return MoreObjects.toStringHelper(this)
                    .add("enabled", enabled)
                    .add("maximum", maximum)
                    .toString();
        }
    }

    /**
     * SignInKickOutLimited
     *
     * @author shuigedeng
     * @version 2026.01
     * @since 2025-12-19 09:30:45
     */
    public static class SignInKickOutLimited {

        /**
         * 是否开启 Session 踢出功能，默认开启
         */
        private Boolean enabled = true;

        public Boolean getEnabled() {
            return enabled;
        }

        public void setEnabled( Boolean enabled ) {
            this.enabled = enabled;
        }

        @Override
        public String toString() {
            return MoreObjects.toStringHelper(this).add("enabled", enabled).toString();
        }
    }

    /**
     * FormLogin
     *
     * @author shuigedeng
     * @version 2026.01
     * @since 2025-12-19 09:30:45
     */
    public static class FormLogin {

        /**
         * UI 界面用户名标输入框 name 属性值
         */
        private String usernameParameter =
                UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_USERNAME_KEY;

        /**
         * UI 界面密码标输入框 name 属性值
         */
        private String passwordParameter =
                UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_PASSWORD_KEY;

        /**
         * UI 界面Remember Me name 属性值
         */
        private String rememberMeParameter =
                AbstractRememberMeServices.SPRING_SECURITY_REMEMBER_ME_COOKIE_KEY;

        /**
         * UI 界面验证码 name 属性值
         */
        private String captchaParameter = "captcha";

        /**
         * 登录页面地址
         */
        private String loginPageUrl = DefaultLoginPageGeneratingFilter.DEFAULT_LOGIN_PAGE_URL;

        /**
         * 登录失败重定向地址
         */
        private String failureForwardUrl =
                loginPageUrl
                        + SymbolConstants.QUESTION
                        + DefaultLoginPageGeneratingFilter.ERROR_PARAMETER_NAME;

        /**
         * 登录成功重定向地址
         */
        private String successForwardUrl;

        /**
         * 关闭验证码显示，默认 false，显示
         */
        private Boolean closeCaptcha = false;

        /**
         * 验证码类别，默认为 Hutool Gif 类型
         */
        private String category = "HUTOOL_GIF";

        public String getUsernameParameter() {
            return usernameParameter;
        }

        public void setUsernameParameter( String usernameParameter ) {
            this.usernameParameter = usernameParameter;
        }

        public String getPasswordParameter() {
            return passwordParameter;
        }

        public void setPasswordParameter( String passwordParameter ) {
            this.passwordParameter = passwordParameter;
        }

        public String getRememberMeParameter() {
            return rememberMeParameter;
        }

        public void setRememberMeParameter( String rememberMeParameter ) {
            this.rememberMeParameter = rememberMeParameter;
        }

        public String getCaptchaParameter() {
            return captchaParameter;
        }

        public void setCaptchaParameter( String captchaParameter ) {
            this.captchaParameter = captchaParameter;
        }

        public String getLoginPageUrl() {
            return loginPageUrl;
        }

        public void setLoginPageUrl( String loginPageUrl ) {
            this.loginPageUrl = loginPageUrl;
        }

        public String getFailureForwardUrl() {
            return failureForwardUrl;
        }

        public void setFailureForwardUrl( String failureForwardUrl ) {
            this.failureForwardUrl = failureForwardUrl;
        }

        public String getSuccessForwardUrl() {
            return successForwardUrl;
        }

        public void setSuccessForwardUrl( String successForwardUrl ) {
            this.successForwardUrl = successForwardUrl;
        }

        public Boolean getCloseCaptcha() {
            return closeCaptcha;
        }

        public void setCloseCaptcha( Boolean closeCaptcha ) {
            this.closeCaptcha = closeCaptcha;
        }

        public String getCategory() {
            return category;
        }

        public void setCategory( String category ) {
            this.category = category;
        }

        @Override
        public String toString() {
            return MoreObjects.toStringHelper(this)
                    .add("usernameParameter", usernameParameter)
                    .add("passwordParameter", passwordParameter)
                    .add("rememberMeParameter", rememberMeParameter)
                    .add("captchaParameter", captchaParameter)
                    .add("loginPageUrl", loginPageUrl)
                    .add("failureForwardUrl", failureForwardUrl)
                    .add("successForwardUrl", successForwardUrl)
                    .add("closeCaptcha", closeCaptcha)
                    .add("category", category)
                    .toString();
        }
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("signInEndpointLimited", signInEndpointLimited)
                .add("signInFailureLimited", signInFailureLimited)
                .add("signInKickOutLimited", signInKickOutLimited)
                .toString();
    }
}
