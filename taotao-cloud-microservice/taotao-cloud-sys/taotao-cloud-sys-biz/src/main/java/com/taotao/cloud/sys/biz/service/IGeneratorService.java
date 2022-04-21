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
package com.taotao.cloud.sys.biz.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.taotao.cloud.sys.biz.entity.config.ColumnConfig;
import com.taotao.cloud.sys.biz.entity.config.GenConfig;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.scheduling.annotation.Async;

public interface IGeneratorService extends IService<ColumnConfig> {

	/**
	 * ColumnInfo 查询数据库元数据
	 *
	 * @param name 表名
	 * @param page 分页页码
	 * @param size 分页大小
	 * @return 数据库数据
	 */
	Object getTables(String name, Integer page, Integer size);

	/**
	 * 得到数据表的元数据
	 *
	 * @param name 表名
	 * @return 字段配置信息
	 */
	List<ColumnConfig> getColumns(String name);

	/**
	 * 同步表数据
	 *
	 * @param columnInfos    字段配置信息
	 * @param columnInfoList 字段配置信息
	 */
	@Async
	void sync(List<ColumnConfig> columnInfos, List<ColumnConfig> columnInfoList);

	/**
	 * 保持数据
	 *
	 * @param columnInfos 字段配置信息
	 */
	void save(List<ColumnConfig> columnInfos);

	/**
	 * 获取所有table
	 *
	 * @return 所有table
	 */
	Object getTables();

	/**
	 * 代码生成
	 *
	 * @param genConfig 配置信息
	 * @param columns   字段信息
	 */
	void generator(GenConfig genConfig, List<ColumnConfig> columns);

	/**
	 * 预览
	 *
	 * @param genConfig 配置信息
	 * @param columns   字段信息
	 * @return 预览数据
	 */
	List<Map<String, Object>> preview(GenConfig genConfig, List<ColumnConfig> columns);

	/**
	 * 打包下载
	 *
	 * @param genConfig 配置信息
	 * @param columns   字段信息
	 * @param request   请求信息
	 * @param response  返回信息
	 */
	void download(GenConfig genConfig, List<ColumnConfig> columns, HttpServletRequest request,
		HttpServletResponse response);

	/**
	 * 查询数据库的表字段数据数据
	 *
	 * @param table 表名
	 * @return 表字段数据数据
	 */
	List<ColumnConfig> query(String table);


}
