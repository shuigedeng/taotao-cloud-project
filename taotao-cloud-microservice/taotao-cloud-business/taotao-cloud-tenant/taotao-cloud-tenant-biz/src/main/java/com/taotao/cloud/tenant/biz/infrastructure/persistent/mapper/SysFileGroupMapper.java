package com.taotao.cloud.tenant.biz.infrastructure.persistent.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.taotao.cloud.tenant.biz.domain.aggregate.SysFileGroup;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * 文件分组Mapper
 */
@Mapper
public interface SysFileGroupMapper extends BaseMapper<SysFileGroup> {

    /**
     * 获取分组及其文件数量
     */
    @Select("SELECT g.id, g.group_name as groupName, g.group_type as groupType, g.icon, " +
            "COALESCE(COUNT(f.id), 0) as fileCount " +
            "FROM sys_file_group g " +
            "LEFT JOIN sys_file_metadata f ON g.id = f.group_id AND f.status = 1 " +
            "WHERE g.deleted = 0 AND g.status = 1 " +
            "GROUP BY g.id, g.group_name, g.group_type, g.icon " +
            "ORDER BY g.sort")
    List<SysFileGroup> selectGroupWithFileCount();

    /**
     * 获取各类型文件数量统计
     */
    @Select("SELECT " +
            "COUNT(*) as total, " +
            "SUM(CASE WHEN mime_type LIKE 'image/%' THEN 1 ELSE 0 END) as imageCount, " +
            "SUM(CASE WHEN mime_type LIKE 'application/%' THEN 1 ELSE 0 END) as documentCount, " +
            "SUM(CASE WHEN mime_type LIKE 'video/%' THEN 1 ELSE 0 END) as videoCount, " +
            "SUM(CASE WHEN mime_type LIKE 'audio/%' THEN 1 ELSE 0 END) as audioCount " +
            "FROM sys_file_metadata WHERE status = 1")
    Map<String, Object> selectFileStatistics();
}
