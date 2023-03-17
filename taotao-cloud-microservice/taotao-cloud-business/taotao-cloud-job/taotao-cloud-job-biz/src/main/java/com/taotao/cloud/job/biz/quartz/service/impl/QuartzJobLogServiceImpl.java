package com.taotao.cloud.job.biz.quartz.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.taotao.cloud.common.model.PageResult;
import com.taotao.cloud.job.biz.quartz.entity.QuartzJobLogEntity;
import com.taotao.cloud.job.biz.quartz.mapper.QuartzJobLogMapper;
import com.taotao.cloud.job.biz.quartz.param.QuartzJobLogQuery;
import com.taotao.cloud.job.biz.quartz.service.QuartzJobLogService;
import com.taotao.cloud.job.biz.quartz.vo.QuartzJobLogVO;
import java.time.LocalDateTime;
import java.util.Objects;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * 定时任务日志
 */
@Service
public class QuartzJobLogServiceImpl implements QuartzJobLogService {

	private final QuartzJobLogMapper quartzJobLogMapper;

	public QuartzJobLogServiceImpl(QuartzJobLogMapper quartzJobLogMapper) {
		this.quartzJobLogMapper = quartzJobLogMapper;
	}

	@Override
	@Async("asyncExecutor")
	public void add(QuartzJobLogEntity quartzJobLog) {
		quartzJobLog.setCreateTime(LocalDateTime.now());
		quartzJobLogMapper.insert(quartzJobLog);
	}

	@Override
	public PageResult<QuartzJobLogVO> page(QuartzJobLogQuery quartzJobLogQuery) {

		LambdaQueryWrapper<QuartzJobLogEntity> wrapper = new LambdaQueryWrapper<>();
		wrapper
			.eq(QuartzJobLogEntity::getClassName, quartzJobLogQuery.getClassName())
			.eq(Objects.nonNull(quartzJobLogQuery.getSuccess()), QuartzJobLogEntity::getIsSuccess,
				quartzJobLogQuery.getSuccess())
			.orderByDesc(QuartzJobLogEntity::getId);

		IPage<QuartzJobLogEntity> quartzLogIPage = this.quartzJobLogMapper.selectPage(
			quartzJobLogQuery.buildMpPage(), wrapper);

		return PageResult.convertMybatisPage(quartzLogIPage, QuartzJobLogVO.class);
	}

	@Override
	public QuartzJobLogVO findById(Long id) {
		QuartzJobLogVO vo = new QuartzJobLogVO();
		QuartzJobLogEntity quartzJobLog = quartzJobLogMapper.selectById(id);
		BeanUtil.copyProperties(quartzJobLog, vo);
		return vo;
	}

}
