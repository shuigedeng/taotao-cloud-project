package com.taotao.cloud.job.biz.schedule.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.taotao.cloud.job.biz.schedule.model.ScheduledJobLog;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ScheduledJobMapper extends BaseMapper<ScheduledJobLog> {


}
