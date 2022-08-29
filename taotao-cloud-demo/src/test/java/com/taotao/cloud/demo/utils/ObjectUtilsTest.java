package com.taotao.cloud.demo.utils;

import com.taotao.cloud.common.utils.lang.ObjectUtils;
import org.junit.Assert;
import org.junit.Test;

import java.util.Collections;

/**
 * ObjectUtil Tester.
 *
 */
public class ObjectUtilsTest {

	/**
	 * Method: isNull(@Nullable Object object)
	 */
	@Test
	public void testIsNull() throws Exception {
		Assert.assertTrue(ObjectUtils.isNull(null));
	}

	/**
	 * Method: isNotNull(@Nullable Object object)
	 */
	@Test
	public void testIsNotNull() throws Exception {
		Assert.assertFalse(ObjectUtils.isNotNull(null));
	}

	/**
	 * Method: isTrue(@Nullable Boolean object)
	 */
	@Test
	public void testIsTrue() throws Exception {
		Assert.assertTrue(ObjectUtils.isTrue(true));
		Assert.assertTrue(ObjectUtils.isTrue(Boolean.TRUE));
		Assert.assertFalse(ObjectUtils.isTrue(null));
		Assert.assertFalse(ObjectUtils.isTrue(false));
		Assert.assertFalse(ObjectUtils.isTrue(Boolean.FALSE));
	}

	/**
	 * Method: isFalse(@Nullable Boolean object)
	 */
	@Test
	public void testIsFalse() throws Exception {
		Assert.assertTrue(ObjectUtils.isFalse(null));
		Assert.assertTrue(ObjectUtils.isFalse(false));
		Assert.assertTrue(ObjectUtils.isFalse(Boolean.FALSE));
		Assert.assertFalse(ObjectUtils.isFalse(true));
		Assert.assertFalse(ObjectUtils.isFalse(Boolean.TRUE));
	}

	/**
	 * Method: isNotEmpty(@Nullable Object[] array)
	 */
	@Test
	public void testIsNotEmpty() throws Exception {
		Assert.assertFalse(ObjectUtils.isNotEmpty(null));
		Assert.assertFalse(ObjectUtils.isNotEmpty(new Object[0]));
		Assert.assertFalse(ObjectUtils.isNotEmpty(Collections.emptyList()));
	}

	/**
	 * Method: isNotEmpty(@Nullable Object obj)
	 */
	@Test
	public void testIsEmptyObj() throws Exception {
		Assert.assertTrue(ObjectUtils.isEmpty(null));
		Assert.assertTrue(ObjectUtils.isEmpty(new Object[0]));
		Assert.assertTrue(ObjectUtils.isEmpty(Collections.emptyList()));
	}

	/**
	 * Method: toBoolean(@Nullable Object obj)
	 */
	@Test
	public void testToBoolean() throws Exception {
		Assert.assertNull(ObjectUtils.toBoolean(null));
		Assert.assertNull(ObjectUtils.toBoolean("a"));
		Assert.assertTrue(ObjectUtils.toBoolean("1"));
		Assert.assertTrue(ObjectUtils.toBoolean(null, true));
		Assert.assertTrue(ObjectUtils.toBoolean("a", true));
		Assert.assertTrue(ObjectUtils.toBoolean("1", true));
	}

}
