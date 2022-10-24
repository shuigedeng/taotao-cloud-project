package com.taotao.cloud.job.schedule.utils;

import com.taotao.cloud.job.schedule.model.constant.CycleTypeConstant;
import com.taotao.cloud.job.schedule.model.param.TaskParam;
import org.quartz.CronExpression;
import org.quartz.TriggerUtils;
import org.quartz.impl.triggers.CronTriggerImpl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * cron表达式工具类
 */
public class CronUtils {
	/**
	 * 返回一个布尔值代表一个给定的Cron表达式的有效性
	 *
	 * @param cronExpression Cron表达式
	 * @return boolean 表达式是否有效
	 */
	private static boolean isValid(String cronExpression) {
		return CronExpression.isValidExpression(cronExpression);
	}

	/**
	 * 返回下一个执行时间根据给定的Cron表达式
	 *
	 * @param cronExpression Cron表达式
	 * @param num            获取数量
	 * @param format         是否格式化为字符串
	 * @return Date 下次Cron表达式执行时间
	 */
	public static Object getNextExecution(String cronExpression, int num, boolean format) {

		List<String> list = new ArrayList<>();
		try {
			CronTriggerImpl cronTriggerImpl = new CronTriggerImpl();
			cronTriggerImpl.setCronExpression(cronExpression);
			// 近n次时间
			List<Date> dates = TriggerUtils.computeFireTimes(cronTriggerImpl, null, num);
			//不进行格式化
			if (!format) {
				return dates;
			}
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			for (Date date : dates) {
				list.add(dateFormat.format(date));
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * 将用户选择的时候转换未cron表达式
	 *
	 * @return str
	 */
	public static String dateConvertToCron(TaskParam param) {
		//秒 分 时 日 月 默认为'*'，周为'?'
		//特殊处理，选择周 就不能拼接月、日；选择月就不能拼接周；选择日不能拼接周,并且默认每月
		//周不能选择为每周

		String finalWeek = "?", finalMonth = "*", finalDay = "*", finalHour = "*", finalMinute = "*", finalSecods = "*";

		switch (param.getCycle()) {
			case CycleTypeConstant.WEEK -> {
				//选择周 就不能拼接月、日
				//周日为1，周六为7，需要将前端传递的数字加1处理
				String[] split = param.getWeek().split(",");
				List<String> newWeek = new ArrayList<>();
				for (String s : split) {
					if ("7".equals(s)) {
						newWeek.add("6");
					} else {
						newWeek.add(Integer.valueOf(s) + 1 + "");
					}
				}
				//判断类型
				finalWeek = String.join(",", newWeek);
				finalMonth = "*";
				finalDay = "?";
				finalHour = param.getHour();
				finalMinute = param.getMinute();
				finalSecods = param.getSecods();
			}
			case CycleTypeConstant.MONTH -> {
				finalMonth = param.getMonth();
				finalDay = param.getDay();
				finalHour = param.getHour();
				finalMinute = param.getMinute();
				finalSecods = param.getSecods();
			}
			case CycleTypeConstant.DAY -> {
				finalDay = param.getDay();
				finalHour = param.getHour();
				finalMinute = param.getMinute();
				finalSecods = param.getSecods();
			}
			case CycleTypeConstant.HOUR -> {
				finalHour = param.getHour();
				finalMinute = param.getMinute();
				finalSecods = param.getSecods();
			}
			case CycleTypeConstant.MINUTE -> {
				finalMinute = param.getMinute();
				finalSecods = param.getSecods();
			}
			case CycleTypeConstant.SECODS -> finalSecods = param.getSecods();
			default -> throw new RuntimeException("周期解析出错!");
		}
		//已空格拼接
		String cron = finalSecods + " " + finalMinute + " " +
			finalHour + " " + finalDay + " " +
			finalMonth + " " + finalWeek;
		if (!isValid(cron)) {
			throw new RuntimeException("表达式解析出错!");
		}
		return cron;
	}

	// 与当前传入时间对比，如果时间差小于1s则获取第二个时间，
	// 主要为了解决时间周期很短的任务会出现返回的下次执行时间还在周期内
	public static Date nextCurrentTime(String cron) {
		//如果存在获取的新的下次执行时间和当前已执行的时间一致，则返回第二个执行时间
		List<Date> execution = (List<Date>) getNextExecution(cron, 2, false);
		long timeMillis = System.currentTimeMillis();
		if (Math.abs(timeMillis - execution.get(0).getTime()) > 1000) {
			return execution.get(0);
		}
		//因为如果还在当前任务执行时间节点上，获取到的下次时间和当前是一致的，所有要获取下下次的时间节点
		return execution.get(1);
	}

	public static Date nextTime(String cron) {
		//如果存在获取的新的下次执行时间和当前已执行的时间一致，则返回第二个执行时间
		List<Date> execution = (List<Date>) getNextExecution(cron, 2, false);

		//因为如果还在当前任务执行时间节点上，获取到的下次时间和当前是一致的，所有要获取下下次的时间节点
		return execution.get(1);
	}
}
