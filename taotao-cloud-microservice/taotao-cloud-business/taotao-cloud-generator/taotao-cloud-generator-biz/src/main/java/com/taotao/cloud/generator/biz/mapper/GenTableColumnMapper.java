package com.taotao.cloud.generator.biz.mapper;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.toolkit.Db;
import com.taotao.cloud.generator.biz.entity.GenTableColumn;
import java.util.List;

/**
 * 业务字段 数据层
 *
 * @author Lion Li
 */
@InterceptorIgnore(dataPermission = "true")
public interface GenTableColumnMapper extends BaseMapper<GenTableColumn> {

	/**
	 * 根据表名称查询列信息
	 *
	 * @param tableName 表名称
	 * @return 列信息
	 */
	List<GenTableColumn> selectDbTableColumnsByName(String tableName);

	default boolean insertBatch(List<GenTableColumn> genTableColumns) {
		return Db.saveBatch(genTableColumns);
	}

}
