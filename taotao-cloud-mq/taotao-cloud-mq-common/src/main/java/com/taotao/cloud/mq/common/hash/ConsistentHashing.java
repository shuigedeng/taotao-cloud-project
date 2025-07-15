//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.taotao.cloud.mq.common.hash;

import com.taotao.cloud.mq.common.hash.api.IHashCode;
import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

public class ConsistentHashing<T> implements IConsistentHashing<T> {
    private final int virtualNum;
    private final IHashCode hashCode;
    private final TreeMap<Integer, T> nodeMap = new TreeMap();

    public ConsistentHashing(int virtualNum, IHashCode hashCode) {
        this.virtualNum = virtualNum;
        this.hashCode = hashCode;
    }

    public T get(String key) {
        int hashCode = this.hashCode.hash(key);
        Integer target = hashCode;
        if (!this.nodeMap.containsKey(hashCode)) {
            target = (Integer)this.nodeMap.ceilingKey(hashCode);
            if (target == null && !this.nodeMap.isEmpty()) {
                target = (Integer)this.nodeMap.firstKey();
            }
        }

        return (T)this.nodeMap.get(target);
    }

    public IConsistentHashing add(T node) {
        for(int i = 0; i < this.virtualNum; ++i) {
            int nodeKey = this.hashCode.hash(node.toString() + "-" + i);
            this.nodeMap.put(nodeKey, node);
        }

        return this;
    }

    public IConsistentHashing remove(T node) {
        for(int i = 0; i < this.virtualNum; ++i) {
            int nodeKey = this.hashCode.hash(node.toString() + "-" + i);
            this.nodeMap.remove(nodeKey);
        }

        return this;
    }

    public Map<Integer, T> nodeMap() {
        return Collections.unmodifiableMap(this.nodeMap);
    }
}
