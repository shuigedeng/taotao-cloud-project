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

import cn.herodotus.engine.cache.jetcache.stamp.AbstractStampManager;
import cn.herodotus.engine.oauth2.core.constants.OAuth2Constants;
import cn.herodotus.engine.oauth2.core.properties.OAuth2ComplianceProperties;
import cn.hutool.core.util.IdUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Description: 锁定账户签章管理
 *
 * @author : gengwei.zheng
 * @date : 2022/7/8 21:27
 */
@Component
public class LockedUserDetailsStampManager extends AbstractStampManager<String, String> {

    private final OAuth2ComplianceProperties complianceProperties;

    @Autowired
    public LockedUserDetailsStampManager(OAuth2ComplianceProperties complianceProperties) {
        super(OAuth2Constants.CACHE_NAME_TOKEN_LOCKED_USER_DETAIL);
        this.complianceProperties = complianceProperties;
    }

    @Override
    public String nextStamp(String key) {
        return IdUtil.fastSimpleUUID();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        super.setExpire(complianceProperties.getSignInFailureLimited().getExpire());
    }
}
