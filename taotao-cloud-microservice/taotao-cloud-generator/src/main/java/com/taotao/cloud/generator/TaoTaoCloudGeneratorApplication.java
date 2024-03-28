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

package com.taotao.cloud.generator;

import com.taotao.cloud.common.utils.common.PropertyUtils;
import com.taotao.cloud.core.startup.StartupSpringApplication;
import com.taotao.cloud.web.annotation.TaoTaoCloudApplication;
import net.maku.generator.autoconfigure.GeneratorAutoConfiguration;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.Import;

/**
 * 代码生成器管理中心
 *
 * @author shuigedeng
 * @version 2023.04
 * @since 2023-05-11 13:55:49
 */
@Import(GeneratorAutoConfiguration.class)
@MapperScan(basePackages = {"net.maku.generator.dao"})
@TaoTaoCloudApplication
public class TaoTaoCloudGeneratorApplication {

    public static void main(String[] args) {
		new StartupSpringApplication(TaoTaoCloudGeneratorApplication.class)
			.setTtcBanner()
			.setTtcProfileIfNotExists("dev")
			.setTtcApplicationProperty("taotao-cloud-generator")
			.setTtcAllowBeanDefinitionOverriding(true)
			.run(args);
    }
}
