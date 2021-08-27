package com.taotao.cloud.health.collect;

import com.yh.csx.bsf.core.common.Collector;
import com.yh.csx.bsf.core.util.ContextUtils;
import com.yh.csx.bsf.core.util.PropertyUtils;
import com.yh.csx.bsf.core.util.ReflectionUtils;
import com.yh.csx.bsf.health.base.AbstractCollectTask;
import com.yh.csx.bsf.health.base.FieldReport;
import lombok.Data;
import lombok.var;

import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.ScheduledThreadPoolExecutor;

/**
 * @author Huang Zhaoping
 */
public class ElasticSearchCollectTask extends AbstractCollectTask {
    @Override
    public int getTimeSpan() {
        return PropertyUtils.getPropertyCache("bsf.health.elasticSearch.timeSpan", 20);
    }

    @Override
    public boolean getEnabled() {
        return PropertyUtils.getPropertyCache("bsf.health.elasticSearch.enabled", true);
    }

    @Override
    public String getDesc() {
        return "ElasticSearch服务性能采集";
    }

    @Override
    public String getName() {
        return "elasticSearch.info";
    }

    @Override
    protected Object getData() {
        ElasticSearchData data = new ElasticSearchData();
        if (ContextUtils.getBean(ReflectionUtils.tryClassForName("com.yh.csx.bsf.elasticsearch.impl.ElasticSearchProvider"), false) != null) {
            var hook = (Collector.Hook)ReflectionUtils.callMethod(ReflectionUtils.tryClassForName("com.yh.csx.bsf.elasticsearch.impl.ElasticSearchMonitor"), "hook", null);
            if (hook != null) {
                data.hookCurrent = hook.getCurrent();
                data.hookError = hook.getLastErrorPerSecond();
                data.hookSuccess = hook.getLastSuccessPerSecond();
                data.hookList = hook.getMaxTimeSpanList().toText();
                data.hookListPerMinute = hook.getMaxTimeSpanListPerMinute().toText();
            }
            Object transportClient = ContextUtils.getBean(ReflectionUtils.tryClassForName("org.elasticsearch.client.transport.TransportClient"), false);
            if (transportClient != null) {
                data.threadsTotal = 0;
                data.activateTotal = 0;
                data.queueSize = 0;
                // 线程池
                Iterator<Object> stats = ReflectionUtils.tryGetValue(transportClient, "threadPool.stats.iterator");
                if (stats != null) {
                    stats.forEachRemaining(stat -> {
                        data.threadsTotal += ReflectionUtils.tryGetValue(stat, "threads", 0);
                        data.activateTotal += ReflectionUtils.tryGetValue(stat, "active", 0);
                        data.queueSize += ReflectionUtils.tryGetValue(stat, "queue", 0);
                    });
                }
                ScheduledThreadPoolExecutor scheduler = ReflectionUtils.tryGetValue(transportClient, "threadPool.scheduler");
                if (scheduler != null) {
                    data.threadsTotal += scheduler.getPoolSize();
                    data.activateTotal += scheduler.getActiveCount();
                    data.queueSize += scheduler.getQueue().size();
                }
                // 节点连接
                Object nodesService = ReflectionUtils.tryGetValue(transportClient, "nodesService");
                if (nodesService != null) {
                    data.nodesCount = ReflectionUtils.tryGetValue(nodesService, "listedNodes.size");
                    data.connectedCount = ReflectionUtils.tryGetValue(nodesService, "nodes.size");
                    data.connectionCount = 0;
                    Collection<Object> channels = ReflectionUtils.tryGetValue(nodesService, "transportService.connectionManager.connectedNodes.values");
                    if (channels != null) {
                        for(Object obj : channels){
                            data.connectionCount += ReflectionUtils.tryGetValue(obj, "channels.size", 0);
                        }
                    }
                }

            }
        }
        return data;
    }

    @Data
    private static class ElasticSearchData {

        @FieldReport(name = "elasticSearch.hook.error", desc = "ElasticSearch服务拦截上一次每秒出错次数")
        private Long hookError;
        @FieldReport(name = "elasticSearch.hook.success", desc = "ElasticSearch服务拦截上一次每秒成功次数")
        private Long hookSuccess;
        @FieldReport(name = "elasticSearch.hook.current", desc = "ElasticSearch服务拦截当前执行任务数")
        private Long hookCurrent;
        @FieldReport(name = "elasticSearch.hook.list.detail", desc = "ElasticSearch服务拦截历史最大耗时任务列表")
        private String hookList;
        @FieldReport(name = "elasticSearch.hook.list.minute.detail", desc = "ElasticSearch服务拦截历史最大耗时任务列表(每分钟)")
        private String hookListPerMinute;
        @FieldReport(name = "elasticSearch.node.count", desc = "ElasticSearch集群发现节点数")
        private Integer nodesCount;
        @FieldReport(name = "elasticSearch.node.connected", desc = "ElasticSearch集群已连接节点数")
        private Integer connectedCount;
        @FieldReport(name = "elasticSearch.node.connections", desc = "ElasticSearch集群连接数")
        private Integer connectionCount;
        @FieldReport(name = "elasticSearch.pool.threads.count", desc = "ElasticSearch集群线程池线程数")
        private Integer threadsTotal;
        @FieldReport(name = "elasticSearch.pool.threads.active", desc = "ElasticSearch集群池线程活动线程数")
        private Integer activateTotal;
        @FieldReport(name = "elasticSearch.pool.queue.size", desc = "ElasticSearch集群池线程队列大小")
        private Integer queueSize;
    }
}
