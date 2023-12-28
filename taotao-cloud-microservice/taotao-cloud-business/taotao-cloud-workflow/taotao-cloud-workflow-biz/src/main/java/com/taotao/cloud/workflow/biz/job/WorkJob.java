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

package com.taotao.cloud.workflow.biz.job;

import com.taotao.cloud.workflow.biz.common.model.engine.flowengine.FlowModel;
import com.taotao.cloud.workflow.biz.engine.service.FlowTaskNewService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;

/** */
@Slf4j
public class WorkJob implements Job {

    @Override
    public void execute(JobExecutionContext context) {
        LogUtils.info("进入调度");
        JobDataMap dataMap = context.getMergedJobDataMap();
        FlowModel model = dataMap.get("model") != null ? (FlowModel) dataMap.get("model") : null;
        String type = dataMap.getString("type");
        String id = dataMap.getString("id");
        String tenantId = dataMap.getString("tenantId");
        String tenantDbConnectionString = dataMap.getString("tenantDbConnectionString");
        try {
            if (model != null) {
                if (StringUtil.isNotEmpty(tenantId)) {
                    DataSourceContextHolder.setDatasource(tenantId, tenantDbConnectionString);
                }
                FlowTaskNewService taskService = SpringContext.getBean(FlowTaskNewService.class);
                if ("1".equals(type)) {
                    taskService.audit(id, model);
                } else {
                    taskService.reject(id, model);
                }
            }
        } catch (Exception e) {
            log.error("工作流调度报错:" + e.getMessage());
        }
    }
}
