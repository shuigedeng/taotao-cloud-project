//package com.taotao.cloud.health.collect;
//
//
//import com.taotao.cloud.common.exception.BaseException;
//import com.taotao.cloud.common.utils.ContextUtil;
//import com.taotao.cloud.common.utils.ReflectionUtil;
//import com.taotao.cloud.core.base.Collector;
//import com.taotao.cloud.core.utils.PropertyUtil;
//import com.taotao.cloud.health.base.FieldReport;
//import org.springframework.util.ReflectionUtils;
//
//public class RocketMQCollectTask extends AbstractCollectTask {
//
//	@Override
//	public int getTimeSpan() {
//		return PropertyUtil.getPropertyCache("bsf.health.rocketmq.timeSpan", 20);
//	}
//
//	@Override
//	public boolean getEnabled() {
//		return PropertyUtil.getPropertyCache("bsf.health.rocketmq.enabled", true);
//	}
//
//	@Override
//	public String getDesc() {
//		return "RocketMQ性能采集";
//	}
//
//	@Override
//	public String getName() {
//		return "rocketmq.info";
//	}
//
//	@Override
//	protected Object getData() {
//		RocketMQInfo data = new RocketMQInfo();
//		if (ContextUtil.getBean(
//			ReflectionUtil.tryClassForName("com.yh.csx.bsf.mq.rocketmq.RocketMQConsumerProvider"),
//			false) != null) {
//			Collector.Hook hook = getCollectorHook("com.yh.csx.bsf.mq.rocketmq.RocketMQMonitor");
//			data.consumerHookCurrent = hook.getCurrent();
//			data.consumerHookError = hook.getLastErrorPerSecond();
//			data.consumerHookSuccess = hook.getLastSuccessPerSecond();
//			data.consumerHookList = hook.getMaxTimeSpanList().toText();
//			data.consumerHookListPerMinute = hook.getMaxTimeSpanListPerMinute().toText();
//		}
//
//		if (ContextUtil.getBean(
//			ReflectionUtil.tryClassForName("com.yh.csx.bsf.mq.rocketmq.RocketMQProducerProvider"),
//			false) != null) {
//			Collector.Hook hook = getCollectorHook("com.yh.csx.bsf.mq.rocketmq.RocketMQMonitor");
//			data.producerHookCurrent = hook.getCurrent();
//			data.producerHookError = hook.getLastErrorPerSecond();
//			data.producerHookSuccess = hook.getLastSuccessPerSecond();
//			data.producerHookList = hook.getMaxTimeSpanList().toText();
//			data.producerHookListPerMinute = hook.getMaxTimeSpanListPerMinute().toText();
//		}
//		return data;
//	}
//
//	private Collector.Hook getCollectorHook(String className) {
//		Class<?> monitor = ReflectionUtil.classForName(className);
//		try {
//			return (Collector.Hook) ReflectionUtils.findMethod(monitor, "hook").invoke(null);
//		} catch (Exception e) {
//			throw new BaseException(e);
//		}
//	}
//
//	private static class RocketMQInfo {
//
//		@FieldReport(name = "rocketmq.consumer.hook.error", desc = "Consumer拦截上一次每秒出错次数")
//		private Long consumerHookError;
//		@FieldReport(name = "rocketmq.consumer.hook.success", desc = "Consumer拦截上一次每秒成功次数")
//		private Long consumerHookSuccess;
//		@FieldReport(name = "rocketmq.consumer.hook.current", desc = "Consumer拦截当前执行任务数")
//		private Long consumerHookCurrent;
//		@FieldReport(name = "rocketmq.consumer.hook.list.detail", desc = "Consumer拦截历史最大耗时任务列表")
//		private String consumerHookList;
//		@FieldReport(name = "rocketmq.consumer.hook.list.minute.detail", desc = "Consumer拦截历史最大耗时任务列表(每分钟)")
//		private String consumerHookListPerMinute;
//
//		@FieldReport(name = "rocketmq.producer.hook.error", desc = "Producer拦截上一次每秒出错次数")
//		private Long producerHookError;
//		@FieldReport(name = "rocketmq.producer.hook.success", desc = "Producer拦截上一次每秒成功次数")
//		private Long producerHookSuccess;
//		@FieldReport(name = "rocketmq.producer.hook.current", desc = "Producer拦截当前执行任务数")
//		private Long producerHookCurrent;
//		@FieldReport(name = "rocketmq.producer.hook.list.detail", desc = "Producer拦截历史最大耗时任务列表")
//		private String producerHookList;
//		@FieldReport(name = "rocketmq.producer.hook.list.minute.detail", desc = "Producer拦截历史最大耗时任务列表(每分钟)")
//		private String producerHookListPerMinute;
//
//		public RocketMQInfo() {
//		}
//
//		public RocketMQInfo(Long consumerHookError, Long consumerHookSuccess,
//			Long consumerHookCurrent, String consumerHookList,
//			String consumerHookListPerMinute, Long producerHookError,
//			Long producerHookSuccess, Long producerHookCurrent, String producerHookList,
//			String producerHookListPerMinute) {
//			this.consumerHookError = consumerHookError;
//			this.consumerHookSuccess = consumerHookSuccess;
//			this.consumerHookCurrent = consumerHookCurrent;
//			this.consumerHookList = consumerHookList;
//			this.consumerHookListPerMinute = consumerHookListPerMinute;
//			this.producerHookError = producerHookError;
//			this.producerHookSuccess = producerHookSuccess;
//			this.producerHookCurrent = producerHookCurrent;
//			this.producerHookList = producerHookList;
//			this.producerHookListPerMinute = producerHookListPerMinute;
//		}
//
//		public Long getConsumerHookError() {
//			return consumerHookError;
//		}
//
//		public void setConsumerHookError(Long consumerHookError) {
//			this.consumerHookError = consumerHookError;
//		}
//
//		public Long getConsumerHookSuccess() {
//			return consumerHookSuccess;
//		}
//
//		public void setConsumerHookSuccess(Long consumerHookSuccess) {
//			this.consumerHookSuccess = consumerHookSuccess;
//		}
//
//		public Long getConsumerHookCurrent() {
//			return consumerHookCurrent;
//		}
//
//		public void setConsumerHookCurrent(Long consumerHookCurrent) {
//			this.consumerHookCurrent = consumerHookCurrent;
//		}
//
//		public String getConsumerHookList() {
//			return consumerHookList;
//		}
//
//		public void setConsumerHookList(String consumerHookList) {
//			this.consumerHookList = consumerHookList;
//		}
//
//		public String getConsumerHookListPerMinute() {
//			return consumerHookListPerMinute;
//		}
//
//		public void setConsumerHookListPerMinute(String consumerHookListPerMinute) {
//			this.consumerHookListPerMinute = consumerHookListPerMinute;
//		}
//
//		public Long getProducerHookError() {
//			return producerHookError;
//		}
//
//		public void setProducerHookError(Long producerHookError) {
//			this.producerHookError = producerHookError;
//		}
//
//		public Long getProducerHookSuccess() {
//			return producerHookSuccess;
//		}
//
//		public void setProducerHookSuccess(Long producerHookSuccess) {
//			this.producerHookSuccess = producerHookSuccess;
//		}
//
//		public Long getProducerHookCurrent() {
//			return producerHookCurrent;
//		}
//
//		public void setProducerHookCurrent(Long producerHookCurrent) {
//			this.producerHookCurrent = producerHookCurrent;
//		}
//
//		public String getProducerHookList() {
//			return producerHookList;
//		}
//
//		public void setProducerHookList(String producerHookList) {
//			this.producerHookList = producerHookList;
//		}
//
//		public String getProducerHookListPerMinute() {
//			return producerHookListPerMinute;
//		}
//
//		public void setProducerHookListPerMinute(String producerHookListPerMinute) {
//			this.producerHookListPerMinute = producerHookListPerMinute;
//		}
//	}
//}
