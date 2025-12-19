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

package com.taotao.cloud.rpc.common.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import lombok.extern.slf4j.Slf4j;

/**
 * ResourceLoadUtils
 *
 * @author shuigedeng
 * @version 2026.01
 * @since 2025-12-19 09:30:45
 */
@Slf4j
public class ResourceLoadUtils {

    private static Map<String, String> resourceLoaders = new HashMap<>();

    /**
     * 加载 Jar 包外置配置文件 找不到返回 null
     */
    public static Map<String, String> load( String resourceName ) {
        // 2.1 创建Properties对象
        Properties p = new Properties();
        // 2.2 调用p对象中的load方法进行配置文件的加载
        // 使用InPutStream流读取properties文件
        String currentWorkPath = System.getProperty("user.dir");
        InputStream is = null;
        String propertyValue = "";
        try (BufferedReader bufferedReader =
                new BufferedReader(new FileReader(currentWorkPath + "/config/" + resourceName));) {
            p.load(bufferedReader);
            Enumeration<Object> keys = p.keys();
            while (keys.hasMoreElements()) {
                String property = (String) keys.nextElement();
                propertyValue = p.getProperty(property);
                log.info(
                        "discover key: {}, value: {} in {}", property, propertyValue, resourceName);
                resourceLoaders.put(property, propertyValue);
            }
            log.info(
                    "read resource from resource path: {}",
                    currentWorkPath + "/config/" + resourceName);
            return resourceLoaders;
        } catch (IOException e) {
            log.info(
                    "not found resource from resource path: {}",
                    currentWorkPath + "/config/" + resourceName);
            return null;
        }
    }
}
