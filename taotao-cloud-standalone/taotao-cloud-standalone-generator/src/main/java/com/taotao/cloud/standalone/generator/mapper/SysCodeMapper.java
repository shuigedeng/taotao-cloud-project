package com.taotao.cloud.standalone.generator.mapper;

import com.taotao.cloud.standalone.generator.domain.SysColumnEntity;
import com.taotao.cloud.standalone.generator.domain.SysTableEntity;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Classname SysCodeMapper
 * @Description 代码生成mapper
 * @Author shuigedeng
 * @since 2019-07-29 18:51
 * @Version 1.0
 */
@Repository
public interface SysCodeMapper {

    /**
     * 根据数据库名称查询所有表属性
     * @param tableSchema
     * @return
     */
    @Select("SELECT TABLE_NAME AS tableName,TABLE_COMMENT AS comments,TABLE_SCHEMA AS tableSchema,CREATE_TIME AS createTime FROM information_schema.TABLES WHERE table_schema=#{tableSchema} ORDER BY createTime DESC")
    List<SysTableEntity> findTableList(String tableSchema);

    /**
     * 根据数据库名和表名查询表的列属性
     * @param tableName
     * @return
     */
    @Select("select COLUMN_NAME AS columnName,DATA_TYPE AS dataType,COLUMN_COMMENT AS columnComment,CHARACTER_SET_NAME AS characterSetName,COLUMN_TYPE AS columnType from information_schema.COLUMNS where table_name = #{tableName} and table_schema = #{tableSchema}")
    List<SysColumnEntity> findColumnList(String tableName, String tableSchema);

}
