/**
 * Copyright (C) 2018-2020 All rights reserved, Designed By www.yixiang.co 注意：
 * 本软件为www.yixiang.co开发研制
 */
package com.taotao.cloud.sys.biz.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.taotao.cloud.common.utils.StringUtil;
import com.taotao.cloud.sys.biz.entity.GenConfig;
import com.taotao.cloud.sys.biz.mapper.GenConfigMapper;
import com.taotao.cloud.sys.biz.service.GenConfigService;
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
public class GenConfigServiceImpl extends ServiceImpl<GenConfigMapper, GenConfig> implements
	GenConfigService {

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
