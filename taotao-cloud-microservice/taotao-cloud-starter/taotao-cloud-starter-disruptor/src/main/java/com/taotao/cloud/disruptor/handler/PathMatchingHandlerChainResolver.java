/*
 * Copyright 2002-2021 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.taotao.cloud.disruptor.handler;

import com.taotao.cloud.common.utils.log.LogUtil;
import com.taotao.cloud.disruptor.event.DisruptorEvent;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

/**
 * PathMatchingHandlerChainResolver
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-03 20:22:51
 */
public class PathMatchingHandlerChainResolver implements HandlerChainResolver<DisruptorEvent> {

	/**
	 * handlerChain管理器
	 */
	private HandlerChainManager<DisruptorEvent> handlerChainManager;

	/**
	 * 路径匹配器
	 */
	private PathMatcher pathMatcher;

	public PathMatchingHandlerChainResolver() {
		this.pathMatcher = new AntPathMatcher();
		this.handlerChainManager = new DefaultHandlerChainManager();
	}

	public HandlerChainManager<DisruptorEvent> getHandlerChainManager() {
		return handlerChainManager;
	}

	public void setHandlerChainManager(HandlerChainManager<DisruptorEvent> handlerChainManager) {
		this.handlerChainManager = handlerChainManager;
	}

	public PathMatcher getPathMatcher() {
		return pathMatcher;
	}

	public void setPathMatcher(PathMatcher pathMatcher) {
		this.pathMatcher = pathMatcher;
	}


	public HandlerChain<DisruptorEvent> getChain(DisruptorEvent event,
		HandlerChain<DisruptorEvent> originalChain) {
		HandlerChainManager<DisruptorEvent> handlerChainManager = getHandlerChainManager();
		if (!handlerChainManager.hasChains()) {
			return null;
		}
		String eventURI = getPathWithinEvent(event);
		for (String pathPattern : handlerChainManager.getChainNames()) {
			if (pathMatches(pathPattern, eventURI)) {
				LogUtil.info(
					"Matched path pattern [" + pathPattern + "] for eventURI [" + eventURI + "].  "
						+
						"Utilizing corresponding handler chain...");
				return handlerChainManager.proxy(originalChain, pathPattern);
			}
		}
		return null;
	}

	protected boolean pathMatches(String pattern, String path) {
		PathMatcher pathMatcher = getPathMatcher();
		return pathMatcher.match(pattern, path);
	}

	protected String getPathWithinEvent(DisruptorEvent event) {
		return event.getRouteExpression();
	}

}
