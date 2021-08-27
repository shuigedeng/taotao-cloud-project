package com.taotao.cloud.disruptor.event.handler;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

import com.lmax.disruptor.spring.boot.event.DisruptorEvent;

public abstract class AbstractPathMatchEventHandler<T extends DisruptorEvent> extends AbstractAdviceEventHandler<T>  implements PathProcessor<T> {

	private static final Logger LOG = LoggerFactory.getLogger(AbstractPathMatchEventHandler.class);

	protected PathMatcher pathMatcher = new AntPathMatcher();
	
	// 需要过滤的路径
	protected List<String> appliedPaths = new ArrayList<String>();

	@Override
	public DisruptorHandler<T> processPath(String path) {
		this.appliedPaths.add(path);
		return this;
	}
	
	protected String getPathWithinEvent(T event) {
		return event.getRouteExpression();
	}

	protected boolean pathsMatch(String path, T event) {
		String eventExp = getPathWithinEvent(event);
		LOG.trace("Attempting to match pattern '{}' with current Event Expression '{}'...", path, eventExp);
		return pathsMatch(path, eventExp);
	}

	protected boolean pathsMatch(String pattern, String path) {
		return pathMatcher.match(pattern, path);
	}
	
	
	protected boolean preHandle(T event) throws Exception {

		if (this.appliedPaths == null || this.appliedPaths.isEmpty()) {
			if (LOG.isTraceEnabled()) {
				LOG.trace("appliedPaths property is null or empty.  This Handler will passthrough immediately.");
			}
			return true;
		}

		for (String path : this.appliedPaths) {
			// If the path does match, then pass on to the subclass
			// implementation for specific checks
			// (first match 'wins'):
			if (pathsMatch(path, event)) {
				LOG.trace("Current Event Expression matches pattern '{}'.  Determining handler chain execution...", path);
				return isHandlerChainContinued(event, path);
			}
		}

		// no path matched, allow the request to go through:
		return true;
	}

	private boolean isHandlerChainContinued(T event, String path) throws Exception {

		if (isEnabled(event, path)) { // isEnabled check

			if (LOG.isTraceEnabled()) {
				LOG.trace("Handler '{}' is enabled for the current event under path '{}'.  " + "Delegating to subclass implementation for 'onPreHandle' check.", new Object[] { getName(), path });
			}
			// The handler is enabled for this specific request, so delegate to
			// subclass implementations
			// so they can decide if the request should continue through the
			// chain or not:
			return onPreHandle(event);
		}

		if (LOG.isTraceEnabled()) {
			LOG.trace("Handler '{}' is disabled for the current event under path '{}'.  " + "The next element in the HandlerChain will be called immediately.", new Object[] { getName(), path });
		}
		// This handler is disabled for this specific request,
		// return 'true' immediately to indicate that the handler will not
		// process the request
		// and let the request/response to continue through the handler chain:
		return true;
	}

	protected boolean onPreHandle(T event) throws Exception {
		return true;
	}
	
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
