package com.taotao.cloud.ai.spring;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.taotao.cloud.ai.spring.XueQiuFinanceService.FinanceRequest;
import java.util.function.Function;
import org.springframework.web.client.RestTemplate;

public class XueQiuFinanceService implements Function<FinanceRequest, String> {

	@Override
	public String apply(FinanceRequest request) {
		RestTemplate restTemplate = new RestTemplate();
		String url =
			"https://stock.xueqiu.com/v5/stock/finance/cn/income.json?symbol=" + request.getSymbol()
				+ "&type=all&is_detail=true&count=1";
		String response = restTemplate.getForObject(url, String.class);

		// 解析response并生成简单分析，这里仅做示意
		return "解析后的数据及简要分析：" + response;
	}

	public static class FinanceRequest {

		@JsonProperty(required = true, value = "公司代码")
		@JsonPropertyDescription("上市公司的股票代码")
		private String symbol;

		public FinanceRequest() {
		}

		public FinanceRequest(String symbol) {
			this.symbol = symbol;
		}

		public String getSymbol() {
			return symbol;
		}

		public void setSymbol(String symbol) {
			this.symbol = symbol;
		}
	}
}
