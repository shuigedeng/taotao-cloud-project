package com.taotao.cloud.quartz.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.taotao.cloud.quartz.entity.QuartzJobLog;
import org.apache.ibatis.annotations.Mapper;

/**
 * 定时任务日志
 */
@Mapper
public interface QuartzJobLogMapper extends BaseMapper<QuartzJobLog> {

}
