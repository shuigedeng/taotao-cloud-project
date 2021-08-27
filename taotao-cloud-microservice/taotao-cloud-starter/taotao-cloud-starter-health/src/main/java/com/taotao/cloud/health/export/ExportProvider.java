package com.taotao.cloud.health.export;

import com.yh.csx.bsf.core.thread.ThreadPool;
import com.yh.csx.bsf.core.util.ContextUtils;
import com.yh.csx.bsf.core.util.LogUtils;
import com.yh.csx.bsf.health.base.AbstractExport;
import com.yh.csx.bsf.health.collect.HealthCheckProvider;
import com.yh.csx.bsf.health.config.ExportProperties;
import com.yh.csx.bsf.health.config.HealthProperties;
import lombok.val;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: chejiangyi
 * @version: 2019-08-14 11:01
 **/
public class ExportProvider {
    private boolean isClose = true;
    protected List<AbstractExport> exports = new ArrayList<>();
    public void registerCollectTask(AbstractExport export){
        exports.add(export);
    }
    public void start() {
        isClose=false;
        if(ExportProperties.Default().isBsfCatEnabled()) {
            registerCollectTask(new CatExport());
        }
        if(ExportProperties.Default().isBsfElkEnabled()) {
            registerCollectTask(new ElkExport());
        }
        ThreadPool.System.submit("bsf系统任务:ExportProvider采集上传任务",()->{
            while (!ThreadPool.System.isShutdown()&&!isClose){
                try{
                    run();
                }catch (Exception e){
                    LogUtils.error(ExportProvider.class, HealthProperties.Project,"run 循环上传报表出错",e);
                }
                try{Thread.sleep(ExportProperties.Default().getBsfHealthExportTimeSpan()*1000); }catch (Exception e){}
            }
        });
        for(val e:exports){
            try {
                e.start();
            }catch (Exception ex){
                LogUtils.error(ExportProvider.class, HealthProperties.Project,e.getClass().getName()+"启动出错",ex);
            }
        }
    }
    public void run(){
        val healthProvider = ContextUtils.getBean(HealthCheckProvider.class,false);
        if(healthProvider!=null) {
            val report = healthProvider.getReport(false);
            for (val e : exports) {
                e.run(report);
            }
        }
    }

    public void close() {
        isClose=true;
        for(val e:exports){
            try {
                e.close();
            }catch (Exception ex){
                LogUtils.error(ExportProvider.class, HealthProperties.Project,e.getClass().getName()+"关闭出错",ex);
            }
        }
    }
}
