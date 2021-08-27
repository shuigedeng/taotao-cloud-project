package com.taotao.cloud.disruptor.event.handler.chain;


import com.taotao.cloud.disruptor.event.DisruptorEvent;

public interface HandlerChainResolver<T extends DisruptorEvent> {

	HandlerChain<T> getChain(T event , HandlerChain<T> originalChain);
	
}
