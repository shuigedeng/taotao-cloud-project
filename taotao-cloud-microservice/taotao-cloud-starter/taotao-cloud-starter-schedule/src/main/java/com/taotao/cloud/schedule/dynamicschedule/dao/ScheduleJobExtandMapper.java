package com.taotao.cloud.schedule.dynamicschedule.dao;

import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ScheduleJobExtandMapper {

    int updateBatch(@Param("ids") List ids,@Param("status") int status);

    int deleteBatch(@Param("ids") List<Long> ids);
}
