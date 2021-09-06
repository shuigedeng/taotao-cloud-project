/**
 * HandlerChainManager
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-03 20:20:44
 */
package com.taotao.cloud.disruptor.handler;

import com.taotao.cloud.disruptor.event.DisruptorEvent;
import java.util.Map;
import java.util.Set;

/**
 * HandlerChain管理器，负责创建和维护HandlerChain
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-03 20:20:50
 */
public interface HandlerChainManager<T extends DisruptorEvent> {

	/**
	 * 获取所有HandlerChain
	 *
	 * @return {@link java.util.Map }
	 * @author shuigedeng
	 * @since 2021-09-03 20:21:01
	 */
	Map<String, DisruptorHandler<T>> getHandlers();

	/**
	 * 根据指定的chainName获取Handler列表
	 *
	 * @param chainName chainName
	 * @return {@link com.taotao.cloud.disruptor.handler.NamedHandlerList }
	 * @author shuigedeng
	 * @since 2021-09-03 20:21:08
	 */
	NamedHandlerList<T> getChain(String chainName);

	/**
	 * 是否有HandlerChain
	 *
	 * @return boolean
	 * @author shuigedeng
	 * @since 2021-09-03 20:21:14
	 */
	boolean hasChains();

	/**
	 * 获取HandlerChain名称列表
	 *
	 * @return {@link java.util.Set }
	 * @author shuigedeng
	 * @since 2021-09-03 20:21:19
	 */
	Set<String> getChainNames();

	/**
	 * 生成代理HandlerChain,先执行chainName指定的filerChian,最后执行servlet容器的original
	 *
	 * @param original  original
	 * @param chainName chainName
	 * @return {@link com.taotao.cloud.disruptor.handler.HandlerChain }
	 * @author shuigedeng
	 * @since 2021-09-03 20:21:28
	 */
	HandlerChain<T> proxy(HandlerChain<T> original, String chainName);

	/**
	 * 增加handler到handler列表中
	 *
	 * @param name    name
	 * @param handler handler
	 * @author shuigedeng
	 * @since 2021-09-03 20:21:34
	 */
	void addHandler(String name, DisruptorHandler<T> handler);

	/**
	 * 创建HandlerChain
	 *
	 * @param chainName       chainName
	 * @param chainDefinition chainDefinition
	 * @author shuigedeng
	 * @since 2021-09-03 20:21:40
	 */
	void createChain(String chainName, String chainDefinition);

	/**
	 * 追加handler到指定的HandlerChian中
	 *
	 * @param chainName   chainName
	 * @param handlerName handlerName
	 * @author shuigedeng
	 * @since 2021-09-03 20:21:46
	 */
	void addToChain(String chainName, String handlerName);

}
