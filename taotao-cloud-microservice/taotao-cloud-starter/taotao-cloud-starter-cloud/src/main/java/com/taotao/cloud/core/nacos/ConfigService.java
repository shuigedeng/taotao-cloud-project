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
package com.taotao.cloud.core.nacos;

import com.alibaba.nacos.api.config.ConfigType;
import com.alibaba.nacos.api.config.annotation.NacosConfigListener;
import com.taotao.cloud.common.utils.LogUtil;
import java.util.Properties;
import org.springframework.stereotype.Component;

/**
 * ConfigService
 *
 * @author dengtao
 * @version 1.0.0
 * @since 2021/04/06 11:20
 */
@Component
public class ConfigService {

	@NacosConfigListener(dataId = "taotao-cloud", type = ConfigType.YAML)
	public void onReceived(Properties value) {
		LogUtil.info("onReceived(Properties) : {}", value);
	}


}
