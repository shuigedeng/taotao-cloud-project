package com.taotao.cloud.sys.biz.mapper;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.taotao.cloud.sys.biz.model.entity.gen.GenTableColumn;
import com.taotao.cloud.web.base.mapper.BaseSuperMapper;

import java.util.List;

/**
 * 业务字段 数据层
 */
@InterceptorIgnore(dataPermission = "true")
public interface IGenTableColumnMapper extends BaseSuperMapper<GenTableColumn, Long> {
	/**
	 * 根据表名称查询列信息
	 *
	 * @param tableName 表名称
	 * @return 列信息
	 */
	List<GenTableColumn> selectDbTableColumnsByName(String tableName);

}
