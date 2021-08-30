package com.taotao.cloud.core.model;

import com.taotao.cloud.common.constant.StarterName;
import com.taotao.cloud.common.exception.BaseException;
import com.taotao.cloud.common.utils.ContextUtil;
import com.taotao.cloud.common.utils.LogUtil;
import com.taotao.cloud.common.utils.NumberUtil;
import com.taotao.cloud.core.model.Callable.Func0;
import com.taotao.cloud.core.properties.CoreProperties;
import com.taotao.cloud.core.utils.PropertyUtil;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 自定义采集器
 *
 * @author: chejiangyi
 * @version: 2019-07-30 16:11
 **/
public class Collector {

	/**
	 * 默认实例
	 */
	public static Collector DEFAULT = new Collector();

	private final Map<String, Object> map = new ConcurrentHashMap<>();

	private final Object lock = new Object();

	protected Object get(String key, Class type) {
		if (!map.containsKey(key)) {
			synchronized (lock) {
				if (!map.containsKey(key)) {
					Object create = createFactory(key, type);
					map.put(key, create);
				}
			}
		}
		return map.get(key);
	}

	private Object createFactory(String key, Class type) {
		try {
			Object obj = type.newInstance();
			if (type == Hook.class) {
				Hook o = (Hook) obj;
				o.setKey(key);
				o.setMaxLength(PropertyUtil.getPropertyCache(key + ".length", 10));
			}
			return obj;
		} catch (Exception e) {
			throw new BaseException(e);
		}
	}

	public Sum sum(String key) {
		return (Sum) get(key, Sum.class);
	}

	public Hook hook(String key) {
		return (Hook) get(key, Hook.class);
	}

	public Call call(String key) {
		return (Call) get(key, Call.class);
	}

	public Value value(String key) {
		return (Value) get(key, Value.class);
	}

	/**
	 * 设值
	 */
	public static class Value {

		protected Object value;

		public void set(Object value) {
			this.value = value;
		}

		public Object get() {
			return this.value;
		}

		public void reset() {
			this.value = null;
		}

		public Object getValue() {
			return value;
		}

		public void setValue(Object value) {
			this.value = value;
		}
	}


	/**
	 * 累加
	 */
	public static class Sum {

		protected AtomicInteger sum = new AtomicInteger(0);

		public void add(int count) {
			sum.addAndGet(count);
		}

		public int get() {
			return sum.get();
		}

		public void reset() {
			sum.set(0);
		}

		public AtomicInteger getSum() {
			return sum;
		}

		public void setSum(AtomicInteger sum) {
			this.sum = sum;
		}
	}

	/**
	 * 拦截
	 */
	public static class Hook {

		/**
		 * 当前正在处理数
		 */
		protected AtomicLong current = new AtomicLong(0);

		public Long getCurrent() {
			return current.get();
		}

		/**
		 * 最近每秒失败次数
		 */
		protected AtomicLong lastErrorPerSecond = new AtomicLong(0);

		public Long getLastErrorPerSecond() {
			return lastErrorPerSecond.get();
		}

		/**
		 * 最近每秒成功次数
		 */
		protected AtomicLong lastSuccessPerSecond = new AtomicLong(0);

		public Long getLastSuccessPerSecond() {
			return lastSuccessPerSecond.get();
		}

		protected SortList sortList = new SortList();
		protected double lastMinTimeSpan = 0;

		protected SortList sortListPerMinute = new SortList();
		protected double lastMinTimeSpanPerMinute = 0;
		protected Integer maxLength = 10;
		protected volatile Long lastSecond = 0L;
		protected volatile Long lastMinute = 0L;
		protected Method method;
		private String key;

		public Object run(String tag, Object obj, String methodName, Object[] params) {
			if (method == null) {
				Optional<Method> find = Arrays.stream(obj.getClass().getMethods())
					.filter(c -> methodName.equalsIgnoreCase(c.getName())).findFirst();
				if (!find.isPresent()) {
					throw new BaseException("未找到方法:" + obj.getClass().getName() + "下" + methodName);
				}
				method = find.get();
			}
			return run(tag, () -> {
				try {
					return method.invoke(obj, params);
				} catch (Exception exp) {
					throw new BaseException(exp);
				}
			});
		}

		public void run(String tag, Callable.Action0 action) {
			run(tag, () -> {
				action.invoke();
				return null;
			});
		}

