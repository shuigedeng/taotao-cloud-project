//package com.taotao.cloud.health.collect;
//
//
//import com.taotao.cloud.common.utils.ContextUtil;
//import com.taotao.cloud.common.utils.ReflectionUtil;
//import com.taotao.cloud.core.base.Collector;
//import com.taotao.cloud.core.base.Collector.Hook;
//import com.taotao.cloud.core.utils.PropertyUtil;
//import com.taotao.cloud.health.base.FieldReport;
//import java.util.Collection;
//import java.util.Iterator;
//import java.util.concurrent.ScheduledThreadPoolExecutor;
//
///**
// */
//public class ElasticSearchCollectTask extends AbstractCollectTask {
//
//	@Override
//	public int getTimeSpan() {
//		return PropertyUtil.getPropertyCache("bsf.health.elasticSearch.timeSpan", 20);
//	}
//
//	@Override
//	public boolean getEnabled() {
//		return PropertyUtil.getPropertyCache("bsf.health.elasticSearch.enabled", true);
//	}
//
//	@Override
//	public String getDesc() {
//		return "ElasticSearch服务性能采集";
//	}
//
//	@Override
//	public String getName() {
//		return "elasticSearch.info";
//	}
//
//	@Override
//	protected Object getData() {
//		ElasticSearchData data = new ElasticSearchData();
//		if (ContextUtil.getBean(ReflectionUtil.tryClassForName(
//			"com.yh.csx.bsf.elasticsearch.impl.ElasticSearchProvider"), false) != null) {
//			Hook hook = (Collector.Hook) ReflectionUtil.callMethod(ReflectionUtil.tryClassForName(
//				"com.yh.csx.bsf.elasticsearch.impl.ElasticSearchMonitor"), "hook", null);
//			if (hook != null) {
//				data.hookCurrent = hook.getCurrent();
//				data.hookError = hook.getLastErrorPerSecond();
//				data.hookSuccess = hook.getLastSuccessPerSecond();
//				data.hookList = hook.getMaxTimeSpanList().toText();
//				data.hookListPerMinute = hook.getMaxTimeSpanListPerMinute().toText();
//			}
//
//			Object transportClient = ContextUtil.getBean(ReflectionUtil.tryClassForName(
//				"org.elasticsearch.client.transport.TransportClient"), false);
//			if (transportClient != null) {
//				data.threadsTotal = 0;
//				data.activateTotal = 0;
//				data.queueSize = 0;
//				// 线程池
//				Iterator<Object> stats = ReflectionUtil.tryGetValue(transportClient,
//					"threadPool.stats.iterator");
//				if (stats != null) {
//					stats.forEachRemaining(stat -> {
//						data.threadsTotal += ReflectionUtil.tryGetValue(stat, "threads", 0);
//						data.activateTotal += ReflectionUtil.tryGetValue(stat, "active", 0);
//						data.queueSize += ReflectionUtil.tryGetValue(stat, "queue", 0);
//					});
//				}
//				ScheduledThreadPoolExecutor scheduler = ReflectionUtil.tryGetValue(transportClient,
//					"threadPool.scheduler");
//				if (scheduler != null) {
//					data.threadsTotal += scheduler.getPoolSize();
//					data.activateTotal += scheduler.getActiveCount();
//					data.queueSize += scheduler.getQueue().size();
//				}
//				// 节点连接
//				Object nodesService = ReflectionUtil.tryGetValue(transportClient, "nodesService");
//				if (nodesService != null) {
//					data.nodesCount = ReflectionUtil.tryGetValue(nodesService, "listedNodes.size");
//					data.connectedCount = ReflectionUtil.tryGetValue(nodesService, "nodes.size");
//					data.connectionCount = 0;
//					Collection<Object> channels = ReflectionUtil.tryGetValue(nodesService,
//						"transportService.connectionManager.connectedNodes.values");
//					if (channels != null) {
//						for (Object obj : channels) {
//							data.connectionCount += ReflectionUtil.tryGetValue(obj, "channels.size",
//								0);
//						}
//					}
//				}
//
//			}
//		}
//		return data;
//	}
//
//	private static class ElasticSearchData {
//
//		@FieldReport(name = "elasticSearch.hook.error", desc = "ElasticSearch服务拦截上一次每秒出错次数")
//		private Long hookError;
//		@FieldReport(name = "elasticSearch.hook.success", desc = "ElasticSearch服务拦截上一次每秒成功次数")
//		private Long hookSuccess;
//		@FieldReport(name = "elasticSearch.hook.current", desc = "ElasticSearch服务拦截当前执行任务数")
//		private Long hookCurrent;
//		@FieldReport(name = "elasticSearch.hook.list.detail", desc = "ElasticSearch服务拦截历史最大耗时任务列表")
//		private String hookList;
//		@FieldReport(name = "elasticSearch.hook.list.minute.detail", desc = "ElasticSearch服务拦截历史最大耗时任务列表(每分钟)")
//		private String hookListPerMinute;
//		@FieldReport(name = "elasticSearch.node.count", desc = "ElasticSearch集群发现节点数")
//		private Integer nodesCount;
//		@FieldReport(name = "elasticSearch.node.connected", desc = "ElasticSearch集群已连接节点数")
//		private Integer connectedCount;
//		@FieldReport(name = "elasticSearch.node.connections", desc = "ElasticSearch集群连接数")
//		private Integer connectionCount;
//		@FieldReport(name = "elasticSearch.pool.threads.count", desc = "ElasticSearch集群线程池线程数")
//		private Integer threadsTotal;
//		@FieldReport(name = "elasticSearch.pool.threads.active", desc = "ElasticSearch集群池线程活动线程数")
//		private Integer activateTotal;
//		@FieldReport(name = "elasticSearch.pool.queue.size", desc = "ElasticSearch集群池线程队列大小")
//		private Integer queueSize;
//
//		public ElasticSearchData() {
//		}
//
//		public ElasticSearchData(Long hookError, Long hookSuccess, Long hookCurrent,
//			String hookList, String hookListPerMinute, Integer nodesCount,
//			Integer connectedCount, Integer connectionCount, Integer threadsTotal,
//			Integer activateTotal, Integer queueSize) {
//			this.hookError = hookError;
//			this.hookSuccess = hookSuccess;
//			this.hookCurrent = hookCurrent;
//			this.hookList = hookList;
//			this.hookListPerMinute = hookListPerMinute;
//			this.nodesCount = nodesCount;
//			this.connectedCount = connectedCount;
//			this.connectionCount = connectionCount;
//			this.threadsTotal = threadsTotal;
//			this.activateTotal = activateTotal;
//			this.queueSize = queueSize;
//		}
//
//		public Long getHookError() {
//			return hookError;
//		}
//
//		public void setHookError(Long hookError) {
//			this.hookError = hookError;
//		}
//
//		public Long getHookSuccess() {
//			return hookSuccess;
//		}
//
//		public void setHookSuccess(Long hookSuccess) {
//			this.hookSuccess = hookSuccess;
//		}
//
//		public Long getHookCurrent() {
//			return hookCurrent;
//		}
//
//		public void setHookCurrent(Long hookCurrent) {
//			this.hookCurrent = hookCurrent;
//		}
//
//		public String getHookList() {
//			return hookList;
//		}
//
//		public void setHookList(String hookList) {
//			this.hookList = hookList;
//		}
//
//		public String getHookListPerMinute() {
//			return hookListPerMinute;
//		}
//
//		public void setHookListPerMinute(String hookListPerMinute) {
//			this.hookListPerMinute = hookListPerMinute;
//		}
//
//		public Integer getNodesCount() {
//			return nodesCount;
//		}
//
//		public void setNodesCount(Integer nodesCount) {
//			this.nodesCount = nodesCount;
//		}
//
//		public Integer getConnectedCount() {
//			return connectedCount;
//		}
//
//		public void setConnectedCount(Integer connectedCount) {
//			this.connectedCount = connectedCount;
//		}
//
//		public Integer getConnectionCount() {
//			return connectionCount;
//		}
//
//		public void setConnectionCount(Integer connectionCount) {
//			this.connectionCount = connectionCount;
//		}
//
//		public Integer getThreadsTotal() {
//			return threadsTotal;
//		}
//
//		public void setThreadsTotal(Integer threadsTotal) {
//			this.threadsTotal = threadsTotal;
//		}
//
//		public Integer getActivateTotal() {
//			return activateTotal;
//		}
//
//		public void setActivateTotal(Integer activateTotal) {
//			this.activateTotal = activateTotal;
//		}
//
//		public Integer getQueueSize() {
//			return queueSize;
//		}
//
//		public void setQueueSize(Integer queueSize) {
//			this.queueSize = queueSize;
//		}
//	}
//}
