package com.taotao.cloud.disruptor.event.handler;

import com.taotao.cloud.disruptor.event.DisruptorEvent;
import com.taotao.cloud.disruptor.event.handler.chain.HandlerChain;
import com.taotao.cloud.disruptor.event.handler.chain.HandlerChainResolver;
import com.taotao.cloud.disruptor.exception.EventHandleException;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class AbstractRouteableEventHandler<T extends DisruptorEvent> extends AbstractEnabledEventHandler<T> {

	private static final Logger LOG = LoggerFactory.getLogger(AbstractRouteableEventHandler.class);

	/**
	 * 用来判定使用那个HandlerChian
	 */
	protected HandlerChainResolver<T> handlerChainResolver;

	public AbstractRouteableEventHandler() {
		super();
	}

	public AbstractRouteableEventHandler(HandlerChainResolver<T> handlerChainResolver) {
		super();
		this.handlerChainResolver = handlerChainResolver;
	}

	@Override
	protected void doHandlerInternal(T event, HandlerChain<T> handlerChain) throws Exception {
		Throwable t = null;
		try {
			this.executeChain(event, handlerChain);
		} catch (Throwable throwable) {
			t = throwable;
		}
		if (t != null) {
			if (t instanceof IOException) {
				throw (IOException) t;
			}
			String msg = "Handlered event failed.";
			throw new EventHandleException(msg, t);
		}
	}

	protected HandlerChain<T> getExecutionChain(T event, HandlerChain<T> origChain) {
		HandlerChain<T> chain = origChain;

		HandlerChainResolver<T> resolver = getHandlerChainResolver();
		if (resolver == null) {
			LOG.debug("No HandlerChainResolver configured.  Returning original HandlerChain.");
			return origChain;
		}

		HandlerChain<T> resolved = resolver.getChain(event, origChain);
		if (resolved != null) {
			LOG.trace("Resolved a configured HandlerChain for the current event.");
			chain = resolved;
		} else {
			LOG.trace("No HandlerChain configured for the current event.  Using the default.");
		}
		
		return chain;
	}

	protected void executeChain(T event, HandlerChain<T> origChain) throws Exception {
		HandlerChain<T> chain = getExecutionChain(event, origChain);
		chain.doHandler(event);
	}

	public HandlerChainResolver<T> getHandlerChainResolver() {
		return handlerChainResolver;
	}

	public void setHandlerChainResolver(HandlerChainResolver<T> handlerChainResolver) {
		this.handlerChainResolver = handlerChainResolver;
	}


}
