package com.taotao.cloud.core.cron;

import com.taotao.cloud.core.cron.parser.DayBasedCronParser;
import com.taotao.cloud.core.cron.pojo.CronField;
import com.taotao.cloud.core.cron.pojo.TimeOfDay;
import com.taotao.cloud.core.cron.util.CronUtil;
import com.taotao.cloud.core.cron.util.DateUtil;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;

public class CronTest {

	@Test
	public void testCut() {
		String cron = "0     15    10   ? *    *   ";
		Assert.assertEquals(
			Arrays.asList("0", "15", "10", "?", "*", "*"),
			CronUtil.cut(cron));
	}

	@Test
	public void testConvertField() {
		List<CronField> cronFields = CronUtil.convertCronField("0 0-5 14/2 * * ?");
		for (int i = 0; i < 6; i++) {
			CronField field = cronFields.get(i);
			Assert.assertEquals(field.getCronPosition().ordinal(), i);
		}
		Assert.assertEquals("0", cronFields.get(0).getExpress());
		Assert.assertEquals("0-5", cronFields.get(1).getExpress());
		Assert.assertEquals("14/2", cronFields.get(2).getExpress());
		Assert.assertEquals("*", cronFields.get(3).getExpress());
		Assert.assertEquals("*", cronFields.get(4).getExpress());
		Assert.assertEquals("*", cronFields.get(5).getExpress());
		List<CronField> fields = CronUtil.convertCronField("0 15 10 ? JAN-NOV MON-FRI");
		for (int i = 0; i < 6; i++) {
			CronField field = fields.get(i);
			Assert.assertEquals(field.getCronPosition().ordinal(), i);
		}
		Assert.assertEquals("0", fields.get(0).getExpress());
		Assert.assertEquals("15", fields.get(1).getExpress());
		Assert.assertEquals("10", fields.get(2).getExpress());
		Assert.assertEquals("*", fields.get(3).getExpress());
		Assert.assertEquals("1-11", fields.get(4).getExpress());
		Assert.assertEquals("1-5", fields.get(5).getExpress());

		//包含年域的情况
		cronFields = CronUtil.convertCronField("0 15 10 ? JAN-NOV MON-FRi 2018");
		System.out.println(cronFields);
	}

	@Test
	public void testConvertCronField() {
		List<CronField> cronFields = CronUtil.convertCronField("1 0-5 1/3 1,3,4 1-11/2 ?");
		Assert.assertEquals(Collections.singletonList(1), cronFields.get(0).points());
		Assert.assertEquals(Arrays.asList(0, 1, 2, 3, 4, 5), cronFields.get(1).points());
		Assert.assertEquals(Arrays.asList(1, 4, 7, 10, 13, 16, 19, 22), cronFields.get(2).points());
		Assert.assertEquals(Arrays.asList(1, 3, 4), cronFields.get(3).points());
		Assert.assertEquals(Arrays.asList(1, 3, 5, 7, 9, 11), cronFields.get(4).points());
		Assert.assertEquals(Arrays.asList(0, 1, 2, 3, 4, 5, 6), cronFields.get(5).points());
	}

