package com.taotao.cloud.disruptor.event.handler.chain;


import com.taotao.cloud.disruptor.event.DisruptorEvent;

public interface HandlerChain<T extends DisruptorEvent>{

	void doHandler(T event) throws Exception;
	
}
