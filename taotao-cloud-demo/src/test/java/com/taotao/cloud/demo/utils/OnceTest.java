package com.taotao.cloud.demo.utils;

import com.taotao.cloud.common.model.Once;
import org.junit.Assert;
import org.junit.Test;

public class OnceTest {

	@Test
	public void test() {
		Once once = new Once();
		Assert.assertTrue(once.canRun());
		Assert.assertFalse(once.canRun());
		Assert.assertFalse(once.canRun());
		Assert.assertFalse(once.canRun());
		Assert.assertFalse(once.canRun());
		Assert.assertFalse(once.canRun());
	}
}
