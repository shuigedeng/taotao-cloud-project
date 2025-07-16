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
import com.taotao.boot.captcha.support.core.definition.AbstractRenderer;
import com.taotao.boot.captcha.support.core.definition.domain.Metadata;
import com.taotao.boot.captcha.support.core.dto.Captcha;
import com.taotao.boot.captcha.support.core.dto.Verification;
import com.taotao.boot.security.spring.constants.OAuth2Constants;
import com.taotao.cloud.auth.biz.authentication.properties.OAuth2AuthenticationProperties;
import java.time.Duration;
import org.dromara.hutool.core.data.id.IdUtil;
import org.springframework.beans.factory.InitializingBean;

/**
 * <p>锁定账户签章管理 </p>
 *
 * @author shuigedeng
 * @version 2023.07
 * @since 2023-07-10 17:25:06
 */
public class LockedUserDetailsStampManager extends AbstractRenderer implements InitializingBean {
    /**
     * 身份验证属性
     */
    private final OAuth2AuthenticationProperties authenticationProperties;

    /**
     * 锁定用户详细信息邮票管理器
     *
     * @param redisRepository          redis存储库
     * @param authenticationProperties 身份验证属性
     * @return
     * @since 2023-07-10 17:25:06
     */
    public LockedUserDetailsStampManager(
            RedisRepository redisRepository,
            OAuth2AuthenticationProperties authenticationProperties) {
        super(redisRepository, OAuth2Constants.CACHE_NAME_TOKEN_LOCKED_USER_DETAIL);
        this.authenticationProperties = authenticationProperties;
    }

    /**
     * 锁定用户详细信息邮票管理器
     *
     * @param redisRepository          redis存储库
     * @param expire                   过期
     * @param authenticationProperties 身份验证属性
     * @return
     * @since 2023-07-10 17:25:06
     */
    public LockedUserDetailsStampManager(
            RedisRepository redisRepository,
            Duration expire,
            OAuth2AuthenticationProperties authenticationProperties) {
        super(redisRepository, OAuth2Constants.CACHE_NAME_TOKEN_LOCKED_USER_DETAIL, expire);
        this.authenticationProperties = authenticationProperties;
    }

    /**
     * 下一枚邮票
     *
     * @param key 钥匙
     * @return {@link String }
     * @since 2023-07-10 17:25:06
     */
    @Override
    public String nextStamp(String key) {
        return IdUtil.fastSimpleUUID();
    }

    /**
     * 属性设置后
     *
     * @since 2023-07-10 17:25:06
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        super.setExpire(authenticationProperties.getSignInFailureLimited().getExpire());
    }

    /**
     * 抽奖
     *
     * @return {@link Metadata }
     * @since 2023-07-10 17:25:07
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
     * @since 2023-07-10 17:25:07
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
     * @since 2023-07-10 17:25:08
     */
    @Override
    public boolean verify(Verification verification) {
        return false;
    }

    /**
     * 获取类别
     *
     * @return {@link String }
     * @since 2023-07-10 17:25:08
     */
    @Override
    public String getCategory() {
        return null;
    }
}
