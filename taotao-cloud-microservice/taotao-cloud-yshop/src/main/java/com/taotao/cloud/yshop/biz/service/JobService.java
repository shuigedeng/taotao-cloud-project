/**
 * Copyright (C) 2018-2020
 * All rights reserved, Designed By www.yixiang.co
 * 注意：
 * 本软件为www.yixiang.co开发研制
 */
package com.taotao.cloud.system.biz.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.taotao.cloud.system.api.dto.JobDto;
import com.taotao.cloud.system.api.dto.JobQueryCriteria;
import com.taotao.cloud.system.biz.entity.Job;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface JobService extends IService<Job> {

    /**
     * 查询数据分页
     * @param criteria 条件
     * @param pageable 分页参数
     * @return Map<String, Object>
     */
    Map<String, Object> queryAll(JobQueryCriteria criteria, Pageable pageable);

    /**
     * 查询所有数据不分页
     * @param criteria 条件参数
     * @return List<JobDto>
     */
    List<Job> queryAll(JobQueryCriteria criteria);

    /**
     * 导出数据
     * @param all 待导出的数据
     * @param response /
     * @throws IOException /
     */
    void download(List<JobDto> all, HttpServletResponse response) throws IOException;
}
