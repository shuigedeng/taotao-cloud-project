package com.taotao.cloud.job.biz.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.taotao.cloud.job.biz.entity.PlatformViewData;
import com.taotao.cloud.web.base.mapper.BaseSuperMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 平台流量数据
 */
public interface PlatformViewMapper extends BaseSuperMapper<PlatformViewData> {
    /**
     * UV流量统计
     *
     * @param queryWrapper 查询条件
     * @return UV流量统计数量
     */
    @Select("SELECT sum(uv_num) FROM tt_s_platform_view_data ${ew.customSqlSegment}")
    Integer count(@Param(Constants.WRAPPER) QueryWrapper queryWrapper);
}
