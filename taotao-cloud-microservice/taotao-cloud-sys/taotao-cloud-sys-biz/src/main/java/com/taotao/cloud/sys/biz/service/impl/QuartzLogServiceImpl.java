/**
 * Copyright (C) 2018-2020 All rights reserved, Designed By www.yixiang.co 注意：
 * 本软件为www.yixiang.co开发研制
 */
package com.taotao.cloud.sys.biz.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageInfo;
import com.taotao.cloud.common.utils.bean.BeanUtil;
import com.taotao.cloud.sys.api.dto.quartz.QuartzLogDto;
import com.taotao.cloud.sys.api.dto.quartz.QuartzLogQueryCriteria;
import com.taotao.cloud.sys.biz.entity.quartz.QuartzLog;
import com.taotao.cloud.sys.biz.mapper.IQuartzLogMapper;
import com.taotao.cloud.sys.biz.service.IQuartzLogService;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

// 默认不使用缓存
//import org.springframework.cache.annotation.CacheConfig;
//import org.springframework.cache.annotation.CacheEvict;
//import org.springframework.cache.annotation.Cacheable;

@Service
//@CacheConfig(cacheNames = "quartzLog")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class QuartzLogServiceImpl extends ServiceImpl<IQuartzLogMapper, QuartzLog> implements
	IQuartzLogService {

	@Override
	//@Cacheable
	public Map<String, Object> queryAll(QuartzLogQueryCriteria criteria, Pageable pageable) {
		PageInfo<QuartzLog> page = new PageInfo<>(queryAll(criteria));
		Map<String, Object> map = new LinkedHashMap<>(2);

		List<QuartzLog> list = page.getList();
		List<QuartzLogDto> collect = list.stream().filter(Objects::nonNull).map(e -> {
			QuartzLogDto dto = new QuartzLogDto();
			BeanUtil.copyProperties(e, dto);
			return dto;
		}).collect(Collectors.toList());

		map.put("content", collect);
		map.put("totalElements", page.getTotal());
		return map;
	}


	@Override
	//@Cacheable
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
			map.put("isSuccess", quartzLog.getSuccess());
			map.put("jobName", quartzLog.getJobName());
			map.put("methodName", quartzLog.getMethodName());
			map.put("params", quartzLog.getParams());
			map.put("time", quartzLog.getTime());
			map.put("定时任务名称", quartzLog.getBeanName());
			map.put("Bean名称 ", quartzLog.getCreateTime());
			map.put("cron表达式", quartzLog.getCronExpression());
			map.put("异常详细 ", quartzLog.getExceptionDetail());
			map.put("状态", quartzLog.getSuccess());
			map.put("任务名称", quartzLog.getJobName());
			map.put("方法名称", quartzLog.getMethodName());
			map.put("参数", quartzLog.getParams());
			map.put("耗时（毫秒）", quartzLog.getTime());
			list.add(map);
		}
		//FileUtil.downloadExcel(list, response);
	}
}
