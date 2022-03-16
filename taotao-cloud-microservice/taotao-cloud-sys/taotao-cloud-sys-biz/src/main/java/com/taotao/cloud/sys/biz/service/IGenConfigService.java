/**
 * Copyright (C) 2018-2020 All rights reserved, Designed By www.yixiang.co 注意：
 * 本软件为www.yixiang.co开发研制
 */
package com.taotao.cloud.sys.biz.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.taotao.cloud.sys.biz.entity.config.GenConfig;

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
	 * @param tableName 表名
	 * @return 表配置
	 */
	GenConfig find(String tableName);

	/**
	 * 更新表配置
	 * @param tableName 表名
	 * @param genConfig 表配置
	 * @return 表配置
	 */
	GenConfig update(String tableName, GenConfig genConfig);
}
