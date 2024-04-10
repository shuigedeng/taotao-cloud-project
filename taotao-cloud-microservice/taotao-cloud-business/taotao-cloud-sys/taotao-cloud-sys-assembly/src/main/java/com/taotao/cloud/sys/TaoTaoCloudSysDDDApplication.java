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

package com.taotao.cloud.sys;

import com.taotao.cloud.common.utils.common.PropertyUtils;
import com.taotao.cloud.core.startup.StartupSpringApplication;
import com.taotao.cloud.data.jpa.extend.JpaExtendRepositoryFactoryBean;
import com.taotao.cloud.web.annotation.TaoTaoCloudApplication;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * 系统管理中心
 * <p>异常原因：
 *
 * 自从 JDK9 中引入了模块化功能后，再到 JDK17，对于包扫描和反射的权限控制更加的严格。常见的库比如（Spring）大量用到包扫描和反射，所以常出现此错误。
 *
 * 解决方案：
 *
 * 一个粗暴的解决办法是将没开放的 module 强制对外开放，即保持和 Java9 之前的版本一致。
 *
 * --add-exports 导出包，意味着其中的所有公共类型和成员都可以在编译和运行时访问。
 * --add-opens 打开包，意味着其中的所有类型和成员（不仅是公共类型）都可以在运行时访问。
 * 主要区别在于 --add-opens 允许 “深度反射”，即非公共成员的访问，才可以调用 setAccessible(true)
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
 * @since 2020/11/30 下午3:33
 */
@MapperScan(basePackages = {"com.taotao.cloud.sys.infrastructure.persistent.*.mapper"})
@EnableJpaRepositories(
	basePackages = {"com.taotao.cloud.sys.infrastructure.persistent.*.repository.inf"},
	repositoryFactoryBeanClass = JpaExtendRepositoryFactoryBean.class)
//@ComponentScan(basePackages = {
//	"com.taotao.cloud.sys.biz.repository.cls"
//} )
//@EnableFeignClients(basePackages = {"com.taotao.cloud.sys.api.feign"})
@TaoTaoCloudApplication
public class TaoTaoCloudSysDDDApplication {

	public static void main(String[] args) {
		new StartupSpringApplication(TaoTaoCloudSysDDDApplication.class)
			.setTtcBanner()
			.setTtcProfileIfNotExists("dev")
			.setTtcApplicationProperty("taotao-cloud-sys-ddd")
			.setTtcAllowBeanDefinitionOverriding(true)
			.run(args);
	}
}
