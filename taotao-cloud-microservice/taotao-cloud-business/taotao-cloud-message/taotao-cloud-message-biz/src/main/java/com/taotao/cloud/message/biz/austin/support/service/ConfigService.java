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

package com.taotao.cloud.message.biz.austin.support.service;

/**
 * 读取配置服务
 *
 * @author 3y
 */
public interface ConfigService {

    /**
     * 读取配置 1、当启动使用了apollo或者nacos，优先读取远程配置 2、当没有启动远程配置，读取本地 local.properties 配置文件的内容
     *
     * @param key
     * @param defaultValue
     * @return
     */
    String getProperty(String key, String defaultValue);
}
