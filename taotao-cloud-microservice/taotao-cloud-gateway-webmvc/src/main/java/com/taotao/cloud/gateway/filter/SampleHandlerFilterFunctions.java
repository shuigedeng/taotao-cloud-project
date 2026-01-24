package com.taotao.cloud.gateway.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.server.mvc.common.Configurable;
import org.springframework.cloud.gateway.server.mvc.common.Shortcut;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.function.HandlerFilterFunction;
import org.springframework.web.servlet.function.ServerRequest;
import org.springframework.web.servlet.function.ServerResponse;

import java.util.Objects;
import java.util.function.Function;

/**
 * @author 决定“我管了之后要做什么手脚”（改请求、加头、鉴权、限流、改响应……）
 * @date 2025/12/15
 * @description 定义包含所有自定义filter的函数式过滤器类,供filtersupplier类使用暴露到项目中
 * 一个类只能定义一个静态方法，spring cloud gateway只注册一个静态方法
 *
 * 提示：定义的过滤器类按一个类一个静态方法，多增加几个静态方法spring也只会加载第一个静态方法，静态方法一定要加@Shotcut注解，不然启动报错，报错原因分析及解决，将在另一篇关于gateway疑难杂症解决的文章中展开。
 * 过滤器定义（SampleHandlerFilterFunctions.java）:一个类一个静态过滤器方法（方法上一定要加上@Shortcut注解），多加了也没用spring只会加载第一个静态方法
 */
public class SampleHandlerFilterFunctions {

	private static final Logger log = LoggerFactory.getLogger(SampleHandlerFilterFunctions.class);

	@Shortcut
	public static HandlerFilterFunction<ServerResponse, ServerResponse> instrumentForFilter( String reqHeaderName,
		String repHeaderName ) {
		return ( request, next ) -> {
			log.error("==========进入自定义filter处理逻辑，请求头：{},响应头{}===========", reqHeaderName, repHeaderName);
			ServerRequest modified = ServerRequest.from(request)
				.header(reqHeaderName, "request header for filter").build();
			ServerResponse response = next.handle(modified);
			response.headers().add(repHeaderName, "response header for filter");
			return response;
		};
	}
}
