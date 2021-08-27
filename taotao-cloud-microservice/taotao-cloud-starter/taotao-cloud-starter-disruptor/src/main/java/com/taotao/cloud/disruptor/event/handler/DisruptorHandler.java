package com.taotao.cloud.disruptor.event.handler;


import com.taotao.cloud.disruptor.event.DisruptorEvent;
import com.taotao.cloud.disruptor.event.handler.chain.HandlerChain;

public interface DisruptorHandler<T extends DisruptorEvent> {

	public void doHandler(T event, HandlerChain<T> handlerChain) throws Exception;
	
}
