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

package com.taotao.cloud.generator.generator.util;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Get Value From Application.yml
 * @author zhengkai.blog.csdn.net
 */
@Data
@Component
public class ValueUtil {
    @Value("${OEM.title}")
    public String title;

    @Value("${OEM.header}")
    public String header;

    @Value("${OEM.version}")
    public String version;

    @Value("${OEM.author}")
    public String author;

    @Value("${OEM.keywords}")
    public String keywords;

    @Value("${OEM.slogan}")
    public String slogan;

    @Value("${OEM.copyright}")
    public String copyright;

    @Value("${OEM.description}")
    public String description;

    @Value("${OEM.packageName}")
    public String packageName;

    @Value("${OEM.returnUtilSuccess}")
    public String returnUtilSuccess;

    @Value("${OEM.returnUtilFailure}")
    public String returnUtilFailure;

    @Value("${OEM.outputStr}")
    public String outputStr;

    @Value("${OEM.mode}")
    public String mode;
}
