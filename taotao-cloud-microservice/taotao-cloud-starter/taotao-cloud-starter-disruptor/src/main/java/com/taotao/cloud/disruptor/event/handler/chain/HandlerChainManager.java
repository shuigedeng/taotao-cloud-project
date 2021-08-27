package com.taotao.cloud.disruptor.event.handler.chain;

import com.taotao.cloud.disruptor.event.DisruptorEvent;
import com.taotao.cloud.disruptor.event.handler.DisruptorHandler;
import com.taotao.cloud.disruptor.event.handler.NamedHandlerList;
import java.util.Map;
import java.util.Set;


/**
 * HandlerChain管理器，负责创建和维护HandlerChain
 */
public interface HandlerChainManager<T extends DisruptorEvent> {

	/*
	 * 获取所有HandlerChain
	 * @return
	 */
    Map<String, DisruptorHandler<T>> getHandlers();

    /*
     * 根据指定的chainName获取Handler列表
     */
    NamedHandlerList<T> getChain(String chainName);

    /*
     * 是否有HandlerChain
     */
    boolean hasChains();

    /*
     * 获取HandlerChain名称列表
     */
    Set<String> getChainNames();

    /*
     * <p>生成代理HandlerChain,先执行chainName指定的filerChian,最后执行servlet容器的original<p>
     */
    HandlerChain<T> proxy(HandlerChain<T> original, String chainName);

   /*
    * 
    * <p>增加handler到handler列表中<p>
    */
    void addHandler(String name, DisruptorHandler<T> handler);
    
    /*
     * <p>创建HandlerChain<p>
     */
    void createChain(String chainName, String chainDefinition);

    /*
     * <p>追加handler到指定的HandlerChian中<p>
     */
    void addToChain(String chainName, String handlerName);
	
}
