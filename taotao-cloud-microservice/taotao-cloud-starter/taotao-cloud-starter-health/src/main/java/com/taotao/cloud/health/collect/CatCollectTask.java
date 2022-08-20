//package com.taotao.cloud.health.collect;
//
//
//import com.taotao.cloud.common.utils.ContextUtil;
//import com.taotao.cloud.common.utils.ReflectionUtil;
//import com.taotao.cloud.core.utils.PropertyUtil;
//import com.taotao.cloud.health.base.FieldReport;
//import java.util.Collections;
//import java.util.Map;
//
///**
// */
//public class CatCollectTask extends AbstractCollectTask {
//
//	@Override
//	public int getTimeSpan() {
//		return PropertyUtil.getPropertyCache("bsf.health.cat.timeSpan", 20);
//	}
//
//	@Override
//	public boolean getEnabled() {
//		return PropertyUtil.getPropertyCache("bsf.health.cat.enabled", true);
//	}
//
//	@Override
//	public String getDesc() {
//		return "Cat监控性能采集";
//	}
//
//	@Override
//	public String getName() {
//		return "cat.info";
//	}
//
//	@Override
//	protected Object getData() {
//		CatInfo data = new CatInfo();
//		if (ContextUtil.getBean(
//			ReflectionUtil.tryClassForName("com.yh.csx.bsf.cat.CatConfiguration"), false)
//			!= null) {
//			Class senderClass = ReflectionUtil.tryClassForName(
//				"com.dianping.cat.message.io.TcpSocketSender");
//			if (senderClass != null) {
//				Object sender = ReflectionUtil.callMethod(senderClass, "getInstance", null);
//				Object messageQueue = ReflectionUtil.getFieldValue(sender, "messageQueue");
//				if (messageQueue != null) {
//					data.queueSize = (Integer) ReflectionUtil.callMethod(messageQueue, "size",
//						null);
//				}
//				Object statistics = ReflectionUtil.getFieldValue(sender, "statistics");
//				if (statistics != null) {
//					Map<String, Long> values = ReflectionUtil.tryCallMethod(statistics,
//						"getStatistics", null, Collections.emptyMap());
//					data.messageCount = values.getOrDefault("cat.status.message.produced", 0L);
//					data.overflowCount = values.getOrDefault("cat.status.message.overflowed", 0L);
//				}
//			}
//		}
//		return data;
//	}
//
//	private static class CatInfo {
//
//		@FieldReport(name = "cat.queue.size", desc = "Cat监控队列大小")
//		private Integer queueSize;
//		@FieldReport(name = "cat.message.count", desc = "Cat产生消息数量")
//		private Long messageCount;
//		@FieldReport(name = "cat.overflow.count", desc = "Cat队列丢弃数量")
//		private Long overflowCount;
//
//		public CatInfo() {
//		}
//
//		public CatInfo(Integer queueSize, Long messageCount, Long overflowCount) {
//			this.queueSize = queueSize;
//			this.messageCount = messageCount;
//			this.overflowCount = overflowCount;
//		}
//
//		public Integer getQueueSize() {
//			return queueSize;
//		}
//
//		public void setQueueSize(Integer queueSize) {
//			this.queueSize = queueSize;
//		}
//
//		public Long getMessageCount() {
//			return messageCount;
//		}
//
//		public void setMessageCount(Long messageCount) {
//			this.messageCount = messageCount;
//		}
//
//		public Long getOverflowCount() {
//			return overflowCount;
//		}
//
//		public void setOverflowCount(Long overflowCount) {
//			this.overflowCount = overflowCount;
//		}
//	}
//}
