package com.taotao.cloud.health.collect;


import com.yh.csx.bsf.core.util.LogUtils;
import com.yh.csx.bsf.health.config.HealthProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yh.csx.bsf.core.util.ExceptionUtils;
import com.yh.csx.bsf.core.util.PropertyUtils;
import com.yh.csx.bsf.core.util.StringUtils;
import com.yh.csx.bsf.health.base.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.val;

/**
 * @author: chejiangyi
 * @version: 2019-07-26 13:36
 **/
public class UnCatchExceptionCollectTask extends AbstractCollectTask {

    public UnCatchExceptionCollectTask(){
        //注入异常处理
        val handler =  Thread.getDefaultUncaughtExceptionHandler();
        if(handler==null||!(handler instanceof DefaultUncaughtExceptionHandler)) {
            Thread.setDefaultUncaughtExceptionHandler(new DefaultUncaughtExceptionHandler(this,handler));
        }
    }

    @Override
    public int getTimeSpan() {
        return -1;
    }

    private Throwable lastException = null;

    @Override
    public String getDesc() {
        return "全局未捕获异常拦截监测";
    }

    @Override
    public String getName() {
        return "uncatch.info";
    }

    @Override
    public boolean getEnabled() {
        return PropertyUtils.getPropertyCache("bsf.health.uncatch.enabled",true);
    }

    @Override
    public Report getReport() {
        return new Report(new UnCatchInfo(StringUtils.nullToEmpty(ExceptionUtils.trace2String(lastException))));
    }



    public static class DefaultUncaughtExceptionHandler implements Thread.UncaughtExceptionHandler {
        private Thread.UncaughtExceptionHandler lastUncaughtExceptionHandler = null;
        private UnCatchExceptionCollectTask unCatchExceptionCheckTask =null;
        public DefaultUncaughtExceptionHandler(UnCatchExceptionCollectTask unCatchExceptionCheckTask,
                                               Thread.UncaughtExceptionHandler lastUncaughtExceptionHandler){
            this.unCatchExceptionCheckTask = unCatchExceptionCheckTask;
            this.lastUncaughtExceptionHandler = lastUncaughtExceptionHandler;
        }
        @Override
        public void uncaughtException(Thread t, Throwable e){
            try {
                if(e!=null) {
                    this.unCatchExceptionCheckTask.lastException = e;
                    AbstractCollectTask.notifyMessage(EnumWarnType.ERROR, "未捕获错误", ExceptionUtils.trace2String(e));
                    LogUtils.error(UnCatchExceptionCollectTask.class, HealthProperties.Project,"未捕获错误",e);
                }
            }catch (Exception e2){}
            if(lastUncaughtExceptionHandler!=null){
                lastUncaughtExceptionHandler.uncaughtException(t,e);
            }
        }


    }

    @Override
    public void close() throws Exception {
        //解除异常处理
        val handler =  Thread.getDefaultUncaughtExceptionHandler();
        if(handler!=null && handler instanceof DefaultUncaughtExceptionHandler) {
            Thread.setDefaultUncaughtExceptionHandler(((DefaultUncaughtExceptionHandler) handler).lastUncaughtExceptionHandler);
        }
    }

    @Data
    @AllArgsConstructor
    private static class UnCatchInfo {
        @FieldReport(name = "uncatch.trace",desc = "未捕获错误堆栈")
       private String trace;
    }
}
