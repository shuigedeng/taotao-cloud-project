/*
 * Copyright (c) 2019-2029, Dreamlu 卢春梦 (596392912@qq.com & www.dreamlu.net).
 * <p>
 * Licensed under the GNU LESSER GENERAL PUBLIC LICENSE 3.0;
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.gnu.org/licenses/lgpl.html
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.taotao.cloud.processor.common;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * MultiSetMap
 */
public class MultiSetMap<K, V> {

	private transient final Map<K, Set<V>> map;

	public MultiSetMap() {
		map = new HashMap<>();
	}

	private Set<V> createSet() {
		return new HashSet<>();
	}

	/**
	 * put to MultiSetMap
	 *
	 * @param key   键
	 * @param value 值
	 * @return boolean
	 */
	public boolean put(K key, V value) {
		Set<V> set = map.get(key);
		if (set == null) {
			set = createSet();
			if (set.add(value)) {
				map.put(key, set);
				return true;
			} else {
				throw new AssertionError("New set violated the set spec");
			}
		} else {
			return set.add(value);
		}
	}

	/**
	 * 是否包含某个key
	 *
	 * @param key key
	 * @return 结果
	 */
	public boolean containsKey(K key) {
		return map.containsKey(key);
	}

	/**
	 * 是否包含 value 中的某个值
	 *
	 * @param value value
	 * @return 是否包含
	 */
	public boolean containsVal(V value) {
		Collection<Set<V>> values = map.values();
		return values.stream().anyMatch(vs -> vs.contains(value));
	}

	/**
	 * key 集合
	 *
	 * @return keys
	 */
	public Set<K> keySet() {
		return map.keySet();
	}

	/**
	 * put list to MultiSetMap
	 *
	 * @param key 键
	 * @param set 值列表
	 * @return boolean
	 */
	public boolean putAll(K key, Set<V> set) {
		if (set == null) {
			return false;
		}
		Set<V> vSet = map.computeIfAbsent(key, k -> createSet());
		vSet.addAll(set);
		return true;
	}

	/**
	 * put MultiSetMap to MultiSetMap
	 *
	 * @param data MultiSetMap
	 * @return boolean
	 */
	public boolean putAll(MultiSetMap<K, V> data) {
		if (data == null || data.isEmpty()) {
			return false;
		} else {
			for (K k : data.keySet()) {
				this.putAll(k, data.get(k));
			}
			return true;
		}
	}

	/**
	 * get List by key
	 *
	 * @param key 键
	 * @return List
	 */
	public Set<V> get(K key) {
		return map.get(key);
	}

	/**
	 * clear MultiSetMap
	 */
	public void clear() {
		map.clear();
	}

	/**
	 * isEmpty
	 *
	 * @return isEmpty
	 */
	public boolean isEmpty() {
		return map.isEmpty();
	}

	@Override
	public String toString() {
		return map.toString();
	}

}
