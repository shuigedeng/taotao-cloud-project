/*
 * Copyright 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
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
import java.util.ArrayList;
import java.util.List;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

/**
 * AbstractPathMatchEventHandler
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-03 20:16:59
 */
public abstract class AbstractPathMatchEventHandler<T extends DisruptorEvent> extends
	AbstractAdviceEventHandler<T> implements PathProcessor<T> {

	protected PathMatcher pathMatcher = new AntPathMatcher();

	// 需要过滤的路径
	protected List<String> appliedPaths = new ArrayList<String>();

	@Override
	public DisruptorHandler<T> processPath(String path) {
		this.appliedPaths.add(path);
		return this;
	}

	/**
	 * getPathWithinEvent
	 *
	 * @param event event
	 * @return {@link java.lang.String }
	 * @author shuigedeng
	 * @since 2021-09-03 20:17:56
	 */
	protected String getPathWithinEvent(T event) {
		return event.getRouteExpression();
	}

	/**
	 * pathsMatch
	 *
	 * @param path  path
	 * @param event event
	 * @return boolean
	 * @author shuigedeng
	 * @since 2021-09-03 20:18:02
	 */
	protected boolean pathsMatch(String path, T event) {
		String eventExp = getPathWithinEvent(event);
		LogUtil.info("Attempting to match pattern '{}' with current Event Expression '{}'...", path,
			eventExp);
		return pathsMatch(path, eventExp);
	}

	/**
	 * pathsMatch
	 *
	 * @param pattern pattern
	 * @param path    path
	 * @return boolean
	 * @author shuigedeng
	 * @since 2021-09-03 20:17:59
	 */
	protected boolean pathsMatch(String pattern, String path) {
		return pathMatcher.match(pattern, path);
	}

	/**
	 * preHandle
	 *
	 * @param event event
	 * @return boolean
	 * @author shuigedeng
	 * @since 2021-09-03 20:18:06
	 */
	protected boolean preHandle(T event) throws Exception {

		if (this.appliedPaths == null || this.appliedPaths.isEmpty()) {
			LogUtil.info(
				"appliedPaths property is null or empty.  This Handler will passthrough immediately.");
			return true;
		}

		for (String path : this.appliedPaths) {
			// If the path does match, then pass on to the subclass
			// implementation for specific checks
			// (first match 'wins'):
			if (pathsMatch(path, event)) {
				LogUtil.info(
					"Current Event Expression matches pattern '{}'.  Determining handler chain execution...",
					path);
				return isHandlerChainContinued(event, path);
			}
		}

		// no path matched, allow the request to go through:
		return true;
	}

	private boolean isHandlerChainContinued(T event, String path) throws Exception {

		if (isEnabled(event, path)) { // isEnabled check

			LogUtil.info("Handler '{}' is enabled for the current event under path '{}'.  "
					+ "Delegating to subclass implementation for 'onPreHandle' check.",
				new Object[]{getName(), path});
			// The handler is enabled for this specific request, so delegate to
			// subclass implementations
			// so they can decide if the request should continue through the
			// chain or not:
			return onPreHandle(event);
		}

		LogUtil.info("Handler '{}' is disabled for the current event under path '{}'.  "
				+ "The next element in the HandlerChain will be called immediately.",
			new Object[]{getName(), path});
		// This handler is disabled for this specific request,
		// return 'true' immediately to indicate that the handler will not
		// process the request
		// and let the request/response to continue through the handler chain:
		return true;
	}

	/**
	 * onPreHandle
	 *
	 * @param event event
	 * @return boolean
	 * @author shuigedeng
	 * @since 2021-09-03 20:17:40
	 */
	protected boolean onPreHandle(T event) throws Exception {
		return true;
	}

	/**
	 * isEnabled
	 *
	 * @param event event
	 * @param path  path
	 * @return boolean
	 * @author shuigedeng
	 * @since 2021-09-03 20:17:42
	 */
	protected boolean isEnabled(T event, String path) throws Exception {
		return isEnabled(event);
	}

	public PathMatcher getPathMatcher() {
		return pathMatcher;
	}

	public void setPathMatcher(PathMatcher pathMatcher) {
		this.pathMatcher = pathMatcher;
	}

	public List<String> getAppliedPaths() {
		return appliedPaths;
	}

}
