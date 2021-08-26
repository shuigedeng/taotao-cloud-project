package com.taotao.cloud.netty.support;

import static com.taotao.cloud.netty.pojo.PojoEndpointServer.SESSION_KEY;

import com.taotao.cloud.netty.pojo.Session;
import io.netty.channel.Channel;
import org.springframework.core.MethodParameter;


public class SessionMethodArgumentResolver implements MethodArgumentResolver {

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return Session.class.isAssignableFrom(parameter.getParameterType());
	}

	@Override
	public Object resolveArgument(MethodParameter parameter, Channel channel, Object object)
		throws Exception {
		Session session = channel.attr(SESSION_KEY).get();
		return session;
	}
}
