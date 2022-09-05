package com.taotao.cloud.quartz.quartz.service;


import cn.hutool.db.PageResult;
import com.taotao.cloud.quartz.quartz.entity.ScheduleJobEntity;
import com.taotao.cloud.quartz.quartz.query.ScheduleJobQuery;
import com.taotao.cloud.quartz.quartz.vo.ScheduleJobVO;
import java.util.List;

/**
 * 定时任务
 *
 * @author 阿沐 babamu@126.com
 */
public interface ScheduleJobService extends BaseService<ScheduleJobEntity> {

    PageResult<ScheduleJobVO> page(ScheduleJobQuery query);

    void save(ScheduleJobVO vo);

    void update(ScheduleJobVO vo);

    void delete(List<Long> idList);

    void run(ScheduleJobVO vo);

    void changeStatus(ScheduleJobVO vo);
}
