package com.taotao.cloud.health.collect;

import com.yh.csx.bsf.core.http.DefaultHttpClient;
import com.yh.csx.bsf.core.http.HttpClientManager;
import com.yh.csx.bsf.core.util.PropertyUtils;
import com.yh.csx.bsf.core.util.ReflectionUtils;
import com.yh.csx.bsf.health.base.AbstractCollectTask;
import com.yh.csx.bsf.health.base.FieldReport;
import lombok.Data;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.pool.PoolStats;

import java.util.concurrent.ConcurrentHashMap;

/**
 * HTTP连接池性能采集
 * @author Huang Zhaoping
 */
public class HttpPoolCollectTask extends AbstractCollectTask {
    @Override
    public int getTimeSpan() {
        return PropertyUtils.getPropertyCache("bsf.health.httpPool.timeSpan", 20);
    }

    @Override
    public boolean getEnabled() {
        return PropertyUtils.getPropertyCache("bsf.health.httpPool.enabled", true);
    }

    @Override
    public String getDesc() {
        return "Http连接池性能采集";
    }

    @Override
    public String getName() {
        return "bsf.httpPool.info";
    }

    @Override
    protected Object getData() {
        ConcurrentHashMap<String, DefaultHttpClient> pool = ReflectionUtils.getFieldValue (HttpClientManager.Default, "pool");
        if (pool == null) { 
        	return null;
        }
        HttpPoolInfo data = new HttpPoolInfo();
        StringBuilder detail = new StringBuilder();
        pool.forEach((id, client)->{
            PoolingHttpClientConnectionManager manager = ReflectionUtils.getFieldValue (client, "manager");
            PoolStats stats = manager.getTotalStats();
            data.availableCount += stats.getAvailable();
            data.pendingCount += stats.getPending();
            data.leasedCount += stats.getLeased();
            detail.append(String.format("[Client连接池:%s]\r\n", id));
            detail.append(String.format("路由数:%s\r\n", manager.getRoutes()));
            detail.append(String.format("路由连接数:%s\r\n", manager.getDefaultMaxPerRoute()));
            detail.append(String.format("最大的连接数:%s\r\n", manager.getMaxTotal()));
            detail.append(String.format("可用的连接数:%s\r\n", stats.getAvailable()));
            detail.append(String.format("等待的连接数:%s\r\n", stats.getPending()));
            detail.append(String.format("使用中的连接数:%s\r\n", stats.getLeased()));
        });
        data.poolDetail = detail.toString();
        return data;
    }

    @Data
    private static class HttpPoolInfo {

        @FieldReport(name = "bsf.httpPool.available", desc = "HttpPool可用的连接数")
        private Integer availableCount = 0;
        @FieldReport(name = "bsf.httpPool.pending", desc = "HttpPool等待的连接数")
        private Integer pendingCount = 0;
        @FieldReport(name = "bsf.httpPool.leased", desc = "HttpPool使用中的连接数")
        private Integer leasedCount = 0;
        @FieldReport(name = "bsf.httpPool.detail", desc = "HttpPool详情")
        private String poolDetail;
    }
}
