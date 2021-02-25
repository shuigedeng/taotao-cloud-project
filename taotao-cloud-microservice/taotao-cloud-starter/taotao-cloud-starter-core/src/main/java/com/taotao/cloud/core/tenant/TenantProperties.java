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
package com.taotao.cloud.core.tenant;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;


/**
 * 多租户配置
 *
 * @author dengtao
 * @version 1.0.0
 * @since 2020/5/15 10:45
 */
@Setter
@Getter
@ConfigurationProperties(prefix = "taotao.cloud.tenant")
@RefreshScope
public class TenantProperties {

	/**
	 * 是否开启多租户
	 */
	private Boolean enable = false;

	/**
	 * 配置不进行多租户隔离的表名
	 */
	private List<String> ignoreTables = new ArrayList<>();

	/**
	 * 配置不进行多租户隔离的sql 需要配置mapper的全路径如：com.central.user.mapper.SysUserMapper.findList
	 */
	private List<String> ignoreSqls = new ArrayList<>();
}
