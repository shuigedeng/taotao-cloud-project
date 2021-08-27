package com.taotao.cloud.health.collect;

import com.yh.csx.bsf.core.thread.ThreadPool;
import com.yh.csx.bsf.core.util.LogUtils;
import com.yh.csx.bsf.core.util.StringUtils;
import com.yh.csx.bsf.health.base.AbstractCollectTask;
import com.yh.csx.bsf.health.base.EnumWarnType;
import com.yh.csx.bsf.health.base.Report;
import com.yh.csx.bsf.health.config.HealthProperties;
import com.yh.csx.bsf.health.strategy.DefaultWarnStrategy;
import lombok.val;
import lombok.var;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: chejiangyi
 * @version: 2019-07-23 18:47
 **/
public class HealthCheckProvider implements AutoCloseable {
    protected List<AbstractCollectTask> checkTasks = new ArrayList<>();
    protected DefaultWarnStrategy strategy = DefaultWarnStrategy.Default;
    private boolean isclose=false;
    public void registerCollectTask(AbstractCollectTask task){
        checkTasks.add(task);
    }


    public HealthCheckProvider(){
        isclose =false;
        registerCollectTask(new CpuCollectTask());
        registerCollectTask(new IOCollectTask());
        registerCollectTask(new MemeryCollectTask());
        registerCollectTask(new ThreadCollectTask());
        registerCollectTask(new UnCatchExceptionCollectTask());
        registerCollectTask(new BsfThreadPoolSystemCollectTask());
        registerCollectTask(new BsfEurekaCollectTask());
        registerCollectTask(new MybatisCollectTask());
        registerCollectTask(new DataSourceCollectTask());
        registerCollectTask(new TomcatCollectTask());
        registerCollectTask(new JedisCollectTask());
        registerCollectTask(new NetworkCollectTask());
        registerCollectTask(new XxlJobCollectTask());
        registerCollectTask(new FileCollectTask());
        registerCollectTask(new RocketMQCollectTask());
        registerCollectTask(new HttpPoolCollectTask());
        registerCollectTask(new CatCollectTask());
        registerCollectTask(new ElasticSearchCollectTask());
        registerCollectTask(new ElkCollectTask());
        registerCollectTask(new DoubtApiCollectTask());
        registerCollectTask(new LogStatisticCollectTask());

        ThreadPool.System.submit("bsf系统任务:HealthCheckProvider采集任务",()->{
            while (!ThreadPool.System.isShutdown()&&!isclose){
                try{
                    run();
                }catch (Exception e){
                    LogUtils.warn(HealthCheckProvider.class,HealthProperties.Project,"run 循环采集出错",e);
                }
                try{Thread.sleep(HealthProperties.Default().getBsfHealthTimeSpan()*1000); }catch (Exception e){}
            }
        });
    }


    public Report getReport(boolean isAnalyse){
        Report report = new Report().setDesc("健康检查报表").setName("bsf.health.report");
        for(AbstractCollectTask task:checkTasks){
            if(task.getEnabled()) {
                try {
                    val report2 = task.getReport();
                    if(report2!=null) {
                        report.put(task.getName(), report2.setDesc(task.getDesc()).setName(task.getName()));
                    }
                }
                catch (Exception e){
                    LogUtils.error(HealthCheckProvider.class,HealthProperties.Project,task.getName()+"采集获取报表出错",e);
                }

            }
        }
        if(isAnalyse)
        {
            report = strategy.analyse(report);
        }
        return report;
    }

    public void run(){
        var report = getReport(false);
        val text = strategy.analyseText(report);
        if(StringUtils.isEmpty(text))
        {
            return;
        }
        AbstractCollectTask.notifyMessage(EnumWarnType.ERROR,"bsf健康检查",text);

    }
    @Override
    public void close(){
        isclose =true;
        for(val task: checkTasks){
            try{
                task.close();
            }
            catch (Exception exp){
                LogUtils.warn(HealthCheckProvider.class,HealthProperties.Project,"close资源释放出错",exp);
            }
        }
    }
}
