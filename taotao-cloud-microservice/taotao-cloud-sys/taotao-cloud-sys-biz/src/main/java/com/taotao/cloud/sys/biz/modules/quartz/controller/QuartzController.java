package com.taotao.cloud.sys.biz.modules.quartz.controller;

import com.sanri.tools.modules.database.service.meta.dtos.Namespace;
import com.sanri.tools.modules.quartz.dtos.TriggerTask;
import com.sanri.tools.modules.quartz.service.EditJobParam;
import com.sanri.tools.modules.quartz.service.QuartzService;
import com.sanri.tools.modules.quartz.service.QuartzServiceNew;
import lombok.Data;
import org.quartz.JobKey;
import org.quartz.SchedulerException;
import org.quartz.TriggerKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/quartz")
@Validated
public class QuartzController {
    @Autowired
    private QuartzService quartzService;
    @Autowired
    private QuartzServiceNew quartzServiceNew;

    /**
     * 添加或修改一个 job
     * @param connName 连接名
     * @param editJobParam 任务参数
     */
    @PostMapping("/{connName}/editJob")
    public void editJob(@PathVariable("connName") String connName, @RequestBody @Valid EditJobParam editJobParam) throws Exception {
        quartzService.editJob(connName, editJobParam.getNamespace(), editJobParam);
    }


    /**
     * 查询所有的任务列表
     * @param connName 连接名
     * @param catalog 数据库  catalog
     * @param schema  数据库 schema
     * @return
     * @throws IOException
     * @throws SQLException
     */
    @GetMapping("/triggers")
    public List<TriggerTask> triggers(@NotNull String connName, Namespace namespace,String tablePrefix) throws Exception {
        return quartzServiceNew.triggerTasks(connName,namespace,tablePrefix);
    }

    /**
     * 触发任务
     * @param connName 连接名
     * @param group 分组名
     * @param name 任务名
     * @throws SchedulerException
     */
    @GetMapping("/trigger")
    public void trigger(@NotNull String connName,Namespace namespace,String group,String name) throws Exception {
        JobKey jobKey = new JobKey(name, group);
        quartzService.trigger(connName,namespace,jobKey);
    }

    /**
     * 暂停
     * @param connName 连接名
     * @param name 任务名
     * @param group 任务分组
     * @throws SchedulerException
     */
    @GetMapping("/pause")
    public void pause(@NotNull String connName,Namespace namespace,String name,String group) throws Exception {
        JobKey jobKey = new JobKey(name, group);
        quartzService.pause(connName,namespace,jobKey);
    }

    /**
     * 恢复
     * @param connName 连接名
     * @param name 任务名
     * @param group 任务分组
     * @throws SchedulerException
     */
    @GetMapping("/resume")
    public void resume(@NotNull String connName,Namespace namespace,String name,String group) throws Exception {
        JobKey jobKey = new JobKey(name, group);
        quartzService.resume(connName,namespace,jobKey);
    }

    /**
     * 移除
     * @param connName 连接名称
     * @param triggerName 触发器名称
     * @param triggerGroup 触发器分组
     * @param jobName 任务名
     * @param jobGroup 任务分组
     * @throws SchedulerException
     */
    @GetMapping("/remove")
    public void remove(@NotNull String connName,Namespace namespace,@NotNull String triggerName,@NotNull String triggerGroup,@NotNull String jobName,@NotNull String jobGroup) throws Exception {
        TriggerKey triggerKey = TriggerKey.triggerKey(triggerName, triggerGroup);
        JobKey jobKey = JobKey.jobKey(jobName, jobGroup);
        quartzService.remove(connName,namespace,triggerKey,jobKey);
    }
}
