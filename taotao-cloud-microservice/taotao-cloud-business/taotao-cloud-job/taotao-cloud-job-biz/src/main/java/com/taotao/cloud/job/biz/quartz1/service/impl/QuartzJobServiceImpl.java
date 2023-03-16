package com.taotao.cloud.job.biz.quartz1.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.taotao.cloud.common.model.PageResult;
import com.taotao.cloud.job.biz.quartz1.dao.QuartzJobMapper;
import com.taotao.cloud.job.biz.quartz1.entity.QuartzJobEntity;
import com.taotao.cloud.job.biz.quartz1.param.QuartzJobDTO;
import com.taotao.cloud.job.biz.quartz1.param.QuartzJobQuery;
import com.taotao.cloud.job.biz.quartz1.service.QuartzJobService;
import com.taotao.cloud.job.biz.quartz1.vo.QuartzJobVO;
import com.taotao.cloud.job.quartz.quartz1.entity.QuartzJob;
import com.taotao.cloud.job.quartz.quartz1.enums.QuartzJobCode;
import com.taotao.cloud.job.quartz.quartz1.exception.QuartzExecutionException;
import com.taotao.cloud.job.quartz.quartz1.utils.QuartzManager;
import jakarta.annotation.PostConstruct;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 定时任务
 */
@Service
public class QuartzJobServiceImpl implements QuartzJobService {

	private final QuartzJobMapper quartzJobMapper;
	private final QuartzManager quartzManager;

	public QuartzJobServiceImpl(QuartzJobMapper quartzJobMapper, QuartzManager quartzManager) {
		this.quartzJobMapper = quartzJobMapper;
		this.quartzManager = quartzManager;
	}

