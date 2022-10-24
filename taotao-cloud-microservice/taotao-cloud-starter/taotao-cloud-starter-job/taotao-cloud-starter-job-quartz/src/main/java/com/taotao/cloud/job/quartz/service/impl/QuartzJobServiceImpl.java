package com.taotao.cloud.job.quartz.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.taotao.cloud.common.model.PageResult;
import com.taotao.cloud.job.quartz.dao.QuartzJobMapper;
import com.taotao.cloud.job.quartz.entity.QuartzJob;
import com.taotao.cloud.job.quartz.enums.QuartzJobCode;
import com.taotao.cloud.job.quartz.exception.QuartzExecutionException;
import com.taotao.cloud.job.quartz.param.QuartzJobDTO;
import com.taotao.cloud.job.quartz.param.QuartzJobQuery;
import com.taotao.cloud.job.quartz.service.QuartzJobService;
import com.taotao.cloud.job.quartz.utils.QuartzManager;
import com.taotao.cloud.job.quartz.vo.QuartzJobVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 定时任务
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class QuartzJobServiceImpl implements QuartzJobService {
	private final QuartzJobMapper quartzJobMapper;
	private final QuartzManager quartzManager;

	/**
	 * 启动项目时，初始化定时器
	 */
	@PostConstruct
	public void init() throws SchedulerException {
		quartzManager.clear();
		List<QuartzJob> quartzJobList = quartzJobMapper.selectList(null);
		for (QuartzJob quartzJob : quartzJobList) {
			quartzManager.addJob(quartzJob);
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void addJob(QuartzJobDTO quartzJobDTO) {
		QuartzJob quartzJob = new QuartzJob();
		BeanUtil.copyProperties(quartzJobDTO, quartzJob);

		quartzJob.setState(QuartzJobCode.STOP);

		if (quartzJobMapper.insert(quartzJob) > 0) {
			quartzManager.addJob(quartzJob);
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void updateJob(QuartzJobDTO quartzJobDTO) {
		QuartzJob quartzJob = quartzJobMapper.selectById(quartzJobDTO.getId());
		BeanUtil.copyProperties(quartzJobDTO, quartzJob, CopyOptions.create().ignoreNullValue());

		quartzJobMapper.updateById(quartzJob);

		quartzManager.updateJob(quartzJob);

		// jobScheduler.delete(quartzJob.getId());
		// if (Objects.equals(quartzJob.getState(), QuartzJobCode.RUNNING)) {
		// 	jobScheduler.add(quartzJob.getId(), quartzJob.getJobClassName(), quartzJob.getCron(), quartzJob.getParameter());
		// }
	}

	@Override
	public void runOnce(Long id) {
		QuartzJob quartzJob = quartzJobMapper.selectById(id);
		quartzManager.runJobNow(quartzJob);

		// jobScheduler.execute(quartzJob.getJobClassName(), quartzJob.getParameter());
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void start(Long id) {
		QuartzJob quartzJob = quartzJobMapper.selectById(id);

		// 非运行才进行操作
		if (!Objects.equals(quartzJob.getState(), QuartzJobCode.RUNNING)) {
			quartzJob.setState(QuartzJobCode.RUNNING);
			quartzJobMapper.updateById(quartzJob);

			quartzManager.addJob(quartzJob);
			// jobScheduler.add(quartzJob.getId(), quartzJob.getJobClassName(), quartzJob.getCron(), quartzJob.getParameter());
		} else {
			throw new QuartzExecutionException("已经是启动状态");
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void stopJob(Long id) {
		QuartzJob quartzJob = quartzJobMapper.selectById(id);
		if (!Objects.equals(quartzJob.getState(), QuartzJobCode.STOP)) {
			quartzJob.setState(QuartzJobCode.STOP);
			quartzJobMapper.updateById(quartzJob);

			quartzManager.pauseJob(quartzJob);
			// jobScheduler.delete(id);
		} else {
			throw new QuartzExecutionException("已经是停止状态");
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void deleteJob(Long id) {
		QuartzJob quartzJob = quartzJobMapper.selectById(id);
		quartzJobMapper.deleteById(id);

		quartzManager.deleteJob(quartzJob);
	}

	@Override
	public void syncJobStatus() {
		LambdaQueryWrapper<QuartzJob> wrapper = new LambdaQueryWrapper<>();
		wrapper.eq(QuartzJob::getState, QuartzJobCode.RUNNING);
		List<QuartzJob> quartzJobs = quartzJobMapper.selectList(wrapper);
		Map<String, QuartzJob> quartzJobMap = quartzJobs.stream()
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
		Collection<QuartzJob> quartzJobList = quartzJobMap.values();
		for (QuartzJob quartzJob : quartzJobList) {
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
	public QuartzJob findById(Long id) {
		return quartzJobMapper.selectById(id);
	}

	@Override
	public PageResult<QuartzJobVO> page(QuartzJobQuery quartzJobQuery) {
		LambdaQueryWrapper<QuartzJob> wrapper = new LambdaQueryWrapper<>();
		wrapper.orderByDesc(QuartzJob::getId);

		IPage<QuartzJob> quartzJobIPage = quartzJobMapper.selectPage(quartzJobQuery.buildMpPage(), wrapper);
		return PageResult.convertMybatisPage(quartzJobIPage, QuartzJobVO.class);
	}


	@Override
	public String judgeJobClass(String jobClassName) {
		return quartzManager.getJobClass(jobClassName).getName();
	}
}
