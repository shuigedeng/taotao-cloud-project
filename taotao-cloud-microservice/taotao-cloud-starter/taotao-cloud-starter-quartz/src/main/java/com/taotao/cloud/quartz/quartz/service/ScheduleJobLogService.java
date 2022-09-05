package com.taotao.cloud.quartz.quartz.service;


import cn.hutool.db.PageResult;
import com.taotao.cloud.quartz.quartz.entity.ScheduleJobLogEntity;
import com.taotao.cloud.quartz.quartz.query.ScheduleJobLogQuery;
import com.taotao.cloud.quartz.quartz.vo.ScheduleJobLogVO;

/**
 * 定时任务日志
 *
 * @author 阿沐 babamu@126.com
 */
public interface ScheduleJobLogService extends BaseService<ScheduleJobLogEntity> {

    PageResult<ScheduleJobLogVO> page(ScheduleJobLogQuery query);

}
