//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.taotao.cloud.mq.common.hash.core;


import com.taotao.cloud.mq.common.hash.api.IHashCode;

public abstract class AbstractHashCode implements IHashCode {
    public int hash(String text) {
        return null == text ? 0 : this.doHash(text);
    }

    protected abstract int doHash(String var1);
}
