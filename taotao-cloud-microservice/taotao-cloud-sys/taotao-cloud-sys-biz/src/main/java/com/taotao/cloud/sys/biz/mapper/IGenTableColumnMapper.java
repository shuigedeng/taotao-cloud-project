package com.taotao.cloud.sys.biz.mapper;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.taotao.cloud.sys.biz.model.entity.gen.GenTableColumn;
import com.taotao.cloud.web.base.mapper.BaseSuperMapper;
import java.util.List;
import org.apache.ibatis.annotations.Select;

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
	@Select("""
		select column_name,
					   (case when (is_nullable = 'no' && column_key != 'PRI') then '1' else null end) as is_required,
					   (case when column_key = 'PRI' then '1' else '0' end)                           as is_pk,
					   ordinal_position                                                               as sort,
					   column_comment,
					   (case when extra = 'auto_increment' then '1' else '0' end)                     as is_increment,
					   column_type
				from information_schema.columns
				where table_schema = (select database()) and table_name = (#{tableName})
				order by ordinal_position
		""")
	List<GenTableColumn> selectDbTableColumnsByName(String tableName);

}
