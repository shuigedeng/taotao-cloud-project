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

package com.taotao.cloud.member.biz;

import com.taotao.boot.common.utils.common.PropertyUtils;
import com.taotao.boot.core.startup.StartupSpringApplication;
import com.taotao.boot.web.annotation.TaoTaoBootApplication;
import com.taotao.cloud.bootstrap.annotation.TaoTaoCloudApplication;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * @author shuigedeng
 * @version 2022.03
 * @since 2020/11/20 下午3:47
 */
//@MapperScan(basePackages = "com.taotao.cloud.sys.biz.mapper")
//@EnableJpaRepositories(basePackages = "com.taotao.cloud.member.biz.repository")
@TaoTaoBootApplication
@TaoTaoCloudApplication
public class TaoTaoCloudMemberApplication {

	public static void main(String[] args) {
		new StartupSpringApplication(TaoTaoCloudMemberApplication.class)
			.setTtcBanner()
			.setTtcProfileIfNotExists("dev")
			.setTtcApplicationProperty("taotao-cloud-member")
			//.setTtcAllowBeanDefinitionOverriding(true)
			.run(args);
	}
}
