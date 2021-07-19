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
package com.taotao.cloud.gateway.loadBalancer;

import java.util.Arrays;
import java.util.Random;

/**
 * WeightMeta<br>
 *
 * @author shuigedeng
 * @version 1.0.0
 * @since 2020/4/29 22:10
 */
public class WeightMeta<T> {

	private final Random ran = new Random();
	private final T[] nodes;
	private final int[] weights;
	private final int maxW;

	public WeightMeta(T[] nodes, int[] weights) {
		this.nodes = nodes;
		this.weights = weights;
		this.maxW = weights[weights.length - 1];
	}

	/**
	 * 该方法返回权重随机对象
	 *
	 * @return
	 */
	public T random() {
		int index = Arrays.binarySearch(weights, ran.nextInt(maxW) + 1);
		if (index < 0) {
			index = -1 - index;
		}
		return nodes[index];
	}

	public T random(int ranInt) {
		if (ranInt > maxW) {
			ranInt = maxW;
		} else if (ranInt < 0) {
			ranInt = 1;
		} else {
			ranInt++;
		}
		int index = Arrays.binarySearch(weights, ranInt);
		if (index < 0) {
			index = -1 - index;
		}
		return nodes[index];
	}

	@Override
	public String toString() {
		StringBuilder l1 = new StringBuilder();
		StringBuilder l2 = new StringBuilder("[random]\t");
		StringBuilder l3 = new StringBuilder("[node]\t\t");
		l1.append(this.getClass().getName()).append(":").append(this.hashCode()).append(":\n")
			.append("[index]\t\t");
		for (int i = 0; i < weights.length; i++) {
			l1.append(i).append("\t");
			l2.append(weights[i]).append("\t");
			l3.append(nodes[i]).append("\t");
		}
		l1.append("\n");
		l2.append("\n");
		l3.append("\n");
		return l1.append(l2).append(l3).toString();
	}
}