	@Test
	public void testCal() {
		Date date = DateUtil.toDate("2018-11-18 12:00:12");
		List<TimeOfDay> calculate = CronUtil.timesOfDay("0 15 10 ? * *", date);
		Assert.assertEquals(Collections.singletonList(new TimeOfDay(10, 15, 0)), calculate);

		String cron = "0-5 15 10 ? * *";
		calculate = CronUtil.timesOfDay(cron, date);
		Assert.assertEquals(Arrays.asList(
			new TimeOfDay(10, 15, 0),
			new TimeOfDay(10, 15, 1),
			new TimeOfDay(10, 15, 2),
			new TimeOfDay(10, 15, 3),
			new TimeOfDay(10, 15, 4),
			new TimeOfDay(10, 15, 5)), calculate);
		Assert.assertEquals(calculate, new DayBasedCronParser(cron).timesOfDay(date));

		cron = "0 1/10 10 ? * *";
		calculate = CronUtil.timesOfDay(cron, date);
		Assert.assertEquals(Arrays.asList(
			new TimeOfDay(10, 1, 0),
			new TimeOfDay(10, 11, 0),
			new TimeOfDay(10, 21, 0),
			new TimeOfDay(10, 31, 0),
			new TimeOfDay(10, 41, 0),
			new TimeOfDay(10, 51, 0)), calculate);
		Assert.assertEquals(calculate, new DayBasedCronParser(cron).timesOfDay(date));

		cron = "0 1,4,6,8,10,50 10 ? * *";
		calculate = CronUtil.timesOfDay(cron, date);
		Assert.assertEquals(Arrays.asList(
			new TimeOfDay(10, 1, 0),
			new TimeOfDay(10, 4, 0),
			new TimeOfDay(10, 6, 0),
			new TimeOfDay(10, 8, 0),
			new TimeOfDay(10, 10, 0),
			new TimeOfDay(10, 50, 0)), calculate);
		Assert.assertEquals(calculate, new DayBasedCronParser(cron).timesOfDay(date));

		cron = "0 1-30/5 10 ? * *";
		calculate = CronUtil.timesOfDay(cron, date);
		Assert.assertEquals(Arrays.asList(
			new TimeOfDay(10, 1, 0),
			new TimeOfDay(10, 6, 0),
			new TimeOfDay(10, 11, 0),
			new TimeOfDay(10, 16, 0),
			new TimeOfDay(10, 21, 0),
			new TimeOfDay(10, 26, 0)), calculate);
		Assert.assertEquals(calculate, new DayBasedCronParser(cron).timesOfDay(date));

		cron = "0 1-30/5 10 ? * SUN";
		calculate = CronUtil.timesOfDay(cron, date);
		Assert.assertEquals(Arrays.asList(
			new TimeOfDay(10, 1, 0),
			new TimeOfDay(10, 6, 0),
			new TimeOfDay(10, 11, 0),
			new TimeOfDay(10, 16, 0),
			new TimeOfDay(10, 21, 0),
			new TimeOfDay(10, 26, 0)), calculate);
		Assert.assertEquals(calculate, new DayBasedCronParser(cron).timesOfDay(date));

		cron = "0 1-30/5 10 ? 11 *";
		calculate = CronUtil.timesOfDay(cron, date);
		Assert.assertEquals(Arrays.asList(
			new TimeOfDay(10, 1, 0),
			new TimeOfDay(10, 6, 0),
			new TimeOfDay(10, 11, 0),
			new TimeOfDay(10, 16, 0),
			new TimeOfDay(10, 21, 0),
			new TimeOfDay(10, 26, 0)), calculate);
		Assert.assertEquals(calculate, new DayBasedCronParser(cron).timesOfDay(date));

		cron = "0 1-4,43 10 ? 11 *";
		calculate = CronUtil.timesOfDay(cron, date);
		Assert.assertEquals(Arrays.asList(
			new TimeOfDay(10, 1, 0),
			new TimeOfDay(10, 2, 0),
			new TimeOfDay(10, 3, 0),
			new TimeOfDay(10, 4, 0),
			new TimeOfDay(10, 43, 0)), calculate);
		Assert.assertEquals(calculate, new DayBasedCronParser(cron).timesOfDay(date));

		cron = "0 1-10/2,43 10 ? 11 *";
		calculate = CronUtil.timesOfDay(cron, date);
		Assert.assertEquals(Arrays.asList(
			new TimeOfDay(10, 1, 0),
			new TimeOfDay(10, 3, 0),
			new TimeOfDay(10, 5, 0),
			new TimeOfDay(10, 7, 0),
			new TimeOfDay(10, 9, 0),
			new TimeOfDay(10, 43, 0)), calculate);
		Assert.assertEquals(calculate, new DayBasedCronParser(cron).timesOfDay(date));

		cron = "0 7,1-5/2,5,6 10 ? 11 *";
		calculate = CronUtil.timesOfDay(cron, date);
		Assert.assertEquals(Arrays.asList(
			new TimeOfDay(10, 1, 0),
			new TimeOfDay(10, 3, 0),
			new TimeOfDay(10, 5, 0),
			new TimeOfDay(10, 6, 0),
			new TimeOfDay(10, 7, 0)), calculate);
		Assert.assertEquals(calculate, new DayBasedCronParser(cron).timesOfDay(date));

		cron = "0 1-6/2,12-27/5 10 ? 11 *";
		calculate = CronUtil.timesOfDay(cron, date);
		Assert.assertEquals(Arrays.asList(
			new TimeOfDay(10, 1, 0),
			new TimeOfDay(10, 3, 0),
			new TimeOfDay(10, 5, 0),
			new TimeOfDay(10, 12, 0),
			new TimeOfDay(10, 17, 0),
			new TimeOfDay(10, 22, 0),
			new TimeOfDay(10, 27, 0)), calculate);
		Assert.assertEquals(calculate, new DayBasedCronParser(cron).timesOfDay(date));

		///星期一到星期六执行,星期天不执行就返回空集合
		cron = "0 1-30/5 10 ? * MON-SAT";
		calculate = CronUtil.timesOfDay(cron, date);
		Assert.assertEquals(Collections.emptyList(), calculate);
		Assert.assertEquals(calculate, new DayBasedCronParser(cron).timesOfDay(date));
	}


	@Test(expected = IllegalArgumentException.class)
	public void testException1() {
		Date date = DateUtil.toDate("2018-11-18 12:00:12");
		CronUtil.timesOfDay("5-0 15 10 ? * *", date);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testException2() {
		Date date = DateUtil.toDate("2018-11-18 12:00:12");
		CronUtil.timesOfDay("1-62 15 10 ? * *", date);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testException3() {
		Date date = DateUtil.toDate("2018-11-18 12:00:12");
		CronUtil.timesOfDay("1 2-78 10 ? * *", date);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testException4() {
		Date date = DateUtil.toDate("2018-11-18 12:00:12");
		CronUtil.timesOfDay("2 15 25 ? * *", date);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testException5() {
		Date date = DateUtil.toDate("2018-11-18 12:00:12");
		CronUtil.timesOfDay("2 15 23 2-32 * *", date);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testException6() {
		Date date = DateUtil.toDate("2018-11-18 12:00:12");
		CronUtil.timesOfDay("2 15 23 3 1-13/2 *", date);
	}
}
