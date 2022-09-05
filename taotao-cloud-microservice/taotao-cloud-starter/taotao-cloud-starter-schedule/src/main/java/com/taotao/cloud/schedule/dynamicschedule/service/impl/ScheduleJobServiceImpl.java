
package com.taotao.cloud.schedule.dynamicschedule.service.impl;


import com.example.dynamicschedule.base.Constant;
import com.example.dynamicschedule.bean.ScheduleJob;
import com.example.dynamicschedule.bean.ScheduleJobExample;
import com.example.dynamicschedule.dao.ScheduleJobExtandMapper;
import com.example.dynamicschedule.dao.ScheduleJobMapper;
import com.example.dynamicschedule.service.ScheduleJobService;
import com.example.dynamicschedule.utils.ScheduleUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.quartz.CronTrigger;
import org.quartz.Scheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.*;

@Service("scheduleJobService")
public class ScheduleJobServiceImpl  implements ScheduleJobService {
	@Autowired
    private Scheduler scheduler;

	@Autowired
	private ScheduleJobMapper scheduleJobMapper;

	@Autowired
	private ScheduleJobExtandMapper scheduleJobExtandMapper;

	/**
	 * 项目启动时，初始化定时器
	 */
	@PostConstruct
	public void init(){
		ScheduleJobExample scheduleJobExample = new ScheduleJobExample();

		List<ScheduleJob> scheduleJobList = scheduleJobMapper.selectByExample(scheduleJobExample);


		for(ScheduleJob scheduleJob : scheduleJobList){
			CronTrigger cronTrigger = ScheduleUtils.getCronTrigger(scheduler, scheduleJob.getJobId());
            //如果不存在，则创建
            if(cronTrigger == null) {
                ScheduleUtils.createScheduleJob(scheduler, scheduleJob);
            }else {
                ScheduleUtils.updateScheduleJob(scheduler, scheduleJob);
            }
		}
	}


	public ScheduleJob getScheduleJobByJobId(Long jobId){
		return scheduleJobMapper.selectByPrimaryKey(jobId);
	}

	@Override
	public PageInfo queryPage(Map<String, Object> params) {
		int page = Integer.parseInt(params.getOrDefault("page", "1").toString());
		int pageSize = Integer.parseInt(params.getOrDefault("pageSize", "10").toString());
		PageHelper.startPage(page,pageSize);
		ScheduleJobExample scheduleJobExample = new ScheduleJobExample();
		ScheduleJobExample.Criteria criteria = scheduleJobExample.createCriteria();
		Object beanName = params.get("beanName");
		if(Objects.nonNull(beanName)){
			criteria.andBeanNameLike("%"+beanName+"%");
		}
		List<ScheduleJob> scheduleJobs = scheduleJobMapper.selectByExample(scheduleJobExample);
		PageInfo pageInfo = new PageInfo<>(scheduleJobs);
		return pageInfo;
	}


	@Override
	@Transactional(rollbackFor = Exception.class)
	public void save(ScheduleJob scheduleJob) {
		scheduleJob.setCreateTime(new Date());
		scheduleJob.setStatus(Byte.parseByte(Constant.NORMAL+""));
		scheduleJobMapper.insertSelective(scheduleJob);
        ScheduleUtils.createScheduleJob(scheduler, scheduleJob);
    }

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void update(ScheduleJob scheduleJob) {
        ScheduleUtils.updateScheduleJob(scheduler, scheduleJob);
		scheduleJobMapper.updateByPrimaryKeySelective(scheduleJob);
    }

	@Override
	@Transactional(rollbackFor = Exception.class)
    public void deleteBatch(List<Long> jobIds) {
    	for(Long jobId : jobIds){
    		ScheduleUtils.deleteScheduleJob(scheduler, jobId);
    	}

    	//删除数据
		scheduleJobExtandMapper.deleteBatch(jobIds);
	}

	@Override
    public int updateBatch(List<Long>  jobIds, int status){
    	return scheduleJobExtandMapper.updateBatch(jobIds,status);
    }

	@Override
	@Transactional(rollbackFor = Exception.class)
    public void run(List<Long> jobIds) {
    	for(Long jobId : jobIds){
    		ScheduleUtils.run(scheduler, scheduleJobMapper.selectByPrimaryKey(jobId));
    	}
		updateBatch(jobIds, Constant.NORMAL);
    }

	@Override
	@Transactional(rollbackFor = Exception.class)
    public void pause(List<Long> jobIds) {
        for(Long jobId : jobIds){
    		ScheduleUtils.pauseJob(scheduler, jobId);
    	}

    	updateBatch(jobIds, Constant.PAUSE);
    }

	@Override
	@Transactional(rollbackFor = Exception.class)
    public void resume(List<Long> jobIds) {
    	for(Long jobId : jobIds){
    		ScheduleUtils.resumeJob(scheduler, jobId);
    	}

    	updateBatch(jobIds, Constant.NORMAL);
    }

}
