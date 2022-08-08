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
package com.taotao.cloud.sys.biz.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.taotao.cloud.sys.api.web.vo.generator.TableInfo;
import com.taotao.cloud.sys.biz.model.entity.config.ColumnConfig;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * IColumnInfoMapper
 *
 * @author shuigedeng
 * @version 2022.03
 * @since 2022-03-29 08:56:04
 */

public interface IColumnInfoMapper extends BaseMapper<ColumnConfig> {

	@Select("""
		<script>
		select table_name ,
			   create_time ,
			   engine,
			   table_collation as coding,
			   table_comment as remark
		from information_schema.tables
		where table_schema = (select database())
		<if test="name!=null">
		  and table_name like CONCAT('%',#{name},'%')
		</if>
		order by create_time desc
		</script>
		""")
	IPage<TableInfo> selectTablePage(@Param("page") Page page, @Param("name") String name);

	@Select("""
		<script>
		select table_name,
				create_time, 
				engine, 
				table_collation, 
				table_comment 
		from information_schema.tables
		where table_schema = (select database()) 
		order by create_time desc
		</script>
		""")
	List<TableInfo> selectTables();

	@Select("""
		SELECT COLUMN_NAME,
			   IS_NULLABLE,
			   DATA_TYPE,
			   COLUMN_COMMENT, 
			   COLUMN_KEY, 
			   EXTRA 
		FROM INFORMATION_SCHEMA.COLUMNS
		WHERE TABLE_NAME = #{name} AND TABLE_SCHEMA = (SELECT DATABASE()) 
		ORDER BY ORDINAL_POSITION
		""")
	List<Map<String, Object>> queryByTableName(@Param("name") String name);

}
