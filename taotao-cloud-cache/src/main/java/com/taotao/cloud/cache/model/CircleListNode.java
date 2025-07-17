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
 * 循环链表节点
 * @author shuigedeng
 * @since 2024.06
 * @param <K> key
 * @param <V> value
 */
public class CircleListNode<K, V> {

    /**
     * 键
     */
    private K key;

    /**
     * 值
     */
    private V value = null;

    /**
     * 是否被访问过
     */
    private boolean accessFlag = false;

    /**
     * 后一个节点
     */
    private CircleListNode<K, V> pre;

    /**
     * 后一个节点
     */
    private CircleListNode<K, V> next;

    public CircleListNode(K key) {
        this.key = key;
    }

    public K key() {
        return key;
    }

    public CircleListNode<K, V> key(K key) {
        this.key = key;
        return this;
    }

    public V value() {
        return value;
    }

    public CircleListNode<K, V> value(V value) {
        this.value = value;
        return this;
    }

    public boolean accessFlag() {
        return accessFlag;
    }

    public CircleListNode<K, V> accessFlag(boolean accessFlag) {
        this.accessFlag = accessFlag;
        return this;
    }

    public CircleListNode<K, V> pre() {
        return pre;
    }

    public CircleListNode<K, V> pre(CircleListNode<K, V> pre) {
        this.pre = pre;
        return this;
    }

    public CircleListNode<K, V> next() {
        return next;
    }

    public CircleListNode<K, V> next(CircleListNode<K, V> next) {
        this.next = next;
        return this;
    }

    @Override
    public String toString() {
        return "CircleListNode{"
                + "key="
                + key
                + ", value="
                + value
                + ", accessFlag="
                + accessFlag
                + ", pre="
                + pre
                + ", next="
                + next
                + '}';
    }
}