		public <T> T run(String tag, Callable.Func0<T> func) {
			try {
				CoreProperties coreProperties = ContextUtil.getBean(CoreProperties.class, true);
				if (!coreProperties.isCollectHookEnabled()) {
					return func.invoke();
				}

				current.getAndIncrement();
				//每秒重新计数,不用保证十分精确
				long second = System.currentTimeMillis() / 1000;
				if (second != lastSecond) {
					lastSecond = second;
					lastErrorPerSecond.set(0);
					lastSuccessPerSecond.set(0);
					if (lastSecond / 60 != lastMinute) {
						lastMinute = lastSecond / 60;
						sortListPerMinute.removeMore(0);
					}
				}
				long start = System.currentTimeMillis();
				T result = func.invoke();
				long timeSpan = System.currentTimeMillis() - start;
				insertOrUpdate(tag, timeSpan);
				insertOrUpdatePerMinute(tag, timeSpan);
				lastSuccessPerSecond.getAndIncrement();
				return result;
			} catch (Exception exp) {
				lastErrorPerSecond.getAndIncrement();
				throw exp;
			} finally {
				current.getAndDecrement();
			}
		}

		protected void insertOrUpdate(Object info, double timeSpan) {
			if (info == null || timeSpan < lastMinTimeSpan) {
				return;
			}
			try {
				sortList.add(new SortInfo(info, timeSpan, timeSpan, new AtomicLong(1)));
				sortList.removeMore(this.maxLength);
				SortInfo last = sortList.getLast();
				if (last != null) {
					lastMinTimeSpan = last.getTime();
				}
			} catch (Exception exp) {
				LogUtil.error(Hook.class, StarterName.CLOUD_STARTER, "Collector hook 保存耗时统计出错",
					exp);
			}
		}

		protected void insertOrUpdatePerMinute(Object info, double timeSpan) {
			if (info == null || timeSpan < lastMinTimeSpanPerMinute) {
				return;
			}
			try {
				sortListPerMinute.add(new SortInfo(info, timeSpan, timeSpan, new AtomicLong(1)));
				sortListPerMinute.removeMore(this.maxLength);
				SortInfo last = sortListPerMinute.getLast();
				if (last != null) {
					lastMinTimeSpanPerMinute = last.getTime();
				}
			} catch (Exception exp) {
				LogUtil.error(Hook.class, StarterName.CLOUD_STARTER, "Collector hook 保存耗时统计出错",
					exp);
			}
		}

		/**
		 * 最长耗时列表n条
		 */
		public SortList getMaxTimeSpanList() {
			return sortList;
		}

		/**
		 * 最长耗时列表n条每分钟
		 */
		public SortList getMaxTimeSpanListPerMinute() {
			return sortListPerMinute;
		}


		public void setCurrent(AtomicLong current) {
			this.current = current;
		}

		public void setLastErrorPerSecond(AtomicLong lastErrorPerSecond) {
			this.lastErrorPerSecond = lastErrorPerSecond;
		}

		public void setLastSuccessPerSecond(AtomicLong lastSuccessPerSecond) {
			this.lastSuccessPerSecond = lastSuccessPerSecond;
		}

		public SortList getSortList() {
			return sortList;
		}

		public void setSortList(SortList sortList) {
			this.sortList = sortList;
		}

		public double getLastMinTimeSpan() {
			return lastMinTimeSpan;
		}

		public void setLastMinTimeSpan(double lastMinTimeSpan) {
			this.lastMinTimeSpan = lastMinTimeSpan;
		}

		public SortList getSortListPerMinute() {
			return sortListPerMinute;
		}

		public void setSortListPerMinute(SortList sortListPerMinute) {
			this.sortListPerMinute = sortListPerMinute;
		}

		public double getLastMinTimeSpanPerMinute() {
			return lastMinTimeSpanPerMinute;
		}

		public void setLastMinTimeSpanPerMinute(double lastMinTimeSpanPerMinute) {
			this.lastMinTimeSpanPerMinute = lastMinTimeSpanPerMinute;
		}

		public Integer getMaxLength() {
			return maxLength;
		}

		public void setMaxLength(Integer maxLength) {
			this.maxLength = maxLength;
		}

		public Long getLastSecond() {
			return lastSecond;
		}

		public void setLastSecond(Long lastSecond) {
			this.lastSecond = lastSecond;
		}

