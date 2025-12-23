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

package com.taotao.cloud.sa.just.biz;

import com.taotao.boot.common.utils.common.PropertyUtils;
//import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * TaoTaoCloudAuthBizApplication 默认url 作用
 *
 * <p>/oauth/authorize 授权端点
 *
 * <p>/oauth/token 令牌端点
 *
 * <p>/oauth/confirm_access 用户批准授权的端点
 *
 * <p>/oauth/error 用于渲染授权服务器的错误
 *
 * <p>/oauth/check_token 资源服务器解码access token
 *
 * <p>/oauth/jwks 当使用JWT的时候，暴露公钥的端点
 *
 * <p>
 *
 * <pre class="code">
 * --add-opens java.base/java.lang=ALL-UNNAMED
 * --add-opens java.base/java.lang.reflect=ALL-UNNAMED
 * --add-opens java.base/java.lang.invoke=ALL-UNNAMED
 * --add-opens java.base/java.util=ALL-UNNAMED
 * --add-opens java.base/sun.net=ALL-UNNAMED
 * --add-opens java.base/java.math=ALL-UNNAMED
 * --add-opens java.base/sun.reflect.annotation=ALL-UNNAMED
 * --add-opens java.base/sun.net=ALL-UNNAMED
 * --add-opens java.desktop/sun.awt=ALL-UNNAMED
 * --add-opens java.desktop/sun.font=ALL-UNNAMED
 * --add-opens jdk.management/com.sun.management.internal=ALL-UNNAMED
 * --add-exports java.desktop/sun.awt=ALL-UNNAMED
 * --add-exports java.desktop/sun.font=ALL-UNNAMED
 * </pre>
 *
 * @author shuigedeng
 * @version 2022.03
 * @since 2020/4/29 15:13
 */
// @EnableJpaRepositories(basePackages = {"com.taotao.cloud.auth.biz.repository"})
// @EnableFeignClients(basePackages = {"com.taotao.cloud.*.api.feign"})
//@EnableEncryptableProperties
@EnableDiscoveryClient
@SpringBootApplication
public class TaoTaoCloudSaJustApplication {

    public static void main(String[] args) {
        PropertyUtils.setDefaultProperty("taotao-cloud-sa-just");

        SpringApplication.run(TaoTaoCloudSaJustApplication.class, args);
    }
}
