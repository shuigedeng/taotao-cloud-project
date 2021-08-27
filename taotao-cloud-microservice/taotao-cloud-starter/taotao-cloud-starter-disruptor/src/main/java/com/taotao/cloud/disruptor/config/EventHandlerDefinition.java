package com.taotao.cloud.disruptor.config;

import java.util.LinkedHashMap;
import java.util.Map;

public class EventHandlerDefinition {

	/**
	 * 当前处理器所在位置
	 */
	private int order = 0;

	/**
	 * 处理器链定义
	 */
	private String definitions = null;
	private Map<String /* ruleExpress */, String /* handler names */> definitionMap = new LinkedHashMap<String, String>();
	
	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	public String getDefinitions() {
		return definitions;
	}

	public void setDefinitions(String definitions) {
		this.definitions = definitions;
	}

	public Map<String, String> getDefinitionMap() {
		return definitionMap;
	}

	public void setDefinitionMap(Map<String, String> definitionMap) {
		this.definitionMap = definitionMap;
	}

}
