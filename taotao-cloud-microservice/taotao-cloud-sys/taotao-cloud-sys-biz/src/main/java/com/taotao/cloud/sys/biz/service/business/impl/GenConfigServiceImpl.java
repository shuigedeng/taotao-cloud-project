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
package com.taotao.cloud.sys.biz.service.business.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.taotao.cloud.common.utils.lang.StringUtil;
import com.taotao.cloud.sys.biz.model.entity.config.GenConfig;
import com.taotao.cloud.sys.biz.mapper.IGenConfigMapper;
import com.taotao.cloud.sys.biz.service.business.IGenConfigService;
import java.io.File;
import org.springframework.stereotype.Service;

/**
 * GenConfigServiceImpl 
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2022-02-15 09:10:53
 */
@Service
//@CacheConfig(cacheNames = "genConfig")
public class GenConfigServiceImpl extends ServiceImpl<IGenConfigMapper, GenConfig> implements
	IGenConfigService {

	@Override
//    @Cacheable(key = "#p0")
	public GenConfig find(String tableName) {
		GenConfig genConfig = this.getOne(
			new LambdaQueryWrapper<GenConfig>().eq(GenConfig::getTableName, tableName));
		if (genConfig == null) {
			GenConfig config = new GenConfig();
			config.setTableName(tableName);
			return config;
		}
		return genConfig;
	}

	@Override
//    @CachePut(key = "#p0")
	public GenConfig update(String tableName, GenConfig genConfig) {
		// 如果 api 路径为空，则自动生成路径
		if (StringUtil.isBlank(genConfig.getApiPath())) {
			String separator = File.separator;
			String[] paths;
			String symbol = "\\";
			if (symbol.equals(separator)) {
				paths = genConfig.getPath().split("\\\\");
			} else {
				paths = genConfig.getPath().split(File.separator);
			}
			StringBuilder api = new StringBuilder();
			for (String path : paths) {
				api.append(path);
				api.append(separator);
				if ("src".equals(path)) {
					api.append("api");
					break;
				}
			}
			genConfig.setApiPath(api.toString());
		}
		this.saveOrUpdate(genConfig);
		return genConfig;
	}
}
