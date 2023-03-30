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

package com.taotao.cloud.job.biz.quartz.service;

import com.taotao.cloud.common.model.PageResult;
import com.taotao.cloud.job.biz.quartz.entity.QuartzJobLogEntity;
import com.taotao.cloud.job.biz.quartz.param.QuartzJobLogQuery;
import com.taotao.cloud.job.biz.quartz.vo.QuartzJobLogVO;

/** 定时任务日志 */
public interface QuartzJobLogService {

    /** 添加 */
    public void add(QuartzJobLogEntity quartzJobLog);

    /** 分页 */
    public PageResult<QuartzJobLogVO> page(QuartzJobLogQuery quartzJobLogQuery);

    /** 单条 */
    public QuartzJobLogVO findById(Long id);
}
