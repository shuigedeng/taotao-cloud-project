//package com.taotao.cloud.gateway.filter.global;
//
//import com.taotao.cloud.common.constant.CommonConstant;
//import com.taotao.cloud.common.utils.LogUtil;
//import com.taotao.cloud.common.utils.TraceUtil;
//import java.util.ArrayList;
//import java.util.List;
//import lombok.AllArgsConstructor;
//import org.apache.commons.lang3.StringUtils;
//import org.springframework.cloud.gateway.filter.GatewayFilterChain;
//import org.springframework.cloud.gateway.filter.GlobalFilter;
//import org.springframework.core.Ordered;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.server.reactive.ServerHttpResponse;
//import org.springframework.stereotype.Component;
//import org.springframework.web.server.ServerWebExchange;
//import reactor.core.publisher.Mono;
//
///**
// * 打印请求和响应简要日志
// *
// * @author pangu
// * @author L.cm
// * @since 2020-7-16
// */
//@Component
//@AllArgsConstructor
//public class RequestLogFilter implements GlobalFilter, Ordered {
//
//	private static final String START_TIME = "startTime";
//
//	@Override
//	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
//		String requestUrl = exchange.getRequest().getURI().getRawPath();
//		String traceId = exchange.getRequest().getHeaders().getFirst(CommonConstant.TAOTAO_CLOUD_TRACE_HEADER);
//		if (StringUtils.isBlank(traceId)) {
//			traceId = TraceUtil.getTraceId();
//		}
//		StringBuilder beforeReqLog = new StringBuilder();
//
//		List<Object> beforeReqArgs = new ArrayList<>();
//		beforeReqLog.append("\n\n================ TaoTao Cloud Request Start  ================\n");
//		beforeReqLog.append("===> requestMethod: {}, requestUrl: {}, traceId: {}\n");
//		String requestMethod = exchange.getRequest().getMethodValue();
//		beforeReqArgs.add(requestMethod);
//		beforeReqArgs.add(requestUrl);
//		beforeReqArgs.add(traceId);
//
//		HttpHeaders headers = exchange.getRequest().getHeaders();
//		headers.forEach((headerName, headerValue) -> {
//			beforeReqLog.append("===Headers===  {}: {}\n");
//			beforeReqArgs.add(headerName);
//			beforeReqArgs.add(StringUtils.join(headerValue));
//		});
//		beforeReqLog.append("================ TaoTao Cloud Request End =================\n");
//		LogUtil.info(beforeReqLog.toString(), beforeReqArgs.toArray());
//
//		exchange.getAttributes().put(START_TIME, System.currentTimeMillis());
//		return chain.filter(exchange).then(Mono.fromRunnable(() -> {
//			ServerHttpResponse response = exchange.getResponse();
//			HttpHeaders httpHeaders = response.getHeaders();
//			Long startTime = exchange.getAttribute(START_TIME);
//			long executeTime = 0L;
//			if (startTime != null) {
//				executeTime = (System.currentTimeMillis() - startTime);
//			}
//			String id = exchange.getRequest().getHeaders()
//				.getFirst(CommonConstant.TAOTAO_CLOUD_TRACE_HEADER);
//
//			StringBuilder responseLog = new StringBuilder();
//			List<Object> responseArgs = new ArrayList<>();
//			responseLog
//				.append("\n\n================ TaoTao Cloud Response Start  ================\n");
//			responseLog.append("<===  {}: {}: {}\n");
//			responseLog
//				.append("<=== requestMethod: {}, requestUrl: {}, traceId: {}, executeTime: {}\n");
//			responseArgs.add(requestMethod);
//			responseArgs.add(requestUrl);
//			responseArgs.add(id);
//			responseArgs.add(executeTime + "ms");
//
//			httpHeaders.forEach((headerName, headerValue) -> {
//				responseLog.append("===Headers===  {}: {}\n");
//				responseArgs.add(headerName);
//				responseArgs.add(StringUtils.join(headerValue));
//			});
//			exchange.getAttributes().remove(START_TIME);
//
//			responseLog.append("================  TaoTao Cloud Response End  =================\n");
//			LogUtil.info(responseLog.toString(), responseArgs.toArray());
//		}));
//	}
//
//	@Override
//	public int getOrder() {
//		return Ordered.LOWEST_PRECEDENCE;
//	}
//}
