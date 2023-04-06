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

package com.taotao.cloud.message.biz.austin.support.utils;

import cn.hutool.core.util.StrUtil;
import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.PropertyKeyConst;
import com.alibaba.nacos.api.exception.NacosException;
import java.io.StringReader;
import java.util.Properties;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * @program: austin
 * @description:
 * @author: Giorno
 * @create: 2022-07-28
 */
@Slf4j
@Component
public class NacosUtils {

    @Value("${austin.nacos.server}")
    private String nacosServer;

    @Value("${austin.nacos.username}")
    private String nacosUsername;

    @Value("${austin.nacos.password}")
    private String nacosPassword;

    @Value("${austin.nacos.group}")
    private String nacosGroup;

    @Value("${austin.nacos.dataId}")
    private String nacosDataId;

    @Value("${austin.nacos.namespace}")
    private String nacosNamespace;

    private final Properties request = new Properties();
    private final Properties properties = new Properties();

    public String getProperty(String key, String defaultValue) {
        try {
            String property = this.getContext();
            if (StringUtils.hasText(property)) {
                properties.load(new StringReader(property));
            }
        } catch (Exception e) {
            log.error("Nacos error:{}", ExceptionUtils.getStackTrace(e));
        }
        String property = properties.getProperty(key);
        return StrUtil.isBlank(property) ? defaultValue : property;
    }

    private String getContext() {
        String context = null;
        try {
            request.put(PropertyKeyConst.SERVER_ADDR, nacosServer);
            request.put(PropertyKeyConst.NAMESPACE, nacosNamespace);
            request.put(PropertyKeyConst.USERNAME, nacosUsername);
            request.put(PropertyKeyConst.PASSWORD, nacosPassword);
            context = NacosFactory.createConfigService(request).getConfig(nacosDataId, nacosGroup, 5000);
        } catch (NacosException e) {
            log.error("Nacos error:{}", ExceptionUtils.getStackTrace(e));
        }
        return context;
    }
}
