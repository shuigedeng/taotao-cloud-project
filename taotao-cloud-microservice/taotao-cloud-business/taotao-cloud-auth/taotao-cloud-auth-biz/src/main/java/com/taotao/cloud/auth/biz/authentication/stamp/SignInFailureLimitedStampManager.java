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

package com.taotao.cloud.auth.biz.authentication.stamp;

import com.taotao.boot.cache.redis.repository.RedisRepository;
import com.taotao.boot.captcha.support.core.definition.domain.Metadata;
import com.taotao.boot.captcha.support.core.dto.Captcha;
import com.taotao.boot.captcha.support.core.dto.Verification;
import com.taotao.boot.security.spring.constants.OAuth2Constants;
import com.taotao.cloud.auth.api.model.dto.SignInErrorStatus;
import com.taotao.cloud.auth.biz.authentication.properties.OAuth2AuthenticationProperties;
import java.time.Duration;
import org.apache.commons.lang3.ObjectUtils;
import org.dromara.hutool.crypto.SecureUtil;
import org.springframework.beans.factory.InitializingBean;

/**
 * <p>登录失败次数限制签章管理 </p>
 *
 * @author shuigedeng
 * @version 2023.07
 * @since 2023-07-10 17:25:15
 */
public class SignInFailureLimitedStampManager extends AbstractCountStampManager
        implements InitializingBean {

    /**
     * 身份验证属性
     */
    private final OAuth2AuthenticationProperties authenticationProperties;

    /**
     * 登录失败有限邮票经理
     *
     * @param redisRepository          redis存储库
     * @param authenticationProperties 身份验证属性
     * @return
     * @since 2023-07-10 17:25:15
     */
    public SignInFailureLimitedStampManager(
            RedisRepository redisRepository,
            OAuth2AuthenticationProperties authenticationProperties) {
        super(redisRepository, OAuth2Constants.CACHE_NAME_TOKEN_SIGN_IN_FAILURE_LIMITED);
        this.authenticationProperties = authenticationProperties;
    }

    /**
     * 登录失败有限邮票经理
     *
     * @param redisRepository          redis存储库
     * @param expire                   过期
     * @param authenticationProperties 身份验证属性
     * @return
     * @since 2023-07-10 17:25:15
     */
    public SignInFailureLimitedStampManager(
            RedisRepository redisRepository,
            Duration expire,
            OAuth2AuthenticationProperties authenticationProperties) {
        super(redisRepository, OAuth2Constants.CACHE_NAME_TOKEN_SIGN_IN_FAILURE_LIMITED, expire);
        this.authenticationProperties = authenticationProperties;
    }

    /**
     * 下一枚邮票
     *
     * @param key 钥匙
     * @return {@link Long }
     * @since 2023-07-10 17:25:15
     */
    @Override
    public Long nextStamp(String key) {
        return 1L;
    }

    /**
     * 属性设置后
     *
     * @since 2023-07-10 17:25:15
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        super.setExpire(authenticationProperties.getSignInFailureLimited().getExpire());
    }

    /**
     * 获取身份验证属性
     *
     * @return {@link OAuth2AuthenticationProperties }
     * @since 2023-07-10 17:25:15
     */
    public OAuth2AuthenticationProperties getAuthenticationProperties() {
        return authenticationProperties;
    }

    /**
     * 错误状态
     *
     * @param username 用户名
     * @return {@link SignInErrorStatus }
     * @since 2023-07-10 17:25:16
     */
    public SignInErrorStatus errorStatus(String username) {
        int maxTimes = authenticationProperties.getSignInFailureLimited().getMaxTimes();
        Long storedTimes = (Long) get(SecureUtil.md5(username));

        int errorTimes = 0;
        if (ObjectUtils.isNotEmpty(storedTimes)) {
            errorTimes = storedTimes.intValue();
        }

        int remainTimes = maxTimes;
        if (errorTimes != 0) {
            remainTimes = maxTimes - errorTimes;
        }

        boolean isLocked = false;
        if (errorTimes == maxTimes) {
            isLocked = true;
        }

        SignInErrorStatus status = new SignInErrorStatus();
        status.setErrorTimes(errorTimes);
        status.setRemainTimes(remainTimes);
        status.setLocked(isLocked);

        return status;
    }

    /**
     * 抽奖
     *
     * @return {@link Metadata }
     * @since 2023-07-10 17:25:16
     */
    @Override
    public Metadata draw() {
        return null;
    }

    /**
     * 获取capcha
     *
     * @param key 钥匙
     * @return {@link Captcha }
     * @since 2023-07-10 17:25:16
     */
    @Override
    public Captcha getCapcha(String key) {
        return null;
    }

    /**
     * 验证
     *
     * @param verification 验证
     * @return boolean
     * @since 2023-07-10 17:25:16
     */
    @Override
    public boolean verify(Verification verification) {
        return false;
    }

    /**
     * 获取类别
     *
     * @return {@link String }
     * @since 2023-07-10 17:25:16
     */
    @Override
    public String getCategory() {
        return null;
    }
}
