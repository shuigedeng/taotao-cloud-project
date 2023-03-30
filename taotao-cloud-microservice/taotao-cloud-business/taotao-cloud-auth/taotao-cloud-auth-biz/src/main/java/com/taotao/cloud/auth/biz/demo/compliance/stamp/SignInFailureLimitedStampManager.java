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

package com.taotao.cloud.auth.biz.demo.compliance.stamp;

import cn.herodotus.engine.cache.jetcache.stamp.AbstractCountStampManager;
import cn.herodotus.engine.oauth2.compliance.dto.SignInErrorStatus;
import cn.herodotus.engine.oauth2.core.constants.OAuth2Constants;
import cn.herodotus.engine.oauth2.core.properties.OAuth2ComplianceProperties;
import cn.hutool.crypto.SecureUtil;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Description: 登录失败次数限制签章管理
 *
 * @author : gengwei.zheng
 * @date : 2022/7/6 23:36
 */
@Component
public class SignInFailureLimitedStampManager extends AbstractCountStampManager {

    private final OAuth2ComplianceProperties complianceProperties;

    @Autowired
    public SignInFailureLimitedStampManager(OAuth2ComplianceProperties complianceProperties) {
        super(OAuth2Constants.CACHE_NAME_TOKEN_SIGN_IN_FAILURE_LIMITED);
        this.complianceProperties = complianceProperties;
    }

    @Override
    public Long nextStamp(String key) {
        return 1L;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        super.setExpire(complianceProperties.getSignInFailureLimited().getExpire());
    }

    public OAuth2ComplianceProperties getComplianceProperties() {
        return complianceProperties;
    }

    public SignInErrorStatus errorStatus(String username) {
        int maxTimes = complianceProperties.getSignInFailureLimited().getMaxTimes();
        Long storedTimes = get(SecureUtil.md5(username));

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
}
