/**
 * Copyright (C) 2018-2020
 * All rights reserved, Designed By www.yixiang.co
 * 注意：
 * 本软件为www.yixiang.co开发研制
 */
package com.taotao.cloud.sys.biz.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageInfo;
import com.taotao.cloud.system.api.dto.QuartzLogDto;
import com.taotao.cloud.system.api.dto.QuartzLogQueryCriteria;
import com.taotao.cloud.system.biz.entity.QuartzLog;
import com.taotao.cloud.system.biz.mapper.QuartzLogMapper;
import com.taotao.cloud.system.biz.service.QuartzLogService;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

// 默认不使用缓存
//import org.springframework.cache.annotation.CacheConfig;
//import org.springframework.cache.annotation.CacheEvict;
//import org.springframework.cache.annotation.Cacheable;

/**
 * @author hupeng
 * @date 2020-05-13
 */
@Service
//@CacheConfig(cacheNames = "quartzLog")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class QuartzLogServiceImpl extends ServiceImpl<QuartzLogMapper, QuartzLog> implements
	QuartzLogService {

    private final IGenerator generator;

	public QuartzLogServiceImpl(IGenerator generator) {

		this.generator = generator;
	}

	@Override
    //@Cacheable
    public Map<String, Object> queryAll(QuartzLogQueryCriteria criteria, Pageable pageable) {
        getPage(pageable);
        PageInfo<QuartzLog> page = new PageInfo<>(queryAll(criteria));
        Map<String, Object> map = new LinkedHashMap<>(2);
        map.put("content", generator.convert(page.getList(), QuartzLogDto.class));
        map.put("totalElements", page.getTotal());
        return map;
    }


    @Override
    //@Cacheable
    public List<QuartzLog> queryAll(QuartzLogQueryCriteria criteria) {
        return baseMapper.selectList(QueryHelpPlus.getPredicate(QuartzLog.class, criteria));
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
            map.put(" baenName", quartzLog.getBaenName());
            map.put(" createTime", quartzLog.getCreateTime());
            map.put(" cronExpression", quartzLog.getCronExpression());
            map.put(" exceptionDetail", quartzLog.getExceptionDetail());
            map.put(" isSuccess", quartzLog.getIsSuccess());
            map.put(" jobName", quartzLog.getJobName());
            map.put(" methodName", quartzLog.getMethodName());
            map.put(" params", quartzLog.getParams());
            map.put(" time", quartzLog.getTime());
            map.put("任务名称", quartzLog.getBaenName());
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
        FileUtil.downloadExcel(list, response);
    }
}
