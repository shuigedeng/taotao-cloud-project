package com.taotao.cloud.sys.biz.tools.quartz.service;

import static org.quartz.impl.StdSchedulerFactory.PROP_SCHED_CLASS_LOAD_HELPER_CLASS;

import com.taotao.cloud.sys.biz.tools.core.exception.ToolException;
import com.taotao.cloud.sys.biz.tools.core.service.classloader.ClassloaderService;
import com.taotao.cloud.sys.biz.tools.core.service.file.FileManager;
import com.taotao.cloud.sys.biz.tools.database.service.JdbcService;
import java.io.IOException;
import java.io.StringReader;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.scheduling.support.CronSequenceGenerator;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;

@Service
public class QuartzService {
    @Autowired
    private JdbcService jdbcService;

    @Autowired
    private FileManager fileManager;

    @Autowired
    private ClassloaderService classloaderService;

    @Autowired
    private SpringJobFactory springJobFactory;

    private Map<String, Scheduler> schedulerMap = new ConcurrentHashMap<>();

    /**
     * 将数据库连接绑定到 quartz
     * @param connName
     * @param setttings
     */
    public void bindQuartz(String connName,Map<String,Object> setttings) throws Exception {
        Scheduler scheduler = schedulerMap.get(connName);
        if (scheduler != null) {
            log.info("quartz 已经绑定连接了,不能重复绑定");
            return ;
        }
        DataSource dataSource = jdbcService.dataSource(connName);
        SchedulerFactoryBean factory = new SchedulerFactoryBean();
        factory.setAutoStartup(true);
        factory.setDataSource(dataSource);

        setttings.put(PROP_SCHED_CLASS_LOAD_HELPER_CLASS,CascadingClassLoadHelperExtend.class.getName());
//        setttings.setProperty("org.quartz.jobStore.tablePrefix","qrtz_");
//        setttings.put("org.quartz.scheduler.instanceName","SchedulerFactory");
        Properties properties = new Properties();
        properties.putAll(setttings);
        // 序列化当前配置
        serializer(connName,properties);

        factory.setQuartzProperties(properties);
        factory.setJobFactory(springJobFactory);
        factory.afterPropertiesSet();
        scheduler = factory.getScheduler();

        schedulerMap.put(connName,scheduler);
    }

    /**
     * 一般任务数不会过千, 一次性查出来即可
     * @return
     */
    public List<TriggerTask> triggerTasks(String connName,String catalog,String schema) throws IOException, SQLException {
        String namespace = catalog;
        if (StringUtils.isNotBlank(schema)){
            namespace = schema;
        }
//        String sql = "select trigger_group,trigger_name,job_group,job_name,start_time,prev_fire_time,next_fire_time  from "+namespace+".qrtz_triggers qct  ";
        String sql = "select a.trigger_group,a.trigger_name,job_group,job_name,start_time,prev_fire_time,next_fire_time ,cron_expression " +
                "from "+namespace+".qrtz_triggers a inner join "+namespace+".qrtz_cron_triggers b on a.TRIGGER_GROUP = b .TRIGGER_GROUP and a.TRIGGER_NAME  = b.TRIGGER_NAME ";
        try {
            List<TriggerTask> triggerTasks = jdbcService.executeQuery(connName, sql, triggerTaskProcessor);
            for (TriggerTask triggerTask : triggerTasks) {
                try {
                    String cron = triggerTask.getCron();
                    List<String> nextTimes = new ArrayList<>();
                    CronSequenceGenerator cronSequenceGenerator = new CronSequenceGenerator(cron);
                    Date current = new Date();
                    for (int i = 0; i < 10; i++) {
                        current = cronSequenceGenerator.next(current);
                        nextTimes.add(DateFormatUtils.format(current, "yyyy-MM-dd HH:mm:ss"));
                    }
                    triggerTask.setNextTimes(nextTimes);
                }catch (IllegalArgumentException e){
                    log.error("cron 表达式[{}]错误,不能计算触发时间:{}",triggerTask.getCron(),e.getMessage());
                }
            }

            return triggerTasks;
        }catch (SQLException e){
            log.error(e.getMessage());
            throw new ToolException("当前连接[ "+connName+" ]没有 quartz 相关表");
        }
    }

