package com.taotao.cloud.workflow.biz.job;

import jnpf.database.data.DataSourceContextHolder;
import jnpf.engine.model.flowengine.FlowModel;
import jnpf.engine.service.FlowTaskNewService;
import jnpf.util.StringUtil;
import jnpf.util.context.SpringContext;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;

/**
 * @author JNPF开发平台组
 * @version V3.1.0
 * @copyright 引迈信息技术有限公司
 * @date 2021/3/12 15:31
 */
@Slf4j
public class WorkJob implements Job {

    @Override
    public void execute(JobExecutionContext context) {
        System.out.println("进入调度");
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
