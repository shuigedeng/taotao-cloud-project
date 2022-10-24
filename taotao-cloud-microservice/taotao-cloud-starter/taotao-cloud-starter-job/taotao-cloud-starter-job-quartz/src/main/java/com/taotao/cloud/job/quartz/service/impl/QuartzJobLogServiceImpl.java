package com.taotao.cloud.job.quartz.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.taotao.cloud.common.model.PageResult;
import com.taotao.cloud.job.quartz.dao.QuartzJobLogMapper;
import com.taotao.cloud.job.quartz.entity.QuartzJobLog;
import com.taotao.cloud.job.quartz.param.QuartzJobLogQuery;
import com.taotao.cloud.job.quartz.service.QuartzJobLogService;
import com.taotao.cloud.job.quartz.vo.QuartzJobLogVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * 定时任务日志
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class QuartzJobLogServiceImpl implements QuartzJobLogService {
	private final QuartzJobLogMapper quartzJobLogMapper;

	@Override
	@Async("asyncExecutor")
	public void add(QuartzJobLog quartzJobLog) {
		quartzJobLog.setCreateTime(LocalDateTime.now());
		quartzJobLogMapper.insert(quartzJobLog);
	}

	@Override
	public PageResult<QuartzJobLogVO> page(QuartzJobLogQuery quartzJobLogQuery) {

		LambdaQueryWrapper<QuartzJobLog> wrapper = new LambdaQueryWrapper<QuartzJobLog>();
		wrapper
			.eq(QuartzJobLog::getClassName, quartzJobLogQuery.getClassName())
			.eq(Objects.nonNull(quartzJobLogQuery.getSuccess()), QuartzJobLog::getSuccess, quartzJobLogQuery.getSuccess())
			.orderByDesc(QuartzJobLog::getId);

		IPage<QuartzJobLog> quartzLogIPage = this.quartzJobLogMapper.selectPage(quartzJobLogQuery.buildMpPage(), wrapper);

		return PageResult.convertMybatisPage(quartzLogIPage, QuartzJobLogVO.class);
	}

	@Override
	public QuartzJobLogVO findById(Long id) {
		QuartzJobLogVO vo = new QuartzJobLogVO();
		QuartzJobLog quartzJobLog = quartzJobLogMapper.selectById(id);
		BeanUtil.copyProperties(quartzJobLog, vo);
		return vo;
	}

}
