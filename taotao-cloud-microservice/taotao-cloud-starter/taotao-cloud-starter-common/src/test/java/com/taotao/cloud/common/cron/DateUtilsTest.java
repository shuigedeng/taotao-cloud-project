package com.taotao.cloud.common.cron;

import com.taotao.cloud.common.support.cron.pojo.TimeOfDay;
import com.taotao.cloud.common.support.cron.util.DateUtil;
import java.util.Date;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class DateUtilsTest {

	@Test
	public void testCalculate() {
		Date date = DateUtil.toDate("2018-11-17 12:00:12");
		Assertions.assertEquals(2018, DateUtil.year(date));
		Assertions.assertEquals(11, DateUtil.month(date));
		Assertions.assertEquals(6, DateUtil.week(date));
		Assertions.assertEquals(17, DateUtil.day(date));
		Assertions.assertEquals(12, DateUtil.hour(date));
		Assertions.assertEquals(0, DateUtil.minute(date));
		Assertions.assertEquals(12, DateUtil.second(date));

		date = DateUtil.toDate("2018-11-18 12:00:12");
		Assertions.assertEquals(0, DateUtil.week(date));
	}

	@Test
	public void testEqualsWithTolerance() {
		TimeOfDay base = new TimeOfDay(1, 2, 3);

		Assertions.assertTrue(base.equalsWithTolerance(new TimeOfDay(1, 2, 3), 0));

		Assertions.assertTrue(base.equalsWithTolerance(new TimeOfDay(1, 2, 4), 1));
		Assertions.assertFalse(base.equalsWithTolerance(new TimeOfDay(1, 2, 5), 1));

		Assertions.assertTrue(base.equalsWithTolerance(new TimeOfDay(1, 3, 4), 61));
		Assertions.assertTrue(base.equalsWithTolerance(new TimeOfDay(2, 2, 5), 60 * 60 + 2));

		Assertions.assertFalse(base.equalsWithTolerance(new TimeOfDay(1, 3, 4), 1));
		Assertions.assertFalse(base.equalsWithTolerance(new TimeOfDay(1, 3, 4), 60));
		Assertions.assertFalse(base.equalsWithTolerance(new TimeOfDay(2, 2, 5), 60 * 60));
	}
}
