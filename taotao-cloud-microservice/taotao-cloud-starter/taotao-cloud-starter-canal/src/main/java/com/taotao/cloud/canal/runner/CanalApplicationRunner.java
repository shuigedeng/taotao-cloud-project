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
package com.taotao.cloud.canal.runner;

import com.taotao.cloud.canal.interfaces.CanalClient;
import com.taotao.cloud.common.utils.log.LogUtil;
import java.util.Objects;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;

/**
 * CanalApplicationRunner
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-03 20:53:00
 */
public class CanalApplicationRunner implements ApplicationRunner {

	/**
	 * canalClient
 	 */
	private final CanalClient canalClient;

	public CanalApplicationRunner(CanalClient canalClient) {
		this.canalClient = canalClient;
	}

	@Override
	public void run(ApplicationArguments applicationArguments) throws Exception {
		if (Objects.nonNull(canalClient)) {
			try {
				LogUtil.info(" CanalClient 正在尝试开启 canal 客户端....");

				canalClient.start();

				LogUtil.info(" CanalClient 启动 canal 客户端成功....");
			} catch (Exception e) {
				LogUtil.error(e);
			}
		}
	}
}
