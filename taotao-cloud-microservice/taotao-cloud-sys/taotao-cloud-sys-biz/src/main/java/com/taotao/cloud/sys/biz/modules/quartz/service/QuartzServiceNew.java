package com.taotao.cloud.sys.biz.modules.quartz.service;

import com.alibaba.druid.pool.DruidDataSource;
import com.sanri.tools.modules.core.exception.ToolException;
import com.sanri.tools.modules.classloader.ClassloaderService;
import com.sanri.tools.modules.core.service.file.FileManager;
import com.sanri.tools.modules.database.service.JdbcDataService;
import com.sanri.tools.modules.database.service.connect.ConnDatasourceAdapter;
import com.sanri.tools.modules.database.service.meta.dtos.Namespace;
import com.sanri.tools.modules.quartz.dtos.TriggerTask;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.TriggerKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.scheduling.support.CronSequenceGenerator;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static org.quartz.impl.StdSchedulerFactory.PROP_SCHED_CLASS_LOAD_HELPER_CLASS;

@Service
@Slf4j
public class QuartzServiceNew {
    @Autowired
    private JdbcDataService jdbcDataService;
    @Autowired
    private ConnDatasourceAdapter connDatasourceAdapter;

    @Autowired
    private FileManager fileManager;

    @Autowired
    private ClassloaderService classloaderService;

    @Autowired
    private SpringJobFactory springJobFactory;

    /**
     * connName => Namespace => Scheduler
     */
    private Map<String, Map<Namespace,Scheduler>> schedulerMap = new ConcurrentHashMap<>();

    public static TriggerTaskProcessor triggerTaskProcessor = new TriggerTaskProcessor();

    static class TriggerTaskProcessor implements ResultSetHandler<List<TriggerTask>> {
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
     * 检查当前库是否存在定时任务的数据表并获取所有定时任务数据
     * @param connName
     * @param namespace
     * @param tablePrefix
     */
    public List<TriggerTask> triggerTasks(String connName, Namespace namespace,String tablePrefix) throws Exception {
        String sql = "SELECT A.TRIGGER_GROUP,A.TRIGGER_NAME,JOB_GROUP,JOB_NAME,START_TIME,PREV_FIRE_TIME,NEXT_FIRE_TIME ,CRON_EXPRESSION " +
                "FROM "+tablePrefix+"TRIGGERS A INNER JOIN "+tablePrefix+"CRON_TRIGGERS B ON A.TRIGGER_GROUP = B .TRIGGER_GROUP AND A.TRIGGER_NAME  = B.TRIGGER_NAME ";
        try {
            List<TriggerTask> triggerTasks = jdbcDataService.executeQuery(connName, sql, triggerTaskProcessor,namespace);
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

            // 创建调度器, 如果有定时任务表的话
            createSchedule(connName, namespace, tablePrefix);

            return triggerTasks;
        }catch (SQLException e){
            log.error(e.getMessage());
            throw new ToolException("当前连接[ "+connName+" ]没有 quartz 相关表");
        }
    }

    /**
     * 获取调度器, 有可能为空
     * @param connName
     * @param namespace
     * @return
     */
    public Scheduler loadScheduler(String connName, Namespace namespace){
        final Map<Namespace, Scheduler> namespaceSchedulerMap = schedulerMap.computeIfAbsent(connName, k -> new ConcurrentHashMap<>());
        return namespaceSchedulerMap.get(namespace);
    }

    /**
     * 创建 Schedule
     * @param connName
     * @param namespace
     * @param tablePrefix
     * @throws Exception
     */
    private void createSchedule(String connName, Namespace namespace, String tablePrefix) throws Exception {
        final Map<Namespace, Scheduler> namespaceSchedulerMap = schedulerMap.computeIfAbsent(connName, k -> new ConcurrentHashMap<>());
        if (!namespaceSchedulerMap.containsKey(namespace)) {
            // 如果有定时任务数据表, 并且没有创建 Schedule ,则创建
            DruidDataSource dataSource = connDatasourceAdapter.poolDataSource(connName, namespace);
            SchedulerFactoryBean factory = new SchedulerFactoryBean();
            factory.setAutoStartup(true);
            factory.setDataSource(dataSource);

            Map<String, Object> settings = new HashMap<>();
            settings.put(PROP_SCHED_CLASS_LOAD_HELPER_CLASS, CascadingClassLoadHelperExtend.class.getName());
            settings.put("org.quartz.jobStore.tablePrefix", tablePrefix);
            settings.put("org.quartz.scheduler.instanceName", "SchedulerFactory");
            Properties properties = new Properties();
            properties.putAll(settings);
            factory.setQuartzProperties(properties);
            factory.setJobFactory(springJobFactory);
            factory.afterPropertiesSet();
            Scheduler scheduler = factory.getScheduler();

            namespaceSchedulerMap.put(namespace, scheduler);
        }
    }
}
