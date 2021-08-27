package com.taotao.cloud.health.collect;

import com.yh.csx.bsf.core.util.ContextUtils;
import com.yh.csx.bsf.core.util.PropertyUtils;
import com.yh.csx.bsf.core.util.ReflectionUtils;
import com.yh.csx.bsf.health.base.AbstractCollectTask;
import com.yh.csx.bsf.health.base.FieldReport;
import lombok.Data;
import lombok.val;

import javax.sql.DataSource;

/**
 * @author: chejiangyi
 * @version: 2019-08-02 09:42
 **/
public class DataSourceCollectTask  extends AbstractCollectTask {

    @Override
    public int getTimeSpan() {
        return PropertyUtils.getPropertyCache("bsf.health.dataSource.timeSpan", 20);
    }

    @Override
    public String getDesc() {
        return "dataSource性能采集";
    }

    @Override
    public String getName() {
        return "dataSource.info";
    }

    @Override
    public boolean getEnabled() {
        return PropertyUtils.getPropertyCache("bsf.health.dataSource.enabled", true);
    }

    @Override
    protected Object getData() {
        DataSourceInfo info = new DataSourceInfo();
        val names = ContextUtils.getApplicationContext().getBeanNamesForType(DataSource.class);
        int duridIndex =0;
        for(val name :names){
            val dataSource = ContextUtils.getApplicationContext().getBean(name,DataSource.class);
            Class druidCls = ReflectionUtils.tryClassForName("com.alibaba.druid.pool.DruidDataSource");
            if(druidCls!=null && druidCls.isAssignableFrom(dataSource.getClass())) {
                val field = ReflectionUtils.findField(info.getClass(), "druid" + duridIndex++);
                if(field!=null) {
                    DruidDataSourceInfo druid = new DruidDataSourceInfo();
                   druid.active = (Integer) ReflectionUtils.callMethod(dataSource,"getActiveCount",null);
                   druid.connect = (Long)ReflectionUtils.callMethod(dataSource,"getConnectCount",null);
                   druid.poolingCount= (Integer) ReflectionUtils.callMethod(dataSource,"getPoolingCount",null);
                   druid.lockQueueLength= (Integer) ReflectionUtils.callMethod(dataSource,"getLockQueueLength",null);
                   druid.waitThreadCount =(Integer) ReflectionUtils.callMethod(dataSource,"getWaitThreadCount",null);
                   druid.initialSize=(Integer) ReflectionUtils.callMethod(dataSource,"getInitialSize",null);
                   druid.maxActive =(Integer) ReflectionUtils.callMethod(dataSource,"getMaxActive",null);
                   druid.minIdle=(Integer) ReflectionUtils.callMethod(dataSource,"getMinIdle",null);
                   druid.connectErrorCount=(Long) ReflectionUtils.callMethod(dataSource,"getConnectErrorCount",null);
                   druid.createTimeSpan=(Long) ReflectionUtils.callMethod(dataSource,"getCreateTimespanMillis",null);
                   druid.closeCount=(Long)ReflectionUtils.callMethod(dataSource,"getCloseCount",null);
                   druid.createCount=(Long)ReflectionUtils.callMethod(dataSource,"getCreateCount",null);
                   druid.destroyCount=(Long)ReflectionUtils.callMethod(dataSource,"getDestroyCount",null);
                   druid.isSharePreparedStatements = ReflectionUtils.callMethod(dataSource,"isSharePreparedStatements",null).toString();
                   druid.isRemoveAbandoned = ReflectionUtils.callMethod(dataSource,"isRemoveAbandoned",null).toString();
                   druid.removeAbandonedTimeout=(Integer) ReflectionUtils.callMethod(dataSource,"getRemoveAbandonedTimeout",null);
                   druid.removeAbandonedCount=(Long) ReflectionUtils.callMethod(dataSource,"getRemoveAbandonedCount",null);
                   druid.rollbackCount =(Long) ReflectionUtils.callMethod(dataSource,"getRollbackCount",null);
                   druid.commitCount =(Long) ReflectionUtils.callMethod(dataSource,"getCommitCount",null);
                   druid.startTransactionCount = (Long) ReflectionUtils.callMethod(dataSource,"getStartTransactionCount",null);
                   field.setAccessible(true);
                   ReflectionUtils.setFieldValue(field,info,druid);
                }
            }
        }

        return info;
    }