	/**
	 * 启动项目时，初始化定时器
	 */
	@PostConstruct
	public void init() throws SchedulerException {
		quartzManager.clear();

		List<QuartzJobEntity> quartzJobList = quartzJobMapper.selectList(null);
		for (QuartzJobEntity quartzJobEntity : quartzJobList) {
			QuartzJob quartzJob = new QuartzJob();
			BeanUtil.copyProperties(quartzJobEntity, quartzJob);
			quartzManager.addJob(quartzJob);
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void addJob(QuartzJobDTO quartzJobDTO) {
		QuartzJobEntity quartzJobEntity = new QuartzJobEntity();
		BeanUtil.copyProperties(quartzJobDTO, quartzJobEntity);
		quartzJobEntity.setState(QuartzJobCode.STOP);

		QuartzJob quartzJob = new QuartzJob();
		BeanUtil.copyProperties(quartzJobEntity, quartzJob);

		if (quartzJobMapper.insert(quartzJobEntity) > 0) {
			quartzManager.addJob(quartzJob);
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void updateJob(QuartzJobDTO quartzJobDTO) {
		QuartzJobEntity quartzJobEntity = quartzJobMapper.selectById(quartzJobDTO.getId());
		BeanUtil.copyProperties(quartzJobDTO, quartzJobEntity,
			CopyOptions.create().ignoreNullValue());
		quartzJobMapper.updateById(quartzJobEntity);

		QuartzJob quartzJob = new QuartzJob();
		BeanUtil.copyProperties(quartzJobEntity, quartzJob);
		quartzManager.updateJob(quartzJob);

		//jobScheduler.delete(quartzJob.getId());
		//if (Objects.equals(quartzJob.getState(), QuartzJobCode.RUNNING)) {
		//	jobScheduler.add(quartzJob.getId(), quartzJob.getJobClassName(), quartzJob.getCron(), quartzJob.getParameter());
		//}
	}

	@Override
	public void runOnce(Long id) {
		QuartzJobEntity quartzJobEntity = quartzJobMapper.selectById(id);

		QuartzJob quartzJob = new QuartzJob();
		BeanUtil.copyProperties(quartzJobEntity, quartzJob);
		quartzManager.runJobNow(quartzJob);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void start(Long id) {
		QuartzJobEntity quartzJobEntity = quartzJobMapper.selectById(id);

		// 非运行才进行操作
		if (!Objects.equals(quartzJobEntity.getState(), QuartzJobCode.RUNNING)) {
			quartzJobEntity.setState(QuartzJobCode.RUNNING);
			quartzJobMapper.updateById(quartzJobEntity);

			QuartzJob quartzJob = new QuartzJob();
			BeanUtil.copyProperties(quartzJobEntity, quartzJob);
			quartzManager.addJob(quartzJob);
			// jobScheduler.add(quartzJob.getId(), quartzJob.getJobClassName(), quartzJob.getCron(), quartzJob.getParameter());
		} else {
			throw new QuartzExecutionException("已经是启动状态");
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void stopJob(Long id) {
		QuartzJobEntity quartzJobEntity = quartzJobMapper.selectById(id);
		if (!Objects.equals(quartzJobEntity.getState(), QuartzJobCode.STOP)) {
			quartzJobEntity.setState(QuartzJobCode.STOP);
			quartzJobMapper.updateById(quartzJobEntity);

			QuartzJob quartzJob = new QuartzJob();
			BeanUtil.copyProperties(quartzJobEntity, quartzJob);
			quartzManager.pauseJob(quartzJob);
			// jobScheduler.delete(id);
		} else {
			throw new QuartzExecutionException("已经是停止状态");
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void deleteJob(Long id) {
		QuartzJobEntity quartzJobEntity = quartzJobMapper.selectById(id);
		quartzJobMapper.deleteById(id);

		QuartzJob quartzJob = new QuartzJob();
		BeanUtil.copyProperties(quartzJobEntity, quartzJob);
		quartzManager.deleteJob(quartzJob);
	}

	@Override
	public void syncJobStatus() {
		LambdaQueryWrapper<QuartzJobEntity> wrapper = new LambdaQueryWrapper<>();
		wrapper.eq(QuartzJobEntity::getState, QuartzJobCode.RUNNING);
		List<QuartzJobEntity> quartzJobs = quartzJobMapper.selectList(wrapper);
		Map<String, QuartzJobEntity> quartzJobMap = quartzJobs.stream()
			.collect(Collectors.toMap(o -> o.getId().toString(), o -> o));

		List<Trigger> triggers = quartzManager.findTriggers();

		// 将开始任务列表里没有的Trigger给删除. 将未启动的任务状态更新成停止
		for (Trigger trigger : triggers) {
			String triggerName = trigger.getKey().getName();
			if (!quartzJobMap.containsKey(triggerName)) {
				quartzManager.deleteTrigger(triggerName);
			} else {
				quartzJobMap.remove(triggerName);
			}
		}

		// 更新任务列表状态
		Collection<QuartzJobEntity> quartzJobList = quartzJobMap.values();
		for (QuartzJobEntity quartzJob : quartzJobList) {
			quartzJob.setState(QuartzJobCode.STOP);
		}
		if (CollUtil.isNotEmpty(quartzJobList)) {
			quartzJobList.forEach(quartzJobMapper::updateById);
		}
	}

	@Override
	public void startAllJobs() {
		quartzManager.startAllJobs();
	}

	@Override
	public void pauseAllJobs() {
		quartzManager.pauseAll();
	}

	@Override
	public void resumeAllJobs() {
		quartzManager.resumeAll();
	}

	@Override
	public void shutdownAllJobs() {
		quartzManager.shutdownAll();
	}

	@Override
	public QuartzJobEntity findById(Long id) {
		return quartzJobMapper.selectById(id);
	}

	@Override
	public PageResult<QuartzJobVO> page(QuartzJobQuery quartzJobQuery) {
		LambdaQueryWrapper<QuartzJobEntity> wrapper = new LambdaQueryWrapper<>();
		wrapper.orderByDesc(QuartzJobEntity::getId);

		IPage<QuartzJobEntity> quartzJobIPage = quartzJobMapper.selectPage(
			quartzJobQuery.buildMpPage(), wrapper);
		return PageResult.convertMybatisPage(quartzJobIPage, QuartzJobVO.class);
	}


	@Override
	public String judgeJobClass(String jobClassName) {
		return quartzManager.getJobClass(jobClassName).getName();
	}
}
