/*
 * Copyright (c) 2020-2030 ZHENGGENGWEI(码匠君)<herodotus@aliyun.com>
 *
 * Dante Engine licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * <http://www.apache.org/licenses/LICENSE-2.0>
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
 * 4.分发源码时候，请注明软件出处 <https://gitee.com/herodotus/dante-engine>
 * 5.在修改包名，模块名称，项目代码等时，请注明软件出处 <https://gitee.com/herodotus/dante-engine>
 * 6.若您的项目无法满足以上几点，可申请商业授权
 */

package com.taotao.cloud.auth.biz.authentication.stamp;

import com.taotao.cloud.auth.biz.authentication.dto.SignInErrorStatus;
import com.taotao.cloud.auth.biz.authentication.properties.OAuth2AuthenticationProperties;
import com.taotao.cloud.security.springsecurity.core.constants.OAuth2Constants;
import org.apache.commons.lang3.ObjectUtils;
import org.dromara.hutool.crypto.SecureUtil;

/**
 * <p>Description: 登录失败次数限制签章管理 </p>
 *
 * @author : gengwei.zheng
 * @date : 2022/7/6 23:36
 */
public class SignInFailureLimitedStampManager  {

    private final OAuth2AuthenticationProperties authenticationProperties;

    public SignInFailureLimitedStampManager(OAuth2AuthenticationProperties authenticationProperties) {
//        super(OAuth2Constants.CACHE_NAME_TOKEN_SIGN_IN_FAILURE_LIMITED);
        this.authenticationProperties = authenticationProperties;
    }

//    @Override
    public Long nextStamp(String key) {
        return 1L;
    }

//    @Override
    public void afterPropertiesSet() throws Exception {
//        super.setExpire(authenticationProperties.getSignInFailureLimited().getExpire());
    }

    public OAuth2AuthenticationProperties getAuthenticationProperties() {
        return authenticationProperties;
    }

    public SignInErrorStatus errorStatus(String username) {
        int maxTimes = authenticationProperties.getSignInFailureLimited().getMaxTimes();
//        Long storedTimes = get(SecureUtil.md5(username));
        Long storedTimes = 20L;

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
