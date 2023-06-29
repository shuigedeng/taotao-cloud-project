/*
 * Copyright (c) 2020-2030 ZHENGGENGWEI(码匠君)<herodotus@aliyun.com>
 *
 * Dante Engine licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * 
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Dante Engine 采用APACHE LICENSE 2.0开源协议，您在使用过程中，需要注意以下几点：
 *
 * 1.请不要删除和修改根目录下的LICENSE文件。
 * 2.请不要删除和修改 Dante Cloud 源码头部的版权声明。
 * 3.请保留源码和相关描述文件的项目出处，作者声明等。
 * 4.分发源码时候，请注明软件出处 
 * 5.在修改包名，模块名称，项目代码等时，请注明软件出处 
 * 6.若您的项目无法满足以上几点，可申请商业授权
 */

package com.taotao.cloud.auth.biz.authentication.login.form.captcha;

import com.taotao.cloud.captcha.support.core.dto.Verification;
import com.taotao.cloud.captcha.support.core.exception.CaptchaHasExpiredException;
import com.taotao.cloud.captcha.support.core.exception.CaptchaIsEmptyException;
import com.taotao.cloud.captcha.support.core.exception.CaptchaMismatchException;
import com.taotao.cloud.captcha.support.core.exception.CaptchaParameterIllegalException;
import com.taotao.cloud.captcha.support.core.processor.CaptchaRendererFactory;
import com.taotao.cloud.security.springsecurity.core.definition.details.FormLoginWebAuthenticationDetails;
import com.taotao.cloud.security.springsecurity.core.exception.OAuth2CaptchaArgumentIllegalException;
import com.taotao.cloud.security.springsecurity.core.exception.OAuth2CaptchaHasExpiredException;
import com.taotao.cloud.security.springsecurity.core.exception.OAuth2CaptchaIsEmptyException;
import com.taotao.cloud.security.springsecurity.core.exception.OAuth2CaptchaMismatchException;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * <p>Description: OAuth2 (Security) 表单登录 Provider </p>
 * <p>
 * 扩展的OAuth2表单登录Provider，以支持表单登录的验证码
 *
 * 
 * @date : 2022/4/12 10:21
 * @see DaoAuthenticationProvider
 */
public class OAuth2FormLoginAuthenticationProvider extends DaoAuthenticationProvider {

    private static final Logger log = LoggerFactory.getLogger(OAuth2FormLoginAuthenticationProvider.class);

    private final CaptchaRendererFactory captchaRendererFactory;

    public OAuth2FormLoginAuthenticationProvider(CaptchaRendererFactory captchaRendererFactory) {
        super();
        this.captchaRendererFactory = captchaRendererFactory;
    }

    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
        Object details = authentication.getDetails();

        if (ObjectUtils.isNotEmpty(details) && details instanceof FormLoginWebAuthenticationDetails formLoginWebAuthenticationDetails) {
            if (!formLoginWebAuthenticationDetails.getClosed()) {
                String code = formLoginWebAuthenticationDetails.getCode();
                String category = formLoginWebAuthenticationDetails.getCategory();
                String identity = formLoginWebAuthenticationDetails.getIdentity();

                if (StringUtils.isBlank(code)) {
                    throw new OAuth2CaptchaIsEmptyException("Captcha is empty.");
                }

                try {
                    Verification verification = new Verification();
                    verification.setCharacters(code);
                    verification.setCategory(category);
                    verification.setIdentity(identity);
                    captchaRendererFactory.verify(verification);
                } catch (CaptchaParameterIllegalException e) {
                    throw new OAuth2CaptchaArgumentIllegalException("Captcha argument is illegal!");
                } catch (CaptchaHasExpiredException e) {
                    throw new OAuth2CaptchaHasExpiredException("Captcha is expired!");
                } catch (CaptchaMismatchException e) {
                    throw new OAuth2CaptchaMismatchException("Captcha is mismatch!");
                } catch (CaptchaIsEmptyException e) {
                    throw new OAuth2CaptchaIsEmptyException("Captcha is empty!");
                }
            }
        }

        super.additionalAuthenticationChecks(userDetails, authentication);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        //返回true后才会执行上面的authenticate方法,这步能确保authentication能正确转换类型
        boolean supports = (OAuth2FormLoginAuthenticationToken.class.isAssignableFrom(authentication));
        log.info("[Herodotus] |- Form Login Authentication is supports! [{}]", supports);
        return supports;
    }
}
