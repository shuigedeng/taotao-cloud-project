package com.taotao.cloud.quartz.quartz.dao;

import net.maku.framework.common.dao.BaseDao;
import net.maku.quartz.entity.ScheduleJobLogEntity;
import org.apache.ibatis.annotations.Mapper;

/**
* 定时任务日志
*
* @author 阿沐 babamu@126.com
*/
@Mapper
public interface ScheduleJobLogDao extends BaseDao<ScheduleJobLogEntity> {
	
}
