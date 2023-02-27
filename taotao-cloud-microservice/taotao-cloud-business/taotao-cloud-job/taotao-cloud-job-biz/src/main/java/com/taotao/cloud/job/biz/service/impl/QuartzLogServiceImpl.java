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
package com.taotao.cloud.job.biz.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageInfo;
import com.taotao.cloud.common.utils.bean.BeanUtils;
import com.taotao.cloud.sys.api.model.dto.quartz.QuartzLogDto;
import com.taotao.cloud.sys.api.model.dto.quartz.QuartzLogQueryCriteria;
import com.taotao.cloud.sys.biz.mapper.IQuartzLogMapper;
import com.taotao.cloud.sys.biz.model.entity.quartz.QuartzLog;
import com.taotao.cloud.sys.biz.service.business.IQuartzLogService;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
@CacheConfig(cacheNames = "quartzLog")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class QuartzLogServiceImpl extends ServiceImpl<IQuartzLogMapper, QuartzLog> implements
	IQuartzLogService {

	@Override
	@Cacheable
	public Map<String, Object> queryAll(QuartzLogQueryCriteria criteria, Pageable pageable) {
		PageInfo<QuartzLog> page = new PageInfo<>(queryAll(criteria));
		Map<String, Object> map = new LinkedHashMap<>(2);

		List<QuartzLog> list = page.getList();
		List<QuartzLogDto> collect = list.stream().filter(Objects::nonNull).map(e -> {
			QuartzLogDto dto = new QuartzLogDto();
			BeanUtils.copyProperties(e, dto);
			return dto;
		}).collect(Collectors.toList());

		map.put("content", collect);
		map.put("totalElements", page.getTotal());
		return map;
	}


	@Override
	@Cacheable
	public List<QuartzLog> queryAll(QuartzLogQueryCriteria criteria) {
		LambdaQueryWrapper<QuartzLog> query = Wrappers.<QuartzLog>lambdaQuery()
			.eq(QuartzLog::getId, "1");

		return baseMapper.selectList(query);
	}

	/**
	 * 导出数据
	 *
	 * @param all      待导出的数据
	 * @param response /
	 * @throws IOException /
	 */
	@Override
	public void download(List<QuartzLogDto> all, HttpServletResponse response) throws IOException {
		List<Map<String, Object>> list = new ArrayList<>();
		for (QuartzLogDto quartzLog : all) {
			Map<String, Object> map = new LinkedHashMap<>();
			map.put("beanName", quartzLog.getBeanName());
			map.put("createTime", quartzLog.getCreateTime());
			map.put("cronExpression", quartzLog.getCronExpression());
			map.put("exceptionDetail", quartzLog.getExceptionDetail());
			map.put("isSuccess", quartzLog.getIsSuccess());
			map.put("jobName", quartzLog.getJobName());
			map.put("methodName", quartzLog.getMethodName());
			map.put("params", quartzLog.getParams());
			map.put("time", quartzLog.getTime());
			map.put("定时任务名称", quartzLog.getBeanName());
			map.put("Bean名称 ", quartzLog.getCreateTime());
			map.put("cron表达式", quartzLog.getCronExpression());
			map.put("异常详细 ", quartzLog.getExceptionDetail());
			map.put("状态", quartzLog.getIsSuccess());
			map.put("任务名称", quartzLog.getJobName());
			map.put("方法名称", quartzLog.getMethodName());
			map.put("参数", quartzLog.getParams());
			map.put("耗时（毫秒）", quartzLog.getTime());
			list.add(map);
		}
		//FileUtil.downloadExcel(list, response);
	}
}
