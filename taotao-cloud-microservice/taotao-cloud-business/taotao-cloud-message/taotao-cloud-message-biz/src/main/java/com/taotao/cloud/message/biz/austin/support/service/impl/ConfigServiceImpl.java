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

package com.taotao.cloud.message.biz.austin.support.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.setting.dialect.Props;
import com.ctrip.framework.apollo.Config;
import com.taotao.cloud.message.biz.austin.support.service.ConfigService;
import com.taotao.cloud.message.biz.austin.support.utils.NacosUtils;
import java.nio.charset.StandardCharsets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @author 3y 读取配置实现类
 */
@Service
public class ConfigServiceImpl implements ConfigService {

    /** 本地配置 */
    private static final String PROPERTIES_PATH = "local.properties";

    private Props props = new Props(PROPERTIES_PATH, StandardCharsets.UTF_8);

    /** apollo配置 */
    @Value("${apollo.bootstrap.enabled}")
    private Boolean enableApollo;

    @Value("${apollo.bootstrap.namespaces}")
    private String namespaces;
    /** nacos配置 */
    @Value("${austin.nacos.enabled}")
    private Boolean enableNacos;

    @Autowired
    private NacosUtils nacosUtils;

    @Override
    public String getProperty(String key, String defaultValue) {
        if (enableApollo) {
            Config config = com.ctrip.framework.apollo.ConfigService.getConfig(
                    namespaces.split(StrUtil.COMMA)[0]);
            return config.getProperty(key, defaultValue);
        } else if (enableNacos) {
            return nacosUtils.getProperty(key, defaultValue);
        } else {
            return props.getProperty(key, defaultValue);
        }
    }
}
