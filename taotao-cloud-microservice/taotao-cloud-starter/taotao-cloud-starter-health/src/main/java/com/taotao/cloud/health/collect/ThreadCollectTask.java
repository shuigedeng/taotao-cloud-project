package com.taotao.cloud.health.collect;

import com.yh.csx.bsf.core.util.*;
import com.yh.csx.bsf.health.base.AbstractCollectTask;
import com.yh.csx.bsf.health.base.FieldReport;
import lombok.Data;
import lombok.val;
import lombok.var;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.util.HashMap;

/**
 * @author: chejiangyi
 * @version: 2019-07-24 18:46
 **/
public class ThreadCollectTask extends AbstractCollectTask {
  ThreadMXBean threadMXBean;
    public ThreadCollectTask() {
        threadMXBean = ManagementFactory.getThreadMXBean();
    }

    @Override
    public int getTimeSpan() {
        return PropertyUtils.getPropertyCache("bsf.health.thread.timeSpan",10);
    }

    @Override
    public String getDesc() {
        return "线程监测";
    }

    @Override
    public String getName() {
        return "thread.info";
    }

    @Override
    public boolean getEnabled() {
        return PropertyUtils.getPropertyCache("bsf.health.thread.enabled",true);
    }

    private HashMap<Long,Long> lastThreadUserTime = new HashMap<>();

    @Override
    protected Object getData() {
        ThreadInfo threadInfo = new ThreadInfo();
        val deadlockedThreads = threadMXBean.findDeadlockedThreads();
        threadInfo.deadlockedThreadCount = (deadlockedThreads==null?0:deadlockedThreads.length);
        threadInfo.threadCount = threadMXBean.getThreadCount();
        java.lang.management.ThreadInfo runable=null;
        java.lang.management.ThreadInfo wait=null;
        java.lang.management.ThreadInfo block=null;
        val treadUserTime = new HashMap<Long,Long>();
        for(val info:threadMXBean.dumpAllThreads(false,false)) {
            treadUserTime.put(info.getThreadId(),threadMXBean.getThreadUserTime(info.getThreadId()));
            if(info.getThreadState() == Thread.State.RUNNABLE)
            {
                threadInfo.runableThreadCount+=1;
                if (runable == null) {
                    runable = info;
                } else
                {
                    var lastvalue = lastThreadUserTime.get(info.getThreadId());
                    lastvalue = (lastvalue==null?0L:lastvalue);

                    var runablevalue = lastThreadUserTime.get(runable.getThreadId());
                    runablevalue = (runablevalue==null?0L:runablevalue);
                    if ((threadMXBean.getThreadUserTime(runable.getThreadId())- runablevalue )< (threadMXBean.getThreadUserTime(info.getThreadId())-lastvalue)) {
                        runable = info;
                    }
                }
            }else if(info.getThreadState() == Thread.State.BLOCKED){
                threadInfo.blockedThreadCount+=1;
                if(block==null)
                {
                    block = info;
                }
                else if(block.getBlockedTime()<info.getBlockedTime())
                {
                    block = info;
                }
            }else if(info.getThreadState() == Thread.State.WAITING){
                threadInfo.waitingThreadCount+=1;
                if(wait==null)
                {
                    wait = info;
                }
                else if(wait.getWaitedTime()<info.getWaitedTime())
                {
                    wait = info;
                }
            }
        }
        lastThreadUserTime = treadUserTime;
        if(runable!=null)
        {
            threadInfo.setMaxRunableDetail(ExceptionUtils.trace2String(runable.getStackTrace()));
        }
        if(wait!=null)
        {
            threadInfo.setMaxWaitingDetail(ExceptionUtils.trace2String(wait.getStackTrace()));
        }
        if(block!=null)
        {
            threadInfo.setMaxBlockedDetail(ExceptionUtils.trace2String(block.getStackTrace()));
        }
        return threadInfo;
    }


    @Data
    private static class ThreadInfo {
        @FieldReport(name = "thread.deadlocked.count",desc="死锁线程数")
        private double deadlockedThreadCount;
        @FieldReport(name = "thread.total",desc="线程总数")
        private double threadCount;
        @FieldReport(name = "thread.runable.count",desc="运行线程总数")
        private double runableThreadCount;
        @FieldReport(name = "thread.blocked.count",desc="阻塞线程总数")
        private double blockedThreadCount;
        @FieldReport(name = "thread.waiting.count",desc="等待线程总数")
        private double waitingThreadCount;
        @FieldReport(name = "thread.runable.max.detail",desc="最近运行最耗时的线程详情")
        private String maxRunableDetail;
        @FieldReport(name = "thread.blocked.max.detail",desc="阻塞最耗时的线程详情")
        private String maxBlockedDetail;
        @FieldReport(name = "thread.waiting.max.detail",desc="等待最耗时的线程详情")
        private String maxWaitingDetail;
    }
}
