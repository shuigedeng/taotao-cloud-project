package com.taotao.cloud.job.biz.quartz1.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.taotao.cloud.job.biz.quartz1.entity.QuartzJobEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 定时任务
 */
@Mapper
public interface QuartzJobMapper extends BaseMapper<QuartzJobEntity> {

}
