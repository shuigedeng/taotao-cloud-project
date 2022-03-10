package com.taotao.cloud.demo.utils;

import com.taotao.cloud.common.utils.exception.ExceptionUtil;
import com.taotao.cloud.common.utils.common.JsonUtil;
import org.junit.Test;

import java.io.IOException;

/**
 * Exceptions 工具测试
 *
 */
public class ExceptionsTest {

	@Test(expected = IOException.class)
	public void testIOException() {
		throw ExceptionUtil.unchecked(new IOException());
	}

	@Test(expected = IOException.class)
	public void testJson() {
		JsonUtil.readValue("`12123`", Object.class);
	}
}
