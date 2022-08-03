package com.taotao.cloud.sys.biz.api.controller.tools.quartz.service;

import com.taotao.cloud.sys.biz.api.controller.tools.database.service.meta.dtos.Namespace;
import lombok.Data;
import org.quartz.JobKey;

import javax.validation.constraints.NotNull;

@Data
public class EditJobParam {
    private Namespace namespace;
    /**
     * 任务名
     */
    @NotNull
    private String name;
    /**
     * 任务分组
     */
    @NotNull
    private String group;

    /**
     * 描述
     */
    private String description;
    /**
     * 类名
     */
    @NotNull
    private String className;
    /**
     * 类加载器名称
     */
    private String classloaderName;
    /**
     * 任务方法名称
     */
    @NotNull
    private String jobMethodName;
    /**
     * cron 表达式
     */
    @NotNull
    private String cron;

    public JobKey getJobKey(){
        return JobKey.jobKey(name,group);
    }
}
