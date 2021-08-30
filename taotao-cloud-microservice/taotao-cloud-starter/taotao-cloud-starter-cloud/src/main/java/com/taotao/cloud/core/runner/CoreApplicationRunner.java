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
package com.taotao.cloud.core.runner;

import static com.taotao.cloud.core.properties.CoreProperties.SpringApplicationName;

import com.taotao.cloud.common.utils.LogUtil;
import com.taotao.cloud.core.utils.PropertyUtil;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;

/**
 * 容器生命周期监听程序
 */
@Order
public class CoreApplicationRunner implements ApplicationRunner {

	@Override
	public void run(ApplicationArguments var1) throws Exception {
		saveStatus("STARTED");
	}

	private void saveStatus(String status) {
		HashMap<String, Object> map = new HashMap<>(2);
		map.put("data", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		map.put("state", status);
		LogUtil.info(PropertyUtil.getProperty(SpringApplicationName) + "-- 应用已正常启动!");
	}

}
