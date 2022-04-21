/*
 * Copyright (c) 2018-2022 the original author or authors.
 *
 * Licensed under the GNU LESSER GENERAL PUBLIC LICENSE, Version 3 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.gnu.org/licenses/lgpl-3.0.html
 *
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.taotao.cloud.sms.model;

import java.util.List;
import java.util.Map;
import org.springframework.lang.Nullable;

/**
 * 抽象处理实现配置
 *
 * @param <T> 模板ID类型
 * @author shuigedeng
 */
public abstract class AbstractHandlerProperties<T> {

	/**
	 * 权重
	 */
	private int weight = 1;

	/**
	 * 短信模板
	 */
	private Map<String, T> templates;

	/**
	 * 参数顺序
	 */
	private Map<String, List<String>> paramsOrders;

	/**
	 * 获取短信模板
	 *
	 * @param type 类型
	 * @return 短信模板
	 */
	@Nullable
	public final T getTemplates(String type) {
		return templates == null ? null : templates.get(type);
	}

	/**
	 * 返回参数顺序
	 *
	 * @param type 类型
	 * @return 参数顺序
	 */
	public final List<String> getParamsOrder(String type) {
		return paramsOrders.get(type);
	}

	/**
	 * 获取权重,权重最小值为0
	 *
	 * @return 权重
	 */
	public int getWeight() {
		return weight;
	}

	/**
	 * 设置权重,权重最小值为0
	 *
	 * @param weight 权重
	 */
	public void setWeight(int weight) {
		if (weight >= 0) {
			this.weight = weight;
		}
	}

	public Map<String, T> getTemplates() {
		return templates;
	}

	public void setTemplates(Map<String, T> templates) {
		this.templates = templates;
	}

	public Map<String, List<String>> getParamsOrders() {
		return paramsOrders;
	}

	public void setParamsOrders(
		Map<String, List<String>> paramsOrders) {
		this.paramsOrders = paramsOrders;
	}
}
