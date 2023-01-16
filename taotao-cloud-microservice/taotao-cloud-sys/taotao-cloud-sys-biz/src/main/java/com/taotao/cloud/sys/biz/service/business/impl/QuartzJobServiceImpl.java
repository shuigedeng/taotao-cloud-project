/*
 * Copyright (c) 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.taotao.cloud.sys.biz.service.business.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.github.pagehelper.PageInfo;
import com.taotao.cloud.common.exception.BusinessException;
import com.taotao.cloud.common.utils.bean.BeanUtils;
import com.taotao.cloud.common.utils.common.OrikaUtils;
import com.taotao.cloud.job.quartz.entity.QuartzJob;
import com.taotao.cloud.job.quartz.utils.QuartzManager;
import com.taotao.cloud.sys.api.model.dto.quartz.QuartzJobDto;
import com.taotao.cloud.sys.api.model.dto.quartz.QuartzJobQueryCriteria;
import com.taotao.cloud.sys.biz.mapper.IQuartzJobMapper;
import com.taotao.cloud.sys.biz.service.business.IQuartzJobService;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@CacheConfig(cacheNames = "quartzJob")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class QuartzJobServiceImpl extends
	ServiceImpl<IQuartzJobMapper, com.taotao.cloud.sys.biz.model.entity.quartz.QuartzJob> implements
	IQuartzJobService {

	private final QuartzManager quartzManager;

	public QuartzJobServiceImpl(QuartzManager quartzManager) {
		this.quartzManager = quartzManager;
	}

	@Override
	@Cacheable
	public Map<String, Object> queryAll(QuartzJobQueryCriteria criteria, Pageable pageable) {
		PageInfo<com.taotao.cloud.sys.biz.model.entity.quartz.QuartzJob> page = new PageInfo<>(
			queryAll(criteria));
		Map<String, Object> map = new LinkedHashMap<>(2);

		List<com.taotao.cloud.sys.biz.model.entity.quartz.QuartzJob> list = page.getList();
		List<QuartzJobDto> collect = list.stream()
			.filter(Objects::nonNull)
			.map(e -> OrikaUtils.convert(e, QuartzJobDto.class))
			.collect(Collectors.toList());

		map.put("content", collect);
		map.put("totalElements", page.getTotal());
		return map;
	}


	@Override
	@Cacheable
	public List<com.taotao.cloud.sys.biz.model.entity.quartz.QuartzJob> queryAll(
		QuartzJobQueryCriteria criteria) {
		// todo 需要修改查询条件
		LambdaQueryWrapper<com.taotao.cloud.sys.biz.model.entity.quartz.QuartzJob> query = Wrappers.<com.taotao.cloud.sys.biz.model.entity.quartz.QuartzJob>lambdaQuery()
			.eq(com.taotao.cloud.sys.biz.model.entity.quartz.QuartzJob::getId,
				criteria.getJobName());

		return baseMapper.selectList(query);
	}

	@Override
	public void download(List<QuartzJobDto> all, HttpServletResponse response) throws IOException {
		List<Map<String, Object>> list = new ArrayList<>();
		for (QuartzJobDto quartzJob : all) {
			Map<String, Object> map = new LinkedHashMap<>();
			map.put("Spring Bean名称", quartzJob.getBeanName());
			map.put("cron 表达式", quartzJob.getCronExpression());
			map.put("状态：1暂停、0启用", quartzJob.getIsPause());
			map.put("任务名称", quartzJob.getJobName());
			map.put("方法名称", quartzJob.getMethodName());
			map.put("参数", quartzJob.getParams());
			map.put("备注", quartzJob.getRemark());
			map.put("创建日期", quartzJob.getCreateTime());
			map.put("Spring Bean名称", quartzJob.getBeanName());
			map.put("cron 表达式", quartzJob.getCronExpression());
			map.put("状态：1暂停、0启用", quartzJob.getIsPause());
			map.put("任务名称", quartzJob.getJobName());
			map.put("方法名称", quartzJob.getMethodName());
			map.put("参数", quartzJob.getParams());
			map.put("备注", quartzJob.getRemark());
			map.put("创建日期", quartzJob.getCreateTime());
			map.put("Spring Bean名称", quartzJob.getBeanName());
			map.put("cron 表达式", quartzJob.getCronExpression());
			map.put("状态：1暂停、0启用", quartzJob.getIsPause());
			map.put("任务名称", quartzJob.getJobName());
			map.put("方法名称", quartzJob.getMethodName());
			map.put("参数", quartzJob.getParams());
			map.put("备注", quartzJob.getRemark());
			map.put("创建日期", quartzJob.getCreateTime());
			list.add(map);
		}

		//FileUtil.downloadExcel(list, response);
	}

	/**
	 * 更改定时任务状态
	 */
	@Override
	public void updateIsPause(com.taotao.cloud.sys.biz.model.entity.quartz.QuartzJob quartzJob) {
		if (quartzJob.getId().equals(1L)) {
			throw new BusinessException("该任务不可操作");
		}

		QuartzJob jobModel = new QuartzJob();
		BeanUtils.copyProperties(quartzJob, jobModel);

		if (quartzJob.getIsPause()) {
			quartzManager.resumeJob(jobModel);
		} else {
			quartzManager.pauseJob(jobModel);
		}

		quartzJob.setIsPause(!quartzJob.getIsPause());
		this.saveOrUpdate(quartzJob);
	}

	@Override
	public boolean save(com.taotao.cloud.sys.biz.model.entity.quartz.QuartzJob quartzJob) {
		QuartzJob jobModel = new QuartzJob();
		BeanUtils.copyProperties(quartzJob, jobModel);

		quartzManager.addJob(jobModel);
		return SqlHelper.retBool(baseMapper.insert(quartzJob));
	}

	@Override
	public boolean updateById(com.taotao.cloud.sys.biz.model.entity.quartz.QuartzJob quartzJob) {
		QuartzJob jobModel = new QuartzJob();
		BeanUtils.copyProperties(quartzJob, jobModel);

		// quartzManager.updateJobCron(jobModel);
		return SqlHelper.retBool(baseMapper.updateById(quartzJob));
	}

	/**
	 * 立即执行定时任务
	 *
	 * @param quartzJob /
	 */
	@Override
	public void execution(com.taotao.cloud.sys.biz.model.entity.quartz.QuartzJob quartzJob) {
		if (quartzJob.getId().equals(1L)) {
			throw new BusinessException("该任务不可操作");
		}

		QuartzJob jobModel = new QuartzJob();
		BeanUtils.copyProperties(quartzJob, jobModel);

		quartzManager.runJobNow(jobModel);
	}

	/**
	 * 查询启用的任务
	 */
	@Override
	public List<com.taotao.cloud.sys.biz.model.entity.quartz.QuartzJob> findByIsPauseIsFalse() {
		QuartzJobQueryCriteria criteria = new QuartzJobQueryCriteria();
		criteria.setIsPause(false);

		// todo 需要修改查询条件
		LambdaQueryWrapper<com.taotao.cloud.sys.biz.model.entity.quartz.QuartzJob> query = Wrappers.<com.taotao.cloud.sys.biz.model.entity.quartz.QuartzJob>lambdaQuery()
			.eq(com.taotao.cloud.sys.biz.model.entity.quartz.QuartzJob::getId,
				criteria.getJobName());

		return baseMapper.selectList(query);
	}

	@Override
	public void removeByIds(List<Integer> idList) {
		idList.forEach(id -> {
			com.taotao.cloud.sys.biz.model.entity.quartz.QuartzJob quartzJob = baseMapper.selectById(
				id);
			QuartzJob jobModel = new QuartzJob();
			BeanUtils.copyProperties(quartzJob, jobModel);
			quartzManager.deleteJob(jobModel);
		});

		baseMapper.deleteBatchIds(idList);
	}
}
