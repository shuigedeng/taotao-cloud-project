/**
 * Copyright (C) 2018-2020 All rights reserved, Designed By www.yixiang.co 注意：
 * 本软件为www.yixiang.co开发研制
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
	 * @return /
	 */
	Object getTables(String name, Integer page, Integer size);

	/**
	 * 得到数据表的元数据
	 *
	 * @param name 表名
	 * @return /
	 */
	List<ColumnConfig> getColumns(String name);

	/**
	 * 同步表数据
	 *
	 * @param columnInfos    /
	 * @param columnInfoList
	 */
	@Async
	void sync(List<ColumnConfig> columnInfos, List<ColumnConfig> columnInfoList);

	/**
	 * 保持数据
	 *
	 * @param columnInfos /
	 */
	void save(List<ColumnConfig> columnInfos);

	/**
	 * 获取所有table
	 *
	 * @return /
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
	 * @return /
	 */
	List<Map<String, Object>> preview(GenConfig genConfig, List<ColumnConfig> columns);

	/**
	 * 打包下载
	 *
	 * @param genConfig 配置信息
	 * @param columns   字段信息
	 * @param request   /
	 * @param response  /
	 */
	void download(GenConfig genConfig, List<ColumnConfig> columns, HttpServletRequest request,
		HttpServletResponse response);

	/**
	 * 查询数据库的表字段数据数据
	 *
	 * @param table /
	 * @return /
	 */
	List<ColumnConfig> query(String table);


}
