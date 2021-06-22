package com.taotao.cloud.common.base;

import com.taotao.cloud.common.exception.BaseException;
import com.taotao.cloud.common.utils.LogUtil;
import com.taotao.cloud.common.utils.NumberUtil;
import com.taotao.cloud.common.utils.PropertyUtil;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.val;

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
	public static Collector Default = new Collector();
	private volatile Map<String, Object> map = new ConcurrentHashMap();
	private Object lock = new Object();

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
				val o = (Hook) obj;
				o.setKey(key);
				o.setMaxLength(PropertyUtil.getPropertyCache(key + ".length", 10));
			}
			return obj;
		} catch (Exception e) {
			throw new BaseException(e.toString());
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
		@Getter
		@Setter
		protected Integer maxLength = 10;
		protected volatile Long lastSecond = 0L;
		protected volatile Long lastMinute = 0L;
		protected Method method;
		@Getter
		@Setter
		private String key;

		public Object run(String tag, Object obj, String methodName, Object[] params) {
			if (method == null) {
				val find = Arrays.stream(obj.getClass().getMethods())
					.filter(c -> methodName.equalsIgnoreCase(c.getName())).findFirst();
				if (find.isPresent() == false) {
					throw new BaseException("未找到方法:" + obj.getClass().getName() + "下" + methodName);
				}
				method = find.get();
			}

			return run(tag, () -> {
				try {
					return method.invoke(obj, params);
				} catch (Exception exp) {
					throw new BaseException(exp.toString());
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
				if (!PropertyUtil.getPropertyCache("taotao.cloud.collect.hook.enabled", true)) {
					return func.invoke();
				}
				current.getAndIncrement();
				//每秒重新计数,不用保证十分精确
				val second = System.currentTimeMillis() / 1000;
				if (second != lastSecond) {
					lastSecond = second;
					lastErrorPerSecond.set(0);
					lastSuccessPerSecond.set(0);
					if (lastSecond / 60 != lastMinute) {
						lastMinute = lastSecond / 60;
						sortListPerMinute.removeMore(0);
					}
				}
				val start = System.currentTimeMillis();
				T result = func.invoke();
				val timeSpan = System.currentTimeMillis() - start;
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
				val last = sortList.getLast();
				if (last != null) {
					lastMinTimeSpan = last.getTime();
				}
			} catch (Exception exp) {
				LogUtil.error("Collector hook 保存耗时统计出错", exp);
			}
		}

		protected void insertOrUpdatePerMinute(Object info, double timeSpan) {
			if (info == null || timeSpan < lastMinTimeSpanPerMinute) {
				return;
			}
			try {
				sortListPerMinute.add(new SortInfo(info, timeSpan, timeSpan, new AtomicLong(1)));
				sortListPerMinute.removeMore(this.maxLength);
				val last = sortListPerMinute.getLast();
				if (last != null) {
					lastMinTimeSpanPerMinute = last.getTime();
				}
			} catch (Exception exp) {
				LogUtil.error("Collector hook 保存耗时统计出错", exp);
			}
		}

		/**
		 * 最长耗时列表n条
		 *
		 * @return
		 */
		public SortList getMaxTimeSpanList() {
			return sortList;
		}

		/**
		 * 最长耗时列表n条每分钟
		 *
		 * @return
		 */
		public SortList getMaxTimeSpanListPerMinute() {
			return sortListPerMinute;
		}
	}

	/**
	 * 调用
	 */
	public static class Call {

		private Callable.Func0<Object> func;

		/**
		 * 设置回调
		 *
		 * @param func
		 */
		public void set(Callable.Func0<Object> func) {
			this.func = func;
		}

		/**
		 * 返回结果
		 *
		 * @return
		 */
		public Object run() {
			return this.func.invoke();
		}
	}

	@AllArgsConstructor
	@NoArgsConstructor
	@Data
	public static class SortInfo implements Comparable<SortInfo> {

		protected Object tag;
		protected double time;
		protected double maxTime;
		protected volatile AtomicLong count;

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
	}

	public static class SortList extends ConcurrentSkipListSet<SortInfo> {

		protected Map tagCache = new ConcurrentHashMap<Integer, Object>();

		@Override
		public boolean add(SortInfo sortInfo) {
			Integer hash = sortInfo.tag.hashCode();
			if (tagCache.containsKey(hash)) {
				val value = tagCache.get(hash);
				if (value != null) {
					//累加
					val sort = ((SortInfo) value);
					sort.getCount().getAndIncrement();
					if (sort.time < sortInfo.time) {
						sort.maxTime = sortInfo.time;
					}
				}
				return false;
			}
			if (tagCache.size() > super.size()) {
				LogUtil.info("tagCache 缓存存在溢出风险");
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
				val last = this.pollLast();
				if (last != null) {
					this.remove(last);
				}
				if (count < -10) {
					LogUtil
						.error("【严重bug】remove more,item:" + (last != null ? last.toString() : ""),
							new Exception("长时间无法移除导致死循环"));
					break;
				}
			}
		}

		public String toText() {
			StringBuilder sb = new StringBuilder();
			for (val o : this) {
				sb.append(String
					.format("[耗时ms]%s[tag]%s[次数]%s[最大耗时ms]%s\r\n", NumberUtil.scale(o.time, 2),
						o.tag.toString(), o.count, NumberUtil.scale(o.maxTime, 2)));
			}
			return sb.toString();
		}
	}
}
