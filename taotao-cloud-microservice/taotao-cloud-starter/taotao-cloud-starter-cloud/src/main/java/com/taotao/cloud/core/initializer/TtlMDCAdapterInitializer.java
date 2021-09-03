/*
 * Copyright 2002-2021 the original author or authors.
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
package com.taotao.cloud.core.initializer;

import com.taotao.cloud.common.constant.StarterName;
import com.taotao.cloud.common.utils.LogUtil;
import org.slf4j.TtlMDCAdapter;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.annotation.Order;

/**
 * 初始化TtlMDCAdapter实例，并替换MDC中的adapter对象
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-02 20:24:54
 */
@Order(1)
public class TtlMDCAdapterInitializer implements
	ApplicationContextInitializer<ConfigurableApplicationContext> {

	@Override
	public void initialize(ConfigurableApplicationContext applicationContext) {
		LogUtil.started(TtlMDCAdapterInitializer.class, StarterName.CLOUD_STARTER);

		if (!(applicationContext instanceof AnnotationConfigApplicationContext)) {
			TtlMDCAdapter.getInstance();
		}
	}
}
