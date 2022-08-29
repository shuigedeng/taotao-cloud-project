package com.taotao.cloud.demo.utils;

import com.taotao.cloud.common.utils.exception.ExceptionUtils;
import com.taotao.cloud.common.utils.common.JsonUtils;
import org.junit.Test;

import java.io.IOException;

/**
 * Exceptions 工具测试
 *
 */
public class ExceptionsTest {

	@Test(expected = IOException.class)
	public void testIOException() {
		throw ExceptionUtils.unchecked(new IOException());
	}

	@Test(expected = IOException.class)
	public void testJson() {
		JsonUtils.readValue("`12123`", Object.class);
	}
}
