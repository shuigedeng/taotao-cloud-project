package com.taotao.cloud.health.collect;

import com.yh.csx.bsf.core.common.Collector;
import com.yh.csx.bsf.core.util.ContextUtils;
import com.yh.csx.bsf.core.util.PropertyUtils;
import com.yh.csx.bsf.core.util.ReflectionUtils;
import com.yh.csx.bsf.health.base.AbstractCollectTask;
import com.yh.csx.bsf.health.base.FieldReport;
import com.yh.csx.bsf.health.base.HealthException;
import lombok.Data;
import lombok.val;

/**
 * Redis性能参数收集
 * @author: chejiangyi
 * @version: 2019-08-03 21:17
 **/
public class JedisCollectTask extends AbstractCollectTask {

    public JedisCollectTask() {

    }

    @Override
    public int getTimeSpan() {
        return PropertyUtils.getPropertyCache("bsf.health.jedis.timeSpan", 20);
    }

    @Override
    public String getDesc() {
        return "jedis性能采集";
    }

    @Override
    public String getName() {
        return "jedis.info";
    }

    @Override
    public boolean getEnabled() {
        return PropertyUtils.getPropertyCache("bsf.health.jedis.enabled", true);
    }

    @Override
    protected Object getData() {
        val item = ContextUtils.getBean(ReflectionUtils.classForName("com.yh.csx.bsf.redis.impl.RedisClusterMonitor"), false);
        if (item != null) {
            try {
                ReflectionUtils.callMethod(item, "collect", null);
                JedisInfo info = new JedisInfo();
                String name = "jedis.cluster";
                info.detail = (String)Collector.Default.value(name+".pool.detail").get();
                info.wait = (Integer)Collector.Default.value(name+".pool.wait").get();
                info.active = (Integer)Collector.Default.value(name+".pool.active").get();
                info.idle = (Integer) Collector.Default.value(name+".pool.idle").get();
                info.lockInfo= (String)Collector.Default.value(name+".lock.error.detail").get();
                val hook = Collector.Default.hook(name+".hook");
                if (hook != null) {
                    info.hookCurrent = hook.getCurrent();
                    info.hookError = hook.getLastErrorPerSecond();
                    info.hookSuccess = hook.getLastSuccessPerSecond();
                    info.hookList = hook.getMaxTimeSpanList().toText();
                    info.hookListPerMinute = hook.getMaxTimeSpanListPerMinute().toText();
                }
                return info;
            } catch (Exception exp) {
                throw new HealthException(exp);
            }
        }
        return null;
    }


    @Data
    private static class JedisInfo {
        @FieldReport(name = "jedis.cluster.pool.wait", desc = "jedis集群排队等待的请求数")
        private Integer wait;
        @FieldReport(name = "jedis.cluster.pool.active", desc = "jedis集群活动使用的请求数")
        private Integer active;
        @FieldReport(name = "jedis.cluster.pool.idle", desc = "jedis集群空闲的请求数")
        private Integer idle;
        @FieldReport(name = "jedis.cluster.pool.detail", desc = "jedis集群连接池详情")
        private String detail;
        @FieldReport(name = "jedis.cluster.hook.error", desc = "jedis集群拦截上一次每秒出错次数")
        private Long hookError;
        @FieldReport(name = "jedis.cluster.hook.success", desc = "jedis集群拦截上一次每秒成功次数")
        private Long hookSuccess;
        @FieldReport(name = "jedis.cluster.hook.current", desc = "jedis集群拦截当前执行任务数")
        private Long hookCurrent;
        @FieldReport(name = "jedis.cluster.hook.list.detail", desc = "jedis集群拦截历史最大耗时任务列表")
        private String hookList;
        @FieldReport(name = "jedis.cluster.hook.list.minute.detail", desc = "jedis集群拦截历史最大耗时任务列表(每分钟)")
        private String hookListPerMinute;
        @FieldReport(name = "jedis.cluster.lock.error.detail", desc = "jedis集群分布式锁异常信息")
        private String lockInfo;
    }
}
