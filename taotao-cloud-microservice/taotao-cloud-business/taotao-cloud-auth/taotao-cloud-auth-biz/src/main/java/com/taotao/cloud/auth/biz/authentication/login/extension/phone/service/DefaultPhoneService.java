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

package com.taotao.cloud.auth.biz.authentication.login.extension.phone.service;

import com.taotao.cloud.message.api.feign.IFeignNoticeMessageApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 默认电话服务
 *
 * @author shuigedeng
 * @version 2023.04
 * @since 2023-06-25 17:06:29
 */
@Service
public class DefaultPhoneService implements PhoneService {

    @Autowired
    private IFeignNoticeMessageApi feignNoticeMessageApi;

    @Override
    public boolean verifyCaptcha(String phone, String rawCode) {
        // 校验短信验证码
        return false;
    }
}