    /**
     * 编辑或者添加一个 job
     * @param editJobParam
     */
    public void editJob(String connName,EditJobParam editJobParam) throws Exception {
        Scheduler scheduler = loadScheduler(connName);
        JobDetail jobDetail = scheduler.getJobDetail(editJobParam.getJobKey());
        // 创建 job
        JobKey jobKey = editJobParam.getJobKey();
        if (jobDetail != null){
            scheduler.deleteJob(jobKey);
        }
        ClassLoader classloader = classloaderService.getClassloader(editJobParam.getClassloaderName());
        Class<? extends Job> jobClass = (Class<? extends Job>) classloader.loadClass(editJobParam.getClassName());
        jobDetail = JobBuilder.newJob(jobClass).withIdentity(editJobParam.getJobKey()).withDescription(editJobParam.getDescription()).build();
        jobDetail.getJobDataMap().put("jobMethodName", editJobParam.getJobMethodName());

        // 创建触发器
        CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(editJobParam.getCron());
        TriggerKey triggerKey = TriggerKey.triggerKey("trigger" + jobKey.getName(), jobKey.getGroup());
        Trigger trigger = TriggerBuilder.newTrigger().withIdentity(triggerKey).startNow().withSchedule(cronScheduleBuilder).build();
        Date scheduleJob = scheduler.scheduleJob(jobDetail, trigger);
    }

    /**
     * 触发一个任务
     * @param connName
     * @param jobKey
     * @throws SchedulerException
     */
    @InvokeClassLoader
    public void trigger(String connName,JobKey jobKey) throws Exception {
        Scheduler scheduler = loadScheduler(connName);
        scheduler.triggerJob(jobKey);
    }

    /**
     * 暂停一个任务
     * @param connName
     * @param jobKey
     * @throws SchedulerException
     */
    @InvokeClassLoader
    public void pause(String connName,JobKey jobKey) throws Exception {
        Scheduler scheduler = loadScheduler(connName);
        scheduler.pauseJob(jobKey);
    }

    /**
     * 恢复一个任务
     * @param connName
     * @param jobKey
     */
    @InvokeClassLoader
    public void resume(String connName,JobKey jobKey) throws Exception {
        Scheduler scheduler = loadScheduler(connName);
        scheduler.resumeJob(jobKey);
    }

    /**
     * 删除一个任务
     * @param connName
     * @param triggerKey
     */
    @InvokeClassLoader
    public void remove(String connName,TriggerKey triggerKey,JobKey jobKey) throws Exception {
        Scheduler scheduler = loadScheduler(connName);
        // 停止触发器
        scheduler.pauseTrigger(triggerKey);

        // 停止任务调度
        scheduler.unscheduleJob(triggerKey);

        // 删除任务
        scheduler.deleteJob(jobKey);
    }

    static TriggerTaskProcessor triggerTaskProcessor = new TriggerTaskProcessor();

    static class TriggerTaskProcessor implements ResultSetHandler<List<TriggerTask>>{
        @Override
        public List<TriggerTask> handle(ResultSet rs) throws SQLException {
            List<TriggerTask> triggerTasks = new ArrayList<>();
            while (rs.next()){
                String triggerGroup = rs.getString("trigger_group");
                String triggerName = rs.getString("trigger_name");
                TriggerKey triggerKey = new TriggerKey(triggerName,triggerGroup);
                String jobGroup = rs.getString("job_group");
                String jobName = rs.getString("job_name");
                JobKey jobKey = new JobKey(jobName,jobGroup);

                long startTime = rs.getLong("start_time");
                long prevFireTime = rs.getLong("prev_fire_time");
                long nextFireTime = rs.getLong("next_fire_time");
                TriggerTask triggerTask = new TriggerTask(triggerKey, jobKey, startTime, prevFireTime, nextFireTime);
                String cronExpression = rs.getString("cron_expression");
                triggerTask.setCron(cronExpression);

                triggerTasks.add(triggerTask);
            }
            return triggerTasks;
        }
    }

    /**
     * 创建一个日程调度工具
     * @param connName
     * @return
     * @throws Exception
     */
    public Scheduler loadScheduler(String connName) throws Exception {
        Scheduler scheduler = schedulerMap.get(connName);
        if (scheduler == null){
            throw new ToolException("当前连接 "+connName+" 未找到绑定的调度器,请先执行绑定操作");
        }

        return scheduler;
    }

    public static final String MODULE = "quartz";

    @PostConstruct
    public void register() throws IOException {
//        pluginManager.register(PluginDto.builder().module("monitor")
//                .name("quartz").author("9420")
//                .logo("null.png").desc("可视化任务调度").build());

        // 加载序列化的连接配置
        List<String> settings = fileManager.simpleConfigNames(MODULE, "settings");
        for (String connName : settings) {
            String setting = fileManager.readConfig(MODULE, "settings/" + connName);
            Properties properties = new Properties();
            StringReader stringReader = new StringReader(setting);
            properties.load(stringReader);stringReader.close();
            try {
                bindQuartz(connName,new HashMap<String,Object>((Map)properties));
            } catch (Exception e) {
                log.error("加载以前绑定的调度器失败,连接名为 {},异常信息为:{}",connName,e.getMessage());
            }
        }
    }

    /**
     * 将当前连接配置序列化到文件
     * @param connName
     * @param properties
     */
    public void serializer(String connName,Properties properties) throws IOException {
        String jsonString = JSON.toJSONString(properties);
        fileManager.writeConfig(MODULE,"settings/"+connName,jsonString);
    }
}
