/*
 * Copyright (c) 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
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

package com.taotao.cloud.cache.model;

/**
 * 双向链表节点
 * @author shuigedeng
 * @since 2024.06
 * @param <K> key
 * @param <V> value
 */
public class DoubleListNode<K, V> {

    /**
     * 键
     * @since 2024.06
     */
    private K key;

    /**
     * 值
     * @since 2024.06
     */
    private V value;

    /**
     * 前一个节点
     * @since 2024.06
     */
    private DoubleListNode<K, V> pre;

    /**
     * 后一个节点
     * @since 2024.06
     */
    private DoubleListNode<K, V> next;

    public K key() {
        return key;
    }

    public DoubleListNode<K, V> key(K key) {
        this.key = key;
        return this;
    }

    public V value() {
        return value;
    }

    public DoubleListNode<K, V> value(V value) {
        this.value = value;
        return this;
    }

    public DoubleListNode<K, V> pre() {
        return pre;
    }

    public DoubleListNode<K, V> pre(DoubleListNode<K, V> pre) {
        this.pre = pre;
        return this;
    }

    public DoubleListNode<K, V> next() {
        return next;
    }

    public DoubleListNode<K, V> next(DoubleListNode<K, V> next) {
        this.next = next;
        return this;
    }
}
