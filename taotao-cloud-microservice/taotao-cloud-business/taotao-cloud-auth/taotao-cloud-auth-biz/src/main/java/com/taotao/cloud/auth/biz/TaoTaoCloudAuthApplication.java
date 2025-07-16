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

package com.taotao.cloud.auth.biz;

import com.taotao.boot.core.startup.StartupSpringApplication;
import com.taotao.boot.data.jpa.extend.JpaExtendRepositoryFactoryBean;
import com.taotao.boot.security.spring.annotation.EnableSecurityConfiguration;
import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.envers.repository.config.EnableEnversRepositories;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisIndexedHttpSession;

/**
 * TaoTaoCloudAuthApplication
 *
 * <p>/oauth/authorize 授权端点
 * <p>/oauth/token 令牌端点
 * <p>/oauth/confirm_access 用户批准授权的端点
 * <p>/oauth/error 用于渲染授权服务器的错误
 * <p>/oauth/check_token 资源服务器解码access token
 * <p>/oauth/jwks 当使用JWT的时候，暴露公钥的端点
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
 * <p>
 * <p>
 * //http://127.0.0.1:33401/oauth2/authorize?client_id=67601992f3574c75809a3d79888bf16e&response_type=code&scope=message.read&redirect_uri=http%3A%2F%2F127.0.0.1%3A8090%2Fauthorized
 * //http://127.0.0.1:33401/oauth2/authorize?client_id=67601992f3574c75809a3d79888bf16e&response_type=code&scope=profile&state=46ge_TeI-dHuAnyv67nVmCcAmFgCVSZAqjTi9Om-1aA=&redirect_uri=http%3A%2F%2F192.168.101.10%3A8847%2Ftaotao-cloud-upms%2Fopen%2Fauthorized&code_challenge=KJlktPdfHdPPenXDN3HARjV6pzM7ljfHs-L-bFao3zM&code_challenge_method=S256
 * //http://127.0.0.1:33401/oauth2/authorize?client_id=67601992f3574c75809a3d79888bf16e&response_type=code&scope=profile,read-user-by-page&redirect_uri=http%3A%2F%2F192.168.101.10%3A8847%2Ftaotao-cloud-upms%2Fopen%2Fauthorized&code_challenge=GMBkW4F_Ap4Us75TZ7nDhSUd87HXAt7cLMG-R_2VGwE&code_challenge_method=S256
 *
 * @author shuigedeng
 * @version 2022.03
 * @since 2020/4/29 15:13
 */
@EnableEnversRepositories(
        basePackages = {
            "com.taotao.cloud.auth.biz.jpa.repository",
            "com.taotao.cloud.auth.biz.management.repository"
        })
@EntityScan(
        basePackages = {
            "com.taotao.cloud.auth.biz.jpa.entity",
            "com.taotao.cloud.auth.biz.management.entity"
        })
@EnableJpaRepositories(
        basePackages = {
            "com.taotao.cloud.auth.biz.jpa.repository",
            "com.taotao.cloud.auth.biz.management.repository"
        },
        repositoryFactoryBeanClass = JpaExtendRepositoryFactoryBean.class)
@EnableFeignClients(basePackages = {"com.taotao.cloud.*.api.feign"})
@EnableEncryptableProperties
@EnableDiscoveryClient
@ConfigurationPropertiesScan
@EnableSecurityConfiguration
@EnableRedisIndexedHttpSession
@SpringBootApplication
public class TaoTaoCloudAuthApplication {

    public static void main(String[] args) {
        new StartupSpringApplication(TaoTaoCloudAuthApplication.class)
                .setTtcBanner()
                .setTtcProfileIfNotExists("dev")
                .setTtcApplicationProperty("taotao-cloud-auth")
                .setTtcAllowBeanDefinitionOverriding(true)
                .run(args);
    }
}
