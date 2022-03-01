package com.taotao.cloud.core.cron.parser;


import com.taotao.cloud.core.cron.pojo.CronField;
import com.taotao.cloud.core.cron.pojo.CronPosition;
import com.taotao.cloud.core.cron.pojo.TimeOfDay;
import com.taotao.cloud.core.cron.util.CompareUtil;
import com.taotao.cloud.core.cron.util.CronUtil;
import com.taotao.cloud.core.cron.util.DateUtil;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class DayBasedCronParser implements CronParser {

	private static final int CRON_LEN_YEAR = 7;
	/**
	 * 以下字段在构造之后就不会变了
	 */
	private final String expression;
	private final TimeZone timeZone;

	private final List<CronField> cronFields;
	private final CronField fieldSecond;
	private final CronField fieldMinute;
	private final CronField fieldHour;
	private final CronField fieldDay;
	private final CronField fieldMonth;
	private final CronField fieldWeek;
	private final CronField fieldYear;


	public DayBasedCronParser(String expression) {
		this(expression, TimeZone.getDefault());
	}

	public DayBasedCronParser(String expression, TimeZone timeZone) {
		this.expression = expression;
		this.timeZone = timeZone;
		cronFields = CronUtil.convertCronField(expression);

		fieldSecond = cronFields.get(CronPosition.SECOND.ordinal());
		fieldMinute = cronFields.get(CronPosition.MINUTE.ordinal());
		fieldHour = cronFields.get(CronPosition.HOUR.ordinal());
		fieldDay = cronFields.get(CronPosition.DAY.ordinal());
		fieldMonth = cronFields.get(CronPosition.MONTH.ordinal());
		fieldWeek = cronFields.get(CronPosition.WEEK.ordinal());
		/// 如果包含年域
		if (CRON_LEN_YEAR == cronFields.size()) {
			fieldYear = cronFields.get(CronPosition.YEAR.ordinal());
		} else {
			fieldYear = null;
		}
	}

	/**
	 * 思路：  1、找到所有时分秒的组合并按照时分秒排序 2、给定的时分秒在以上集合之前、之后处理 3、给定时时分秒在以上集合中找到一个最小的位置
	 * 4、day+1循环直到找到满足月、星期的那一天 5、或者在列表中找到最小的即可
	 */
	@Override
	public Date next(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeZone(timeZone);
		//基准线,至少从下一秒开始
		calendar.setTime(date);
		calendar.add(Calendar.SECOND, 1);

		if (null != fieldYear) {
			Integer year = DateUtil.year(calendar);
			List<Integer> listYear = fieldYear.points();
			Integer calYear = CompareUtil.findNext(year, listYear);
			if (!year.equals(calYear)) {
				calendar.set(Calendar.YEAR, calYear);
			}
		}

		return CronUtil.doNext(calendar, fieldSecond, fieldMinute, fieldHour, fieldDay, fieldMonth,
			fieldWeek, fieldYear);
	}

	/**
	 * 思路：1、切割cron表达式 2、转换每个域 3、计算执行时间点（关键算法，解析cron表达式） 4、计算某一天的哪些时间点执行
	 */
	@Override
	public List<TimeOfDay> timesOfDay(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeZone(timeZone);
		calendar.setTime(date);

		int year = DateUtil.year(calendar);
		int week = DateUtil.week(calendar);
		int month = DateUtil.month(calendar);
		int day = DateUtil.day(calendar);
		/// 如果包含年域
		if (null != fieldYear) {
			if (!CronUtil.satisfy(year, fieldYear)) {
				return Collections.emptyList();
			}
		}

		///今天不执行就直接返回空
		if (!CronUtil.satisfy(week, fieldWeek)
			|| !CronUtil.satisfy(month, fieldMonth)
			|| !CronUtil.satisfy(day, fieldDay)) {
			return Collections.emptyList();
		}

		CronField fieldHour = cronFields.get(CronPosition.HOUR.ordinal());
		CronField fieldMinute = cronFields.get(CronPosition.MINUTE.ordinal());
		CronField fieldSecond = cronFields.get(CronPosition.SECOND.ordinal());

		return CronUtil.timesOfDay(fieldHour, fieldMinute, fieldSecond);
	}
}
