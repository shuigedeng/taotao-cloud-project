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

package com.taotao.cloud.message.biz.austin.web.utils;

import com.taotao.cloud.message.biz.austin.common.constant.CommonConstant;
import com.taotao.cloud.message.biz.austin.common.constant.OfficialAccountParamConstant;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * @author 3y
 * @date 2022/12/22 微信服务号登录的Utils
 */
@Component
@Slf4j
public class LoginUtils {

    @Autowired private ApplicationContext applicationContext;

    @Value("${spring.profiles.active}")
    private String env;

    /**
     * 测试环境 使用 获取 WeChatLoginConfig 对象
     *
     * @return
     */
    public WeChatLoginConfig getLoginConfig() {
        try {
            return applicationContext.getBean(
                    OfficialAccountParamConstant.WE_CHAT_LOGIN_CONFIG, WeChatLoginConfig.class);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 测试环境使用 判断是否需要登录
     *
     * @return
     */
    public boolean needLogin() {
        try {
            WeChatLoginConfig bean =
                    applicationContext.getBean(
                            OfficialAccountParamConstant.WE_CHAT_LOGIN_CONFIG,
                            WeChatLoginConfig.class);
            if (CommonConstant.ENV_TEST.equals(env) && Objects.nonNull(bean)) {
                return true;
            }
        } catch (Exception e) {
        }
        return false;
    }
}
