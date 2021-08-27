package com.taotao.cloud.disruptor.event.handler;

import com.taotao.cloud.disruptor.event.DisruptorEvent;
import com.taotao.cloud.disruptor.event.handler.chain.HandlerChain;
import java.util.List;



public interface NamedHandlerList<T extends DisruptorEvent> extends List<DisruptorHandler<T>> {
	 
	/**
     * Returns the configuration-unique name assigned to this {@code Handler} list.
     * @return configuration-unique name
     */
    String getName();

    /**
     * Returns a new {@code HandlerChain<T>} instance that will first execute this list's {@code Handler}s (in list order)
     * and end with the execution of the given {@code handlerChain} instance.
     * @param handlerChain {@code HandlerChain<T>} instance 
     * @return  {@code HandlerChain<T>} instance 
     */
    HandlerChain<T> proxy(HandlerChain<T> handlerChain);
    
}
