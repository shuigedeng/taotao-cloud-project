package com.taotao.cloud.xss;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import com.taotao.cloud.xss.support.XssStringJsonDeserializer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class JsonDeserializerTest {


	@Test
	void test() throws JsonProcessingException {
		Assertions.assertThrows(MismatchedInputException.class, () -> {
			ObjectMapper objectMapper = new ObjectMapper();
			DemoBean demoBean = objectMapper.readValue("{\n"
				+ "    \"pageNum\": 1,\n"
				+ "    \"pageSize\": 15,\n"
				+ " \n"
				+ "    \"createDate\":[\"12\"],\n"
				+ "       \"system\": \"1\",\n"
				+ "       \"isRead\": 1,\n"
				+ "    \"issue\": \"qweqweq\"\n"
				+ "}", DemoBean.class);
			System.out.println(demoBean);
		});
	}

	static class DemoBean {
		private Integer pageNum;
		private Integer pageSize;
		@JsonDeserialize(using = XssStringJsonDeserializer.class)
		private String createDate;
		private Integer isRead;

		public Integer getPageNum() {
			return pageNum;
		}

		public void setPageNum(Integer pageNum) {
			this.pageNum = pageNum;
		}

		public Integer getPageSize() {
			return pageSize;
		}

		public void setPageSize(Integer pageSize) {
			this.pageSize = pageSize;
		}

		public String getCreateDate() {
			return createDate;
		}

		public void setCreateDate(String createDate) {
			this.createDate = createDate;
		}

		public Integer getIsRead() {
			return isRead;
		}

		public void setIsRead(Integer isRead) {
			this.isRead = isRead;
		}
	}

}
