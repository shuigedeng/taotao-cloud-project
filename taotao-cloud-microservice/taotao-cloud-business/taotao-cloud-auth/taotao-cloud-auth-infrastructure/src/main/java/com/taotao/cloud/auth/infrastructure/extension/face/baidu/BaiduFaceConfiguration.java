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

package com.taotao.cloud.auth.infrastructure.extension.face.baidu;

import com.baidu.aip.face.AipFace;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({BaiduFaceProperties.class})
public class BaiduFaceConfiguration {

    @Autowired
    private BaiduFaceProperties baiduFaceProperties;

    @Bean
    public AipFace aipFace() {
        AipFace aipFace = new AipFace(
                baiduFaceProperties.getAppId(), baiduFaceProperties.getApiKey(), baiduFaceProperties.getSecretKey());
        aipFace.setConnectionTimeoutInMillis(60 * 1000);
        aipFace.setSocketTimeoutInMillis(60 * 1000);
        return aipFace;
    }
}
