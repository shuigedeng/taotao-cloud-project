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
package com.taotao.cloud.sys.biz.service.business;


import com.baomidou.mybatisplus.extension.service.IService;
import com.taotao.cloud.sys.biz.model.entity.config.GenConfig;

/**
 * GenConfigService
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2022-02-15 09:12:37
 */
public interface IGenConfigService extends IService<GenConfig> {

	/**
	 * 查询表配置
	 *
	 * @param tableName 表名
	 * @return 表配置
	 */
	GenConfig find(String tableName);

	/**
	 * 更新表配置
	 *
	 * @param tableName 表名
	 * @param genConfig 表配置
	 * @return 表配置
	 */
	GenConfig update(String tableName, GenConfig genConfig);
}
