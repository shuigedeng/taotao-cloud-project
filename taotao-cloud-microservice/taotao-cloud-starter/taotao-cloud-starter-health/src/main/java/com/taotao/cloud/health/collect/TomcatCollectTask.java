package com.taotao.cloud.health.collect;

import com.yh.csx.bsf.core.util.ContextUtils;
import com.yh.csx.bsf.core.util.PropertyUtils;
import com.yh.csx.bsf.core.util.ReflectionUtils;
import com.yh.csx.bsf.health.base.AbstractCollectTask;
import com.yh.csx.bsf.health.base.FieldReport;
import lombok.Data;
import lombok.val;
import lombok.var;
import org.springframework.boot.web.embedded.tomcat.TomcatWebServer;

import java.util.concurrent.ThreadPoolExecutor;


/**
 * @author: chejiangyi
 * @version: 2019-08-03 11:59
 **/
public class TomcatCollectTask extends AbstractCollectTask {

    public TomcatCollectTask() {

    }

    @Override
    public int getTimeSpan() {
        return PropertyUtils.getPropertyCache("bsf.health.tomcat.timeSpan", 20);
    }

    @Override
    public String getDesc() {
        return "tomcat性能采集";
    }

    @Override
    public String getName() {
        return "tomcat.info";
    }

    @Override
    public boolean getEnabled() {
        return PropertyUtils.getPropertyCache("bsf.health.tomcat.enabled", true);
    }

    @Override
    protected Object getData() {
        val context = ContextUtils.getConfigurableWebServerApplicationContext();
        if (context != null) {
            val webServer = context.getWebServer();
            if (webServer != null && webServer instanceof TomcatWebServer) {
                //var item = ((TomcatWebServer) webServer).getTomcat().getConnector().getProtocolHandler().getExecutor();
                var executor = ReflectionUtils.callMethod(ReflectionUtils.callMethod(ReflectionUtils.callMethod(
                        ReflectionUtils.callMethod(webServer,"getTomcat",null)
                        ,"getConnector",null),"getProtocolHandler",null),"getExecutor",null);
                var poolCls = ReflectionUtils.tryClassForName("org.apache.tomcat.util.threads.ThreadPoolExecutor");

                if(executor!=null &&  poolCls.isAssignableFrom(executor.getClass())) {
                   // val executor = ReflectionUtils.tryGetFieldValue(executor,"executor",null);
                    if (executor!=null && executor instanceof ThreadPoolExecutor){
                        TomcatInfo tomcatInfo = new TomcatInfo();
                        val pool = (ThreadPoolExecutor) executor;
                        tomcatInfo.activeCount = pool.getActiveCount();
                        tomcatInfo.corePoolSize = pool.getCorePoolSize();
                        tomcatInfo.poolSizeCount = pool.getPoolSize();
                        tomcatInfo.poolSizeMax =  pool.getMaximumPoolSize();
                        tomcatInfo.poolSizeLargest = pool.getLargestPoolSize();
                        tomcatInfo.queueSize = pool.getQueue().size();
                        tomcatInfo.taskCount = pool.getTaskCount();
                        tomcatInfo.taskCompleted = pool.getCompletedTaskCount();
                        return tomcatInfo;
                    }
                }
            }

        }
        return null;
    }


    @Data
    private static class TomcatInfo {
        @FieldReport(name = "tomcat.threadPool.active.count", desc = "tomcat 线程池活动线程数")
        private Integer activeCount;
        @FieldReport(name = "tomcat.threadPool.core.poolSize", desc = "tomcat 线程池核心线程数")
        private Integer corePoolSize;
        @FieldReport(name = "tomcat.threadPool.poolSize.largest", desc = "tomcat 线程池历史最大线程数")
        private Integer poolSizeLargest;
        @FieldReport(name = "tomcat.threadPool.poolSize.max", desc = "tomcat 线程池最大线程数")
        private Integer poolSizeMax;
        @FieldReport(name = "tomcat.threadPool.poolSize.count", desc = "tomcat 线程池当前线程数")
        private Integer poolSizeCount;
        @FieldReport(name = "tomcat.threadPool.queue.size", desc = "tomcat 线程池当前排队等待任务数")
        private Integer queueSize;
        @FieldReport(name = "tomcat.threadPool.task.count", desc = "tomcat 线程池历史任务数")
        private Long taskCount;
        @FieldReport(name = "tomcat.threadPool.task.completed", desc = "tomcat 线程池已完成任务数")
        private Long taskCompleted;
//        @FieldReport(name = "tomcat.threadPool.task.hook.error", desc = "tomcat 线程池拦截上一次每秒出错次数")
//        private Integer taskHookError;
//        @FieldReport(name = "tomcat.threadPool.task.hook.success", desc = "tomcat 线程池拦截上一次每秒成功次数")
//        private Integer taskHookSuccess;
//        @FieldReport(name = "tomcat.threadPool.task.hook.current", desc = "tomcat 线程池拦截当前执行任务数")
//        private Integer taskHookCurrent;
//        @FieldReport(name = "tomcat.threadPool.task.hook.list", desc = "tomcat 线程池拦截历史最大耗时任务列表")
//        private String taskHookList;

    }
}