		public Long getLastMinute() {
			return lastMinute;
		}

		public void setLastMinute(Long lastMinute) {
			this.lastMinute = lastMinute;
		}

		public Method getMethod() {
			return method;
		}

		public void setMethod(Method method) {
			this.method = method;
		}

		public String getKey() {
			return key;
		}

		public void setKey(String key) {
			this.key = key;
		}
	}

	/**
	 * 调用
	 */
	public static class Call {

		private Callable.Func0<Object> func;

		/**
		 * 设置回调
		 */
		public void set(Callable.Func0<Object> func) {
			this.func = func;
		}

		/**
		 * 返回结果
		 */
		public Object run() {
			return this.func.invoke();
		}

		public Func0<Object> getFunc() {
			return func;
		}

		public void setFunc(Func0<Object> func) {
			this.func = func;
		}
	}

	public static class SortInfo implements Comparable<SortInfo> {

		protected Object tag;
		protected double time;
		protected double maxTime;
		protected volatile AtomicLong count;

		public SortInfo() {
		}

		public SortInfo(Object tag, double time, double maxTime,
			AtomicLong count) {
			this.tag = tag;
			this.time = time;
			this.maxTime = maxTime;
			this.count = count;
		}

		@Override
		public int compareTo(SortInfo o) {
			if (o.time > this.time) {
				return 1;
			} else if (o.time < this.time) {
				return -1;
			} else {
				return 0;
			}

		}

		@Override
		public String toString() {
			return tag.toString() + ":" + this.time;
		}

		@Override
		public int hashCode() {
			return tag.hashCode();
		}

		@Override
		public boolean equals(Object obj) {
			return obj.hashCode() == this.hashCode();
		}

		public Object getTag() {
			return tag;
		}

		public void setTag(Object tag) {
			this.tag = tag;
		}

		public double getTime() {
			return time;
		}

		public void setTime(double time) {
			this.time = time;
		}

		public double getMaxTime() {
			return maxTime;
		}

		public void setMaxTime(double maxTime) {
			this.maxTime = maxTime;
		}

		public AtomicLong getCount() {
			return count;
		}

		public void setCount(AtomicLong count) {
			this.count = count;
		}
	}

	public static class SortList extends ConcurrentSkipListSet<SortInfo> {

		protected Map tagCache = new ConcurrentHashMap<Integer, Object>();

		@Override
		public boolean add(SortInfo sortInfo) {
			Integer hash = sortInfo.tag.hashCode();
			if (tagCache.containsKey(hash)) {
				Object value = tagCache.get(hash);
				if (value != null) {
					//累加
					SortInfo sort = ((SortInfo) value);
					sort.getCount().getAndIncrement();
					if (sort.time < sortInfo.time) {
						sort.maxTime = sortInfo.time;
					}
				}
				return false;
			}
			if (tagCache.size() > super.size()) {
				LogUtil.info(SortList.class, StarterName.CLOUD_STARTER, "tag cache 缓存存在溢出风险");
			}
			if (super.add(sortInfo)) {
				tagCache.put(hash, sortInfo);
			}
			return true;
		}

		@Override
		public boolean remove(Object o) {
			Integer hash = ((SortInfo) o).tag.hashCode();
			tagCache.remove(hash);
			return super.remove(o);
		}

		public SortInfo getLast() {
			try {
				if (!this.isEmpty()) {
					return this.last();
				}
			} catch (NoSuchElementException e) {
			}
			return null;
		}


		public void removeMore(int maxLength) {
			int count = this.size();
			while (this.size() > maxLength) {
				count--;
				SortInfo last = this.pollLast();
				if (last != null) {
					this.remove(last);
				}
				if (count < -10) {
					LogUtil.error(SortList.class, StarterName.CLOUD_STARTER,
						"【严重bug】remove more,item:" + (last != null ? last.toString() : ""),
						new Exception("长时间无法移除导致死循环"));
					break;
				}
			}
		}

		public String toText() {
			StringBuilder sb = new StringBuilder();
			for (SortInfo o : this) {
				sb.append(String.format("[耗时ms]%s[tag]%s[次数]%s[最大耗时ms]%s\r\n",
					NumberUtil.scale(o.time, 2), o.tag.toString(), o.count,
					NumberUtil.scale(o.maxTime, 2)));
			}
			return sb.toString();
		}
	}
}
