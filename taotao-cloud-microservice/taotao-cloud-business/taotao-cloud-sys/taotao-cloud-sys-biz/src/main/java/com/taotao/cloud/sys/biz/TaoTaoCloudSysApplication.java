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

package com.taotao.cloud.sys.biz;

import com.taotao.boot.core.startup.StartupSpringApplication;
import com.taotao.boot.web.annotation.TaoTaoBootApplication;
import com.taotao.cloud.bootstrap.annotation.TaoTaoCloudApplication;
import org.dromara.x.file.storage.spring.EnableFileStorage;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;

/**
 * 系统管理中心
 * <p>异常原因：
 * <p>
 * 自从 JDK9 中引入了模块化功能后，再到 JDK17，对于包扫描和反射的权限控制更加的严格。常见的库比如（Spring）大量用到包扫描和反射，所以常出现此错误。
 * <p>
 * 解决方案：
 * <p>
 * 一个粗暴的解决办法是将没开放的 module 强制对外开放，即保持和 Java9 之前的版本一致。
 * <p>
 * --add-exports 导出包，意味着其中的所有公共类型和成员都可以在编译和运行时访问。 --add-opens 打开包，意味着其中的所有类型和成员（不仅是公共类型）都可以在运行时访问。
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
@ComponentScan(basePackages = {
	"com.taotao.cloud.sys.biz.repository.cls"
})
@TaoTaoBootApplication
@TaoTaoCloudApplication
@EnableFileStorage
public class TaoTaoCloudSysApplication extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(TaoTaoCloudSysApplication.class);
	}

	public static void main(String[] args) {
		new StartupSpringApplication(TaoTaoCloudSysApplication.class)
			.setTtcBanner()
			.setTtcProfileIfNotExists("dev")
			.setTtcApplicationProperty("taotao-cloud-sys")
			//.setTtcAllowBeanDefinitionOverriding(true)
			.run(args);
	}
}
