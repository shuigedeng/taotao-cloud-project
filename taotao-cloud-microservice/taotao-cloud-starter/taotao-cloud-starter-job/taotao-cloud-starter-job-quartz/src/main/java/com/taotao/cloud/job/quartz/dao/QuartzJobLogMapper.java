package com.taotao.cloud.job.quartz.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.taotao.cloud.job.quartz.entity.QuartzJobLog;
import org.apache.ibatis.annotations.Mapper;

/**
 * 定时任务日志
 */
@Mapper
public interface QuartzJobLogMapper extends BaseMapper<QuartzJobLog> {

}
