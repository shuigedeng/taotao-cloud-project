package com.taotao.cloud.demo.utils;

import org.dromara.hutoolcore.date.DateUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.taotao.cloud.common.utils.common.JsonUtils;
import org.junit.Assert;
import org.junit.Test;

import java.util.Date;

/**
 * json node 测试
 *
 */
public class JsonNodeTest {

	@Test
	public void test() {
		String json = "123123";
		JsonNode jsonNode = JsonUtils.readTree(json);

		Assert.assertEquals(jsonNode.toString(), json);
	}

	@Test
	public void test1() {
		String json = "{\"code\":1}";
		JsonNode jsonNode = JsonUtils.readTree(json);

		//R<?> r = JsonUtil.getInstance().convertValue(jsonNode, R.class);
		//
		//Assert.assertEquals(r.getCode(), 1);
	}

	@Test
	public void test2() {
		String json1 = "1{\"code\":1}";
		boolean isJson1 = JsonUtils.isValidJson(json1);
		Assert.assertFalse(isJson1);

		String json2 = "/**/{\"code\":1}";
		boolean isJson2 = JsonUtils.isValidJson(json2);
		Assert.assertFalse(isJson2);

		String json3 = "{\"code\":1}";
		boolean isJson3 = JsonUtils.isValidJson(json3);
		Assert.assertTrue(isJson3);
	}

	@Test
	public void test3() {
		Date date = new Date();
		TestBean bean = new TestBean();
		bean.setDate(date);

		String dateTime = DateUtil.formatDateTime(date);
		TestBean testBean = JsonUtils.readValue(JsonUtils.toJson(bean), TestBean.class);
		Assert.assertEquals(dateTime, DateUtil.formatDateTime(testBean.getDate()));
	}

	public static class TestBean {
		private Date date;
		private String xxx;

		public Date getDate() {
			return date;
		}

		public void setDate(Date date) {
			this.date = date;
		}

		public String getXxx() {
			return xxx;
		}

		public void setXxx(String xxx) {
			this.xxx = xxx;
		}
	}

}
