package com.taotao.cloud.common.cron;

import com.taotao.cloud.common.support.cron.pojo.TimeOfDay;
import com.taotao.cloud.common.support.cron.util.DateUtil;
import java.util.Date;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

public class DateUtilsTest {

	@Test
	public void testCalculate() {
		Date date = DateUtil.toDate("2018-11-17 12:00:12");
		Assert.assertEquals(2018, DateUtil.year(date));
		Assert.assertEquals(11, DateUtil.month(date));
		Assert.assertEquals(6, DateUtil.week(date));
		Assert.assertEquals(17, DateUtil.day(date));
		Assert.assertEquals(12, DateUtil.hour(date));
		Assert.assertEquals(0, DateUtil.minute(date));
		Assert.assertEquals(12, DateUtil.second(date));

		date = DateUtil.toDate("2018-11-18 12:00:12");
		Assert.assertEquals(0, DateUtil.week(date));
	}

	@Test
	public void testEqualsWithTolerance() {
		TimeOfDay base = new TimeOfDay(1, 2, 3);

		Assert.assertTrue(base.equalsWithTolerance(new TimeOfDay(1, 2, 3), 0));

		Assert.assertTrue(base.equalsWithTolerance(new TimeOfDay(1, 2, 4), 1));
		Assert.assertFalse(base.equalsWithTolerance(new TimeOfDay(1, 2, 5), 1));

		Assert.assertTrue(base.equalsWithTolerance(new TimeOfDay(1, 3, 4), 61));
		Assert.assertTrue(base.equalsWithTolerance(new TimeOfDay(2, 2, 5), 60 * 60 + 2));

		Assert.assertFalse(base.equalsWithTolerance(new TimeOfDay(1, 3, 4), 1));
		Assert.assertFalse(base.equalsWithTolerance(new TimeOfDay(1, 3, 4), 60));
		Assert.assertFalse(base.equalsWithTolerance(new TimeOfDay(2, 2, 5), 60 * 60));
	}
}