    @Data
    private static class DataSourceInfo {
        @FieldReport(name = "bsf.druid0.info", desc = "druid0信息")
        private DruidDataSourceInfo druid0;
        @FieldReport(name = "bsf.druid1.info", desc = "druid1信息")
        private DruidDataSourceInfo druid1;
        @FieldReport(name = "bsf.druid2.info", desc = "druid2信息")
        private DruidDataSourceInfo druid2;
        @FieldReport(name = "bsf.druid3.info", desc = "druid3信息")
        private DruidDataSourceInfo druid3;
        @FieldReport(name = "bsf.druid4.info", desc = "druid4信息")
        private DruidDataSourceInfo druid4;
        @FieldReport(name = "bsf.druid5.info", desc = "druid5信息")
        private DruidDataSourceInfo druid5;
        @FieldReport(name = "bsf.druid6.info", desc = "druid6信息")
        private DruidDataSourceInfo druid6;
        @FieldReport(name = "bsf.druid7.info", desc = "druid7信息")
        private DruidDataSourceInfo druid7;
        @FieldReport(name = "bsf.druid8.info", desc = "druid8信息")
        private DruidDataSourceInfo druid8;
        @FieldReport(name = "bsf.druid9.info", desc = "druid9信息")
        private DruidDataSourceInfo druid9;

    }
    @Data
    private static class DruidDataSourceInfo{
        @FieldReport(name = "dataSource.druid.pool.startTransaction.count", desc = "druid sql 开启事务次数")
        private Long startTransactionCount;
        @FieldReport(name = "dataSource.druid.pool.commit.count", desc = "druid sql commit次数")
        private Long commitCount;
        @FieldReport(name = "dataSource.druid.pool.rollback.count", desc = "druid sql回滚次数")
        private Long rollbackCount;
        @FieldReport(name = "dataSource.druid.pool.removeAbandoned.count", desc = "druid 连接超时回收次数")
        private Long removeAbandonedCount;
        @FieldReport(name = "dataSource.druid.pool.removeAbandoned.timeout", desc = "druid 连接超时回收周期（秒）")
        private Integer removeAbandonedTimeout;
        @FieldReport(name = "dataSource.druid.pool.isRemoveAbandoned", desc = "druid 是否开启连接超时回收")
        private String isRemoveAbandoned;
        @FieldReport(name = "dataSource.druid.pool.isSharePreparedStatements", desc = "druid preparedStatement是否缓存")
        private String isSharePreparedStatements;
        @FieldReport(name = "dataSource.druid.pool.destroy.count", desc = "druid销毁连接次数")
        private Long destroyCount;
        @FieldReport(name = "dataSource.druid.pool.create.count", desc = "druid创建连接次数")
        private Long createCount;
        @FieldReport(name = "dataSource.druid.pool.close.count", desc = "druid关闭连接次数")
        private Long closeCount;
        @FieldReport(name = "dataSource.druid.pool.create.timeSpan", desc = "druid物理连接创建耗时(毫秒)")
        private Long createTimeSpan;
        @FieldReport(name = "dataSource.druid.pool.connect.errorCount", desc = "druid物理连接错误数")
        private Long connectErrorCount;
        @FieldReport(name = "dataSource.druid.pool.idle.min", desc = "druid连接池最小值")
        private Integer minIdle;
        @FieldReport(name = "dataSource.druid.pool.active.max", desc = "druid连接池最大值")
        private Integer maxActive;
        @FieldReport(name = "dataSource.druid.pool.initial.size", desc = "druid连接池初始化长度")
        private Integer initialSize;
        @FieldReport(name = "dataSource.druid.pool.waitThread.count", desc = "druid获取连接时等待线程数")
        private Integer waitThreadCount;
        @FieldReport(name = "dataSource.druid.pool.lockQueue.length", desc = "druid获取连接等待队列长度")
        private Integer lockQueueLength;
        @FieldReport(name = "dataSource.druid.pool.active", desc = "druid正在打开的连接数")
        private Integer active;
        @FieldReport(name = "dataSource.druid.pool.connect", desc = "druid申请连接的次数")
        private Long connect;
        @FieldReport(name = "dataSource.druid.pool.poolingCount", desc = "druid连接池空闲连接数")
        private Integer poolingCount;
    }
}
