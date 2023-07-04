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

import com.taotao.cloud.auth.biz.authentication.properties.OAuth2AuthenticationProperties;
import com.taotao.cloud.cache.redis.repository.RedisRepository;
import com.taotao.cloud.captcha.support.core.definition.AbstractRenderer;
import com.taotao.cloud.captcha.support.core.definition.domain.Metadata;
import com.taotao.cloud.captcha.support.core.dto.Captcha;
import com.taotao.cloud.captcha.support.core.dto.Verification;
import com.taotao.cloud.security.springsecurity.core.constants.OAuth2Constants;
import java.time.Duration;
import org.dromara.hutool.core.data.id.IdUtil;
import org.springframework.beans.factory.InitializingBean;

/**
 * <p>Description: 锁定账户签章管理 </p>
 *
 *
 * @date : 2022/7/8 21:27
 */
public class LockedUserDetailsStampManager extends AbstractRenderer implements InitializingBean {
    private final OAuth2AuthenticationProperties authenticationProperties;

    public LockedUserDetailsStampManager(
            RedisRepository redisRepository, OAuth2AuthenticationProperties authenticationProperties) {
        super(redisRepository, OAuth2Constants.CACHE_NAME_TOKEN_LOCKED_USER_DETAIL);
        this.authenticationProperties = authenticationProperties;
    }

    public LockedUserDetailsStampManager(
            RedisRepository redisRepository, Duration expire, OAuth2AuthenticationProperties authenticationProperties) {
        super(redisRepository, OAuth2Constants.CACHE_NAME_TOKEN_LOCKED_USER_DETAIL, expire);
        this.authenticationProperties = authenticationProperties;
    }

    @Override
    public String nextStamp(String key) {
        return IdUtil.fastSimpleUUID();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        super.setExpire(authenticationProperties.getSignInFailureLimited().getExpire());
    }

    @Override
    public Metadata draw() {
        return null;
    }

    @Override
    public Captcha getCapcha(String key) {
        return null;
    }

    @Override
    public boolean verify(Verification verification) {
        return false;
    }

    @Override
    public String getCategory() {
        return null;
    }
}
