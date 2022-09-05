package com.taotao.cloud.quartz.quartz.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.db.PageResult;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.taotao.cloud.quartz.quartz.convert.ScheduleJobLogConvert;
import com.taotao.cloud.quartz.quartz.dao.ScheduleJobLogDao;
import com.taotao.cloud.quartz.quartz.entity.ScheduleJobLogEntity;
import com.taotao.cloud.quartz.quartz.query.ScheduleJobLogQuery;
import com.taotao.cloud.quartz.quartz.service.ScheduleJobLogService;
import com.taotao.cloud.quartz.quartz.vo.ScheduleJobLogVO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 定时任务日志
 *
 * @author 阿沐 babamu@126.com
 */
@Service
@AllArgsConstructor
public class ScheduleJobLogServiceImpl extends BaseServiceImpl<ScheduleJobLogDao, ScheduleJobLogEntity> implements
	ScheduleJobLogService {

    @Override
    public PageResult<ScheduleJobLogVO> page(ScheduleJobLogQuery query) {
        IPage<ScheduleJobLogEntity> page = baseMapper.selectPage(getPage(query), getWrapper(query));

        return new PageResult<>(ScheduleJobLogConvert.INSTANCE.convertList(page.getRecords()), page.getTotal());
    }

    private LambdaQueryWrapper<ScheduleJobLogEntity> getWrapper(ScheduleJobLogQuery query){
        LambdaQueryWrapper<ScheduleJobLogEntity> wrapper = Wrappers.lambdaQuery();
        wrapper.like(StrUtil.isNotBlank(query.getJobName()), ScheduleJobLogEntity::getJobName, query.getJobName());
        wrapper.like(StrUtil.isNotBlank(query.getJobGroup()), ScheduleJobLogEntity::getJobGroup, query.getJobGroup());
        wrapper.eq(query.getJobId() != null, ScheduleJobLogEntity::getJobId, query.getJobId());
        wrapper.orderByDesc(ScheduleJobLogEntity::getId);
        return wrapper;
    }

}
