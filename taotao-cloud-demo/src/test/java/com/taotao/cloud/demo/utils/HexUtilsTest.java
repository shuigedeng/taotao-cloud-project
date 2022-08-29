package com.taotao.cloud.demo.utils;

import com.taotao.cloud.common.utils.secure.HexUtils;
import org.junit.Assert;
import org.junit.Test;

/**
 * Digest 测试
 *
 */
public class HexUtilsTest {

	@Test
	public void test() {
		String text = "mica 最牛逼";
		String hexText = HexUtils.encodeToString(text);
		String decode = HexUtils.decodeToString(hexText);
		Assert.assertEquals(text, decode);
	}

}
