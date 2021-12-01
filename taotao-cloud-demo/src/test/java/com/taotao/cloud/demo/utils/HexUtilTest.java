package com.taotao.cloud.demo.utils;

import com.taotao.cloud.common.secure.HexUtil;
import org.junit.Assert;
import org.junit.Test;

/**
 * Digest 测试
 *
 */
public class HexUtilTest {

	@Test
	public void test() {
		String text = "mica 最牛逼";
		String hexText = HexUtil.encodeToString(text);
		String decode = HexUtil.decodeToString(hexText);
		Assert.assertEquals(text, decode);
	}

}
