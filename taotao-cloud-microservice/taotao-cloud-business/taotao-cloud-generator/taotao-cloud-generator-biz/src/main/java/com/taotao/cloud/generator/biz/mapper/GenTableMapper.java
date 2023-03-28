package com.taotao.cloud.generator.biz.mapper;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.taotao.cloud.generator.biz.entity.GenTable;
import java.util.List;
import org.apache.ibatis.annotations.Param;

/**
 * 业务 数据层
 *
 * @author Lion Li
 */
@InterceptorIgnore(dataPermission = "true")
public interface GenTableMapper extends BaseMapper<GenTable> {

	Page<GenTable> selectPageDbTableList(
		@Param("com/taotao/cloud/generator/biz/page") Page<GenTable> page,
		@Param("genTable") GenTable genTable);

	/**
	 * 查询据库列表
	 *
	 * @param tableNames 表名称组
	 * @return 数据库表集合
	 */
	List<GenTable> selectDbTableListByNames(String[] tableNames);

	/**
	 * 查询所有表信息
	 *
	 * @return 表信息集合
	 */
	List<GenTable> selectGenTableAll();

	/**
	 * 查询表ID业务信息
	 *
	 * @param id 业务ID
	 * @return 业务信息
	 */
	GenTable selectGenTableById(Long id);

	/**
	 * 查询表名称业务信息
	 *
	 * @param tableName 表名称
	 * @return 业务信息
	 */
	GenTable selectGenTableByName(String tableName);

}
