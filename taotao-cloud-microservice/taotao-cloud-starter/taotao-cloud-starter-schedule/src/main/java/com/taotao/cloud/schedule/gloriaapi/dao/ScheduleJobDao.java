package com.taotao.cloud.schedule.gloriaapi.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gloria.schedule.entity.ScheduleJobEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.Map;


@Mapper
public interface ScheduleJobDao extends BaseMapper<ScheduleJobEntity> {
	
	/**
	 * 批量更新状态
	 */
	int updateBatch(Map<String, Object> map);
}
