package com.taotao.cloud.job.quartz.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.taotao.cloud.job.quartz.entity.QuartzJob;
import org.apache.ibatis.annotations.Mapper;

/**
 * 定时任务
 */
@Mapper
public interface QuartzJobMapper extends BaseMapper<QuartzJob> {
}
