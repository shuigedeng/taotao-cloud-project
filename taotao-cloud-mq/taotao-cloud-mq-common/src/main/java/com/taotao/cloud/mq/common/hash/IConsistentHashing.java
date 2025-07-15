//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.taotao.cloud.mq.common.hash;

import java.util.Map;

public interface IConsistentHashing<T> {
    T get(String var1);

    IConsistentHashing add(T var1);

    IConsistentHashing remove(T var1);

    Map<Integer, T> nodeMap();
}
