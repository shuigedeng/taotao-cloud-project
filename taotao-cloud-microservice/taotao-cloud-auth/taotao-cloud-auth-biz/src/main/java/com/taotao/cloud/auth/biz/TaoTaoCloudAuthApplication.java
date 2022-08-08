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

import com.alibaba.nacos.client.config.impl.LocalConfigInfoProcessor;
import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import java.io.File;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.apache.dubbo.common.serialize.kryo.utils.KryoUtils;
/**
 * TaoTaoCloudAuthBizApplication
 * 默认url
 * 作用
 *
 * /oauth/authorize
 * 授权端点
 * /oauth/token
 * 令牌端点
 * /oauth/confirm_access
 * 用户批准授权的端点
 * /oauth/error
 * 用于渲染授权服务器的错误
 * /oauth/check_token
 * 资源服务器解码access token
 * /oauth/check_token
 *
 * 当使用JWT的时候，暴露公钥的端点
 *
 * 抑制java9 module 报错
 * --add-opens java.base/java.lang=ALL-UNNAMED
 * --add-opens java.base/java.lang.reflect=ALL-UNNAMED
 * --add-opens java.base/java.lang.invoke=ALL-UNNAMED
 * --add-opens java.base/java.util=ALL-UNNAMED
 * --add-opens jdk.management/com.sun.management.internal=ALL-UNNAMED
 * --add-opens java.base/java.math=ALL-UNNAMED
 * --add-opens java.base/sun.reflect.annotation=ALL-UNNAMED
 * --add-exports java.desktop/sun.awt=ALL-UNNAMED
 * --add-exports java.desktop/sun.font=ALL-UNNAMED
 * --add-opens java.desktop/sun.awt=ALL-UNNAMED
 * --add-opens java.desktop/sun.font=ALL-UNNAMED
 *
 * @author shuigedeng
 * @version 2022.03
 * @since 2020/4/29 15:13
 */
@EnableJpaRepositories(basePackages = {"com.taotao.cloud.auth.biz.repository", "com.taotao.cloud.auth.biz.idserver.repository"})
@EnableFeignClients(basePackages = {"com.taotao.cloud.*.api.feign"})
@EnableEncryptableProperties
@EnableDiscoveryClient
@SpringBootApplication
public class TaoTaoCloudAuthApplication {

	public static void main(String[] args) {
		setNacosProperty();

		SpringApplication.run(TaoTaoCloudAuthApplication.class, args);
	}


	public static void setNacosProperty(){
		/**
		 * 设置nacos客户端日志和快照目录
		 *
		 * @see LocalConfigInfoProcessor
		 */
		String userHome = System.getProperty("user.home");
		System.setProperty("JM.LOG.PATH", userHome + File.separator + "logs" + File.separator + "taotao-cloud-auth");
		System.setProperty("JM.SNAPSHOT.PATH", userHome + File.separator + "logs" + File.separator + "taotao-cloud-auth");
		System.setProperty("nacos.logging.default.config.enabled", "false");
	}
}
