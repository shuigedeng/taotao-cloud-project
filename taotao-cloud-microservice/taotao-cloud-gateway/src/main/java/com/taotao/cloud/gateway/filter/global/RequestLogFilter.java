package com.taotao.cloud.gateway.filter.global;

import cn.hutool.core.util.StrUtil;
import com.taotao.cloud.common.constant.CommonConstant;
import com.taotao.cloud.common.utils.JsonUtil;
import com.taotao.cloud.common.utils.LogUtil;
import com.taotao.cloud.common.utils.TraceUtil;
import com.taotao.cloud.gateway.properties.FilterProperties;
import java.util.ArrayList;
import java.util.List;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 打印请求和响应简要日志
 *
 * @author shuigedeng
 * @since 2020-7-16
 */
@Component
@ConditionalOnProperty(prefix = FilterProperties.PREFIX, name = "log", havingValue = "true", matchIfMissing = true)
public class RequestLogFilter implements GlobalFilter, Ordered {

	private static final String START_TIME = "startTime";

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		String requestUrl = exchange.getRequest().getURI().getRawPath();
		String traceId = exchange.getRequest().getHeaders()
			.getFirst(CommonConstant.TAOTAO_CLOUD_TRACE_HEADER);
		if (StrUtil.isBlank(traceId)) {
			traceId = TraceUtil.getTraceId();
		}
		StringBuilder beforeReqLog = new StringBuilder();

		List<Object> beforeReqArgs = new ArrayList<>();
		beforeReqLog.append("\n\n================ TaoTao Cloud Request Start  ================\n");
		beforeReqLog.append("===> requestMethod: {}, requestUrl: {}, traceId: {}\n");
		String requestMethod = exchange.getRequest().getMethodValue();
		beforeReqArgs.add(requestMethod);
		beforeReqArgs.add(requestUrl);
		beforeReqArgs.add(traceId);

		HttpHeaders headers = exchange.getRequest().getHeaders();
		String header = JsonUtil.toJSONString(headers);
		beforeReqLog.append("===Headers=== : {}\n");
		beforeReqArgs.add(header);
		beforeReqLog.append("================ TaoTao Cloud Request End =================\n");
		LogUtil.info(beforeReqLog.toString(), beforeReqArgs.toArray());

		exchange.getAttributes().put(START_TIME, System.currentTimeMillis());
		return chain.filter(exchange).then(Mono.fromRunnable(() -> {
			ServerHttpResponse response = exchange.getResponse();
			HttpHeaders httpHeaders = response.getHeaders();
			Long startTime = exchange.getAttribute(START_TIME);
			long executeTime = 0L;
			if (startTime != null) {
				executeTime = (System.currentTimeMillis() - startTime);
			}

			StringBuilder responseLog = new StringBuilder();
			List<Object> responseArgs = new ArrayList<>();
			responseLog
				.append("\n\n================ TaoTao Cloud Response Start  ================\n");
			responseLog
				.append(
					"<=== requestMethod: {}, requestUrl: {}, traceId: {}, executeTime: {}\n");
			responseArgs.add(requestMethod);
			responseArgs.add(requestUrl);
			responseArgs.add(TraceUtil.getTraceId());
			responseArgs.add(executeTime + "ms");

			String httpHeader = JsonUtil.toJSONString(httpHeaders);
			responseLog.append("===Headers=== : {4}\n");
			responseArgs.add(httpHeader);

			exchange.getAttributes().remove(START_TIME);
			responseLog.append("================  TaoTao Cloud Response End  =================\n");
			LogUtil.info(responseLog.toString(), responseArgs.toArray());
		}));
	}

	@Override
	public int getOrder() {
		return 2;
	}
}
