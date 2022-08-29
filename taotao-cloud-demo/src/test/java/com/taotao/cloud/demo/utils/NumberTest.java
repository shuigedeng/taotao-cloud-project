package com.taotao.cloud.demo.utils;

import com.taotao.cloud.common.utils.number.NumberUtils;
import org.junit.Assert;
import org.junit.Test;

public class NumberTest {

	@Test
	public void testTo62String() {
		long ms = 1551320493447L;
		String string = NumberUtils.to62Str(ms);
		Assert.assertEquals(string, "rjkOH7p");
	}

}
