package com.taotao.cloud.job.biz.schedule.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.taotao.cloud.job.biz.schedule.entity.ScheduledJob;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ScheduledJobLogMapper extends BaseMapper<ScheduledJob> {

}
