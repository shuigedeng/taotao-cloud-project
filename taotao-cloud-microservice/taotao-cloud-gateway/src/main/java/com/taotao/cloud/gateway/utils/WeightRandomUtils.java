/*
 * Copyright 2002-2021 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.taotao.cloud.gateway.utils;

import java.util.Map;

/**
 * WeightRandomUtils<br>
 *
 * @author dengtao
 * @since 2020/4/29 22:10
 * @version 1.0.0
 */
public class WeightRandomUtils {
	public static <T> WeightMeta<T> buildWeightMeta(final Map<T, Integer> weightMap) {
		if (weightMap.isEmpty()) {
			return null;
		}
		final int size = weightMap.size();
		Object[] nodes = new Object[size];
		int[] weights = new int[size];
		int index = 0;
		int weightAdder = 0;
		for (Map.Entry<T, Integer> each : weightMap.entrySet()) {
			nodes[index] = each.getKey();
			weights[index++] = (weightAdder = weightAdder + each.getValue());
		}
		return new WeightMeta<>((T[]) nodes, weights);
	}
}
