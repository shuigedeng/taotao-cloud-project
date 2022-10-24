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

import com.alibaba.nacos.client.config.impl.LocalConfigInfoProcessor;
import com.taotao.cloud.sys.api.model.vo.region.RegionTreeVO;
import com.taotao.cloud.web.annotation.TaoTaoCloudApplication;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.springframework.boot.SpringApplication;

/**
 * TaoTaoCloudSysApplication 抑制java9 module 报错
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
@TaoTaoCloudApplication
public class TaoTaoCloudSysApplication {

	public static void main(String[] args) {
		setNacosProperty();
		SpringApplication.run(TaoTaoCloudSysApplication.class, args);
	}

	public static void setNacosProperty() {
		/**
		 * 设置nacos客户端日志和快照目录
		 *
		 * @see LocalConfigInfoProcessor
		 */
		String userHome = System.getProperty("user.home");
		System.setProperty("JM.LOG.PATH",
			userHome + File.separator + "logs" + File.separator + "taotao-cloud-sys");
		System.setProperty("JM.SNAPSHOT.PATH",
			userHome + File.separator + "logs" + File.separator + "taotao-cloud-sys");
		System.setProperty("nacos.logging.default.config.enabled", "true");
	}


	public static void aa(List<RegionTreeVO> data) {
		data.stream().reduce((prev, cure) -> {
			List<RegionTreeVO> regionTreeVOS = new ArrayList<>();
			regionTreeVOS.add(cure);

			cure.getChildren().forEach(v -> {

			});


		});
	}

}
