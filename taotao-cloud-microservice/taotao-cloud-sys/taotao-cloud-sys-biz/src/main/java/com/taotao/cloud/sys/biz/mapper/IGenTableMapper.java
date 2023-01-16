package com.taotao.cloud.sys.biz.mapper;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.taotao.cloud.sys.biz.model.entity.gen.GenTable;
import com.taotao.cloud.web.base.mapper.BaseSuperMapper;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 业务 数据层
 */
@InterceptorIgnore(dataPermission = "true")
public interface IGenTableMapper extends BaseSuperMapper<GenTable, Long> {

	@Select("""
		select table_name, table_comment, create_time, update_time
				from information_schema.tables ${ew.getCustomSqlSegment}
		""")
	Page<GenTable> selectPageDbTableList(@Param("page") IPage<GenTable> page,
		@Param(Constants.WRAPPER) Wrapper<Object> queryWrapper);

	/**
	 * 查询据库列表
	 *
	 * @param queryWrapper 查询条件
	 * @return 数据库表集合
	 */
	@Select("""
		select table_name, table_comment, create_time, update_time
				from information_schema.tables ${ew.getCustomSqlSegment}
		""")
	List<GenTable> selectDbTableList(@Param(Constants.WRAPPER) Wrapper<Object> queryWrapper);


	/**
	 * 查询据库列表
	 *
	 * @param tableNames 表名称组
	 * @return 数据库表集合
	 */
	@Select("""
		<script>
		select table_name, table_comment, create_time, update_time from information_schema.tables
				where table_name NOT LIKE 'qrtz_%' and table_name NOT LIKE 'gen_%' and table_schema = (select database())
				and table_name in
				<foreach collection="array" item="name" open="(" separator="," close=")">
					#{name}
				</foreach>
		</script>
		""")
	List<GenTable> selectDbTableListByNames(String[] tableNames);

	/**
	 * 查询所有表信息
	 *
	 * @return 表信息集合
	 */
	@Select("""
		SELECT t.table_id,
					   t.table_name,
					   t.table_comment,
					   t.sub_table_name,
					   t.sub_table_fk_name,
					   t.class_name,
					   t.tpl_category,
					   t.package_name,
					   t.module_name,
					   t.business_name,
					   t.function_name,
					   t.function_author,
					   t.options,
					   t.remark,
					   c.column_id,
					   c.column_name,
					   c.column_comment,
					   c.column_type,
					   c.java_type,
					   c.java_field,
					   c.is_pk,
					   c.is_increment,
					   c.is_required,
					   c.is_insert,
					   c.is_edit,
					   c.is_list,
					   c.is_query,
					   c.query_type,
					   c.html_type,
					   c.dict_type,
					   c.sort
				FROM gen_table t
						 LEFT JOIN gen_table_column c ON t.table_id = c.table_id
				order by c.sort
		""")
	List<GenTable> selectGenTableAll();

	/**
	 * 查询表ID业务信息
	 *
	 * @param id 业务ID
	 * @return 业务信息
	 */
	@Select("""
		SELECT t.table_id,
					   t.table_name,
					   t.table_comment,
					   t.sub_table_name,
					   t.sub_table_fk_name,
					   t.class_name,
					   t.tpl_category,
					   t.package_name,
					   t.module_name,
					   t.business_name,
					   t.function_name,
					   t.function_author,
					   t.gen_type,
					   t.gen_path,
					   t.options,
					   t.remark,
					   c.column_id,
					   c.column_name,
					   c.column_comment,
					   c.column_type,
					   c.java_type,
					   c.java_field,
					   c.is_pk,
					   c.is_increment,
					   c.is_required,
					   c.is_insert,
					   c.is_edit,
					   c.is_list,
					   c.is_query,
					   c.query_type,
					   c.html_type,
					   c.dict_type,
					   c.sort
				FROM gen_table t
						 LEFT JOIN gen_table_column c ON t.table_id = c.table_id
				where t.table_id = #{tableId}
				order by c.sort
		""")
	GenTable selectGenTableById(Long id);

	/**
	 * 查询表名称业务信息
	 *
	 * @param tableName 表名称
	 * @return 业务信息
	 */
	@Select("""
			SELECT t.table_id,
					   t.table_name,
					   t.table_comment,
					   t.sub_table_name,
					   t.sub_table_fk_name,
					   t.class_name,
					   t.tpl_category,
					   t.package_name,
					   t.module_name,
					   t.business_name,
					   t.function_name,
					   t.function_author,
					   t.gen_type,
					   t.gen_path,
					   t.options,
					   t.remark,
					   c.column_id,
					   c.column_name,
					   c.column_comment,
					   c.column_type,
					   c.java_type,
					   c.java_field,
					   c.is_pk,
					   c.is_increment,
					   c.is_required,
					   c.is_insert,
					   c.is_edit,
					   c.is_list,
					   c.is_query,
					   c.query_type,
					   c.html_type,
					   c.dict_type,
					   c.sort
				FROM gen_table t
						 LEFT JOIN gen_table_column c ON t.table_id = c.table_id
				where t.table_name = #{tableName}
				order by c.sort
		""")
	GenTable selectGenTableByName(String tableName);

}